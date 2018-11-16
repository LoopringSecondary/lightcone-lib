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

import org.web3j.utils.Numeric
import org.web3j.crypto.{ Hash ⇒ web3Hash }

case class Order(
    owner: String,
    tokenS: String,
    tokenB: String,
    amountS: BigInt,
    amountB: BigInt,
    validSince: Long,
    allOrNone: Boolean,
    feeToken: String,
    feeAmount: BigInt,
    sig: String,
    dualAuthSig: String,
    // option
    hash: String = "",
    validUntil: Long = 0,
    wallet: String = "0x0",
    dualAuthAddress: String = "0x0",
    broker: String = "0x0",
    orderInterceptor: String = "0x0",
    tokenReceipt: String = "0x0",
    version: Int = 0,
    walletSplitPercentage: Int = 0,
    tokenSFeePercentage: Int = 0,
    tokenBFeePercentage: Int = 0,
    waiveFeePercentage: Int = 0,
    tokenSpendableS: BigInt = 0,
    tokenSpendableFee: BigInt = 0,
    brokerSpendableS: BigInt = 0,
    brokerSpendableFee: BigInt = 0,
) {

  val eip712Open = true

  def generateHash: String = {
    if (eip712Open) {
      val typeData = toTypeData()
      typeData.signatureHash()
    } else {
      val data = ByteStream()
      data.addUint(amountS)
      data.addUint(amountB)
      data.addUint(feeAmount)
      data.addUint(BigInt(validSince))
      data.addUint(BigInt(validUntil))
      data.addAddress(owner, true)
      data.addAddress(tokenS, true)
      data.addAddress(tokenB, true)
      data.addAddress(dualAuthAddress, true)
      data.addAddress(broker,true)
      data.addAddress(orderInterceptor, true)
      data.addAddress(wallet, true)
      data.addAddress(tokenReceipt, true)
      data.addAddress(feeToken, true)
      data.addUint16(walletSplitPercentage)
      data.addUint16(tokenSFeePercentage)
      data.addUint16(tokenBFeePercentage)
      data.addBoolean(allOrNone)

      Numeric.toHexString(web3Hash.sha3(data.getBytes))
    }
  }

  def toTypeData(): EIP712TypeData = {
    var types = new EIP712Types
    types += "EIP712Domain" → Seq(
      EIP712TypeUnit(name = "name", typ = "string"),
      EIP712TypeUnit(name = "version", typ = "string")
    )
    types += "Order" → Seq(
      EIP712TypeUnit(name = "amountS", typ = "uint"),
      EIP712TypeUnit(name = "amountB", typ = "uint"),
      EIP712TypeUnit(name = "feeAmount", typ = "uint"),
      EIP712TypeUnit(name = "validSince", typ = "uint"),
      EIP712TypeUnit(name = "validUntil", typ = "uint"),
      EIP712TypeUnit(name = "owner", typ = "address"),
      EIP712TypeUnit(name = "tokenS", typ = "address"),
      EIP712TypeUnit(name = "tokenB", typ = "address"),
      EIP712TypeUnit(name = "dualAuthAddr", typ = "address"),
      EIP712TypeUnit(name = "broker", typ = "address"),
      EIP712TypeUnit(name = "orderInterceptor", typ = "address"),
      EIP712TypeUnit(name = "wallet", typ = "address"),
      EIP712TypeUnit(name = "tokenRecipient", typ = "address"),
      EIP712TypeUnit(name = "feeToken", typ = "address"),
      EIP712TypeUnit(name = "walletSplitPercentage", typ = "uint16"),
      EIP712TypeUnit(name = "tokenSFeePercentage", typ = "uint16"),
      EIP712TypeUnit(name = "tokenBFeePercentage", typ = "uint16"),
      EIP712TypeUnit(name = "allOrNone", typ = "bool")
    )
    val domain = EIP712Domain(name = "Loopring Protocol", version = "2")

    var message = new EIP712Data
    message += "amountS" → amountS
    message += "amountB" → amountB
    message += "feeAmount" → feeAmount
    message += "validSince" → BigInt(validSince)
    message += "validUntil" → BigInt(validUntil)
    message += "owner" → owner
    message += "tokenS" → tokenS
    message += "tokenB" → tokenB
    message += "dualAuthAddr" → dualAuthAddress
    message += "broker" → broker
    message += "orderInterceptor" → orderInterceptor
    message += "wallet" → wallet
    message += "tokenRecipient" → tokenReceipt
    message += "feeToken" → feeToken
    message += "walletSplitPercentage" → walletSplitPercentage
    message += "tokenSFeePercentage" → tokenSFeePercentage
    message += "tokenBFeePercentage" → tokenBFeePercentage
    message += "allOrNone" → allOrNone

    EIP712TypeData(
      types = types,
      primaryType = "Order",
      domain = domain,
      message = message
    )
  }
}
