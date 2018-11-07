/*
 * Copyright 2018 Loopring Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.loopring.lightcone.lib

import scala.collection.mutable.{ HashMap ⇒ HMap }

// warning: 代码顺序不能调整！！！！！！
case class RingGenerator(
    lrcAddress: String,
    ringsInfo: Ring
) {

  val ORDER_VERSION = 0
  val SERIALIZATION_VERSION = 0

  ringsInfo.orders.map(x ⇒ assert(x.hash.nonEmpty))

  var datastream = ByteStream()
  var tablestream = ByteStream()
  var orderSpendableSMap = HMap.empty[String, Int]
  var orderSpendableFeeMap = HMap.empty[String, Int]

  def toSubmitableParam(): String = {
    val numSpendables = setupSpendables()

    datastream.addNumber(0, 32)
    createMiningTable()
    ringsInfo.orders.map(createOrderTable)

    val stream = ByteStream()
    stream.addNumber(SERIALIZATION_VERSION, 2)
    stream.addNumber(ringsInfo.orders.length, 2)
    stream.addNumber(ringsInfo.ringOrderIndex.length, 2)
    stream.addNumber(numSpendables, 2)
    stream.addHex(tablestream.getData)

    ringsInfo.ringOrderIndex.map(orderIdxs ⇒ {
      stream.addNumber(orderIdxs.length, 1)
      orderIdxs.map(o ⇒ stream.addNumber(o, 1))
      stream.addNumber(0, 8 - orderIdxs.length)
    })

    stream.addNumber(0, 32)
    stream.addHex(datastream.getData)

    stream.getData
  }

  private def setupSpendables(): Int = {
    var numSpendables = 0
    var ownerTokens = HMap.empty[String, Int]

    ringsInfo.orders.map(order ⇒ {
      assert(order.hash.nonEmpty)

      val tokenFee = if (order.feeToken.nonEmpty) order.feeToken else lrcAddress

      val tokensKey = (order.owner + "-" + order.tokenS).toLowerCase
      ownerTokens.get(tokensKey) match {
        case Some(x: Int) ⇒
          orderSpendableSMap += order.hash -> x
        case _ ⇒
          ownerTokens += tokensKey -> numSpendables
          orderSpendableSMap += order.hash -> numSpendables
          numSpendables += 1
      }

      ownerTokens.get((order.owner + "-" + tokenFee).toLowerCase) match {
        case Some(x: Int) ⇒
          orderSpendableFeeMap += order.hash -> x
        case _ ⇒
          ownerTokens += tokensKey -> numSpendables
          orderSpendableFeeMap += order.hash -> numSpendables
          numSpendables += 1
      }
    })

    numSpendables
  }

  // 注意:
  // 1. 对于relay来说miner就是transactionOrigin
  private def createMiningTable(): Unit = {
    require(ringsInfo.miner.nonEmpty)

    val transactionOrigin = if (ringsInfo.transactionOrigin.nonEmpty) ringsInfo.transactionOrigin else ringsInfo.miner
    val feeRecipient = if (ringsInfo.feeReceipt.nonEmpty) ringsInfo.feeReceipt else transactionOrigin

    if (feeRecipient safeneq transactionOrigin) {
      insertOffset(datastream.addAddress(ringsInfo.feeReceipt))
    } else {
      insertDefault()
    }

    if (ringsInfo.miner safeneq feeRecipient) {
      insertOffset(datastream.addAddress(ringsInfo.miner))
    } else {
      insertDefault()
    }

    if (ringsInfo.sig.nonEmpty && (ringsInfo.miner safeneq transactionOrigin)) {
      insertOffset(datastream.addHex(createBytes(ringsInfo.sig)))
      addPadding()
    } else {
      insertDefault()
    }
  }

  private def createOrderTable(order: Order): Unit = {
    addPadding()

    insertOffset(ORDER_VERSION)
    insertOffset(datastream.addAddress(order.owner))
    insertOffset(datastream.addAddress(order.tokenS))
    insertOffset(datastream.addAddress(order.tokenB))
    insertOffset(datastream.addNumber(order.amountS, 32, false))
    insertOffset(datastream.addNumber(order.amountB, 32, false))
    insertOffset(datastream.addNumber(order.validSince, 4, false))

    orderSpendableSMap.get(order.hash) match {
      case Some(x: Int) ⇒ tablestream.addNumber(x.intValue(), 2)
      case _            ⇒ throw new Exception("ringGenerator get " + order.hash + "orderSpendableS failed")
    }
    orderSpendableFeeMap.get(order.hash) match {
      case Some(x: Int) ⇒ tablestream.addNumber(x.intValue(), 2)
      case _            ⇒ throw new Exception("ringGenerator get " + order.hash + "orderSpendableFee failed")
    }

    if (order.dualAuthAddress.nonEmpty) {
      insertOffset(datastream.addAddress(order.dualAuthAddress))
    } else {
      insertDefault()
    }

    // order.broker 默认占位
    insertDefault()

    // order.interceptor默认占位
    insertDefault()

    if (order.wallet.nonEmpty) {
      insertOffset(datastream.addAddress(order.wallet))
    } else {
      insertDefault()
    }

    if (order.validUntil > 0) {
      insertOffset(datastream.addNumber(order.validUntil, 4, false))
    } else {
      insertDefault()
    }

    if (order.sig.nonEmpty) {
      insertOffset(datastream.addHex(createBytes(order.sig), false))
      addPadding()
    } else {
      insertDefault()
    }

    if (order.dualAuthSig.nonEmpty) {
      insertOffset(datastream.addHex(createBytes(order.dualAuthSig), false))
      addPadding()
    } else {
      insertDefault()
    }

    tablestream.addNumber(if (order.allOrNone) 1 else 0, 2)

    if (order.feeToken.nonEmpty && (order.feeToken safeneq lrcAddress)) {
      insertOffset(datastream.addAddress(order.feeToken))
    } else {
      insertDefault()
    }

    if (order.feeAmount.signum > 0) {
      insertOffset(datastream.addNumber(order.feeAmount, 32, false))
    } else {
      insertDefault()
    }

    tablestream.addNumber(if (order.feePercentage > 0) order.feePercentage else 0, 2)
    tablestream.addNumber(if (order.waiveFeePercentage > 0) order.waiveFeePercentage else 0, 2)
    tablestream.addNumber(if (order.tokenSFeePercentage > 0) order.tokenSFeePercentage else 0, 2)
    tablestream.addNumber(if (order.tokenBFeePercentage > 0) order.tokenBFeePercentage else 0, 2)

    if (order.tokenReceipt.nonEmpty && (order.tokenReceipt safeneq order.owner)) {
      insertOffset(datastream.addAddress(order.tokenReceipt))
    } else {
      insertDefault()
    }

    tablestream.addNumber(if (order.walletSplitPercentage > 0) order.walletSplitPercentage else 0, 2)
  }

  private def createBytes(data: String): String = {
    val bitstream = ByteStream()
    bitstream.addNumber((data.length - 2) / 2, 32)
    bitstream.addRawBytes(data)
    bitstream.getData
  }

  private def insertOffset(offset: Int): Unit = {
    assert(offset % 4 == 0)
    tablestream.addNumber(offset / 4, 2)
  }

  private def insertDefault(): Unit = {
    tablestream.addNumber(0, 2)
  }

  private def addPadding(): Unit = {
    if (datastream.length % 4 != 0) {
      datastream.addNumber(0, 4 - (datastream.length % 4))
    }
  }

}
