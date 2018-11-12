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

import java.math.BigInteger

import org.web3j.utils.Numeric
import org.web3j.crypto.Hash

package object ring {

  implicit class RichByteArray(bytes: Array[Byte]) {
    def addAddress(address: String): Array[Byte] = bytes ++ Numeric.toBytesPadded(Numeric.toBigInt(address), 20)

    def addUint256(num: BigInteger): Array[Byte] = bytes ++ Numeric.toBytesPadded(num, 32)

    def addUint16(num: BigInteger): Array[Byte] = bytes ++ Numeric.toBytesPadded(num, 2)

    def addBoolean(b: Boolean): Array[Byte] = bytes :+ (if (b) 1 else 0).toByte

    def addRawBytes(otherBytes: Array[Byte]): Array[Byte] = bytes ++ otherBytes

    def addHex(hex: String): Array[Byte] = bytes ++ Numeric.hexStringToByteArray(hex)
  }

  def getOrderHash(essential: Order): String = {
    val data = Array[Byte]()
      .addUint256(essential.amountS.bigInteger)
      .addUint256(essential.amountB.bigInteger)
      .addUint256(essential.feeAmount.bigInteger)
      .addUint256(BigInt(essential.validSince).bigInteger)
      .addUint256(BigInt(essential.validUntil).bigInteger)
      .addAddress(essential.owner)
      .addAddress(essential.tokenS)
      .addAddress(essential.tokenB)
      .addAddress(essential.dualAuthAddress)
      //todo:broker
      .addAddress(essential.broker)
      //todo:orderInterceptor
      .addAddress(essential.orderInterceptor)
      .addAddress(essential.wallet)
      .addAddress(essential.tokenReceipt)
      .addAddress(essential.feeToken)
      .addUint16(BigInt(essential.walletSplitPercentage).bigInteger)
      .addUint16(BigInt(essential.feePercentage).bigInteger)
      .addUint16(BigInt(essential.tokenSFeePercentage).bigInteger)
      .addUint16(BigInt(essential.tokenBFeePercentage).bigInteger)
      .addBoolean(essential.allOrNone)

    Numeric.toHexString(Hash.sha3(data))
  }
}
