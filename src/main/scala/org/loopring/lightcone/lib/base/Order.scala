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

import org.web3j.crypto.{ Hash ⇒ web3Hash }
import org.web3j.utils.Numeric

case class Order(
    owner: String,
    tokenS: String,
    tokenB: String,
    amountS: BigInt,
    amountB: BigInt,
    validSince: Long,
    dualAuthAddress: String,
    wallet: String,
    allOrNone: Boolean,
    feeToken: String,
    feeAmount: BigInt,
    tokenReceipt: String,
    walletSplitPercentage: Int,
    sig: String,
    dualAuthSig: String,
    // option
    validUntil: Long = 0,
    broker: String = "0x0",
    orderInterceptor: String = "0x0",
    version: String = "0",
    feePercentage: Int = 0,
    tokenSFeePercentage: Int = 0,
    tokenBFeePercentage: Int = 0,
    waiveFeePercentage: Int = 0,
    tokenSpendableS: BigInt = 0,
    tokenSpendableFee: BigInt = 0,
    brokerSpendableS: BigInt = 0,
    brokerSpendableFee: BigInt = 0,
) {
  def hash:String = {
    val data = ByteStream()
    data.addUint(amountS)
    data.addUint(amountB)
    data.addUint(feeAmount)
    data.addUint(BigInt(validSince))
    data.addUint(BigInt(validUntil))
    data.addAddress(owner)
    data.addAddress(tokenS)
    data.addAddress(tokenB)
    data.addAddress(dualAuthAddress)
    data.addAddress(broker)
    data.addAddress(orderInterceptor)
    data.addAddress(wallet)
    data.addAddress(tokenReceipt)
    data.addAddress(feeToken)
    data.addUint16(walletSplitPercentage)
    data.addUint16(feePercentage)
    data.addUint16(tokenSFeePercentage)
    data.addUint16(tokenBFeePercentage)
    data.addBoolean(allOrNone)

    Numeric.toHexString(web3Hash.sha3(data.getBytes))
  }

}
