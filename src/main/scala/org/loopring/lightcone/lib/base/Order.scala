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

  /*
  * // Precalculated EIP712_ORDER_SCHEMA_HASH amd EIP712_DOMAIN_HASH because
        // the solidity compiler doesn't correctly precalculate them for us.
        bytes32 _EIP712_ORDER_SCHEMA_HASH = 0x5632ff1bdfbe9ca7ecbcb1bd8c61f364e0debfed45fd8be4e459081586292fff;
        bytes32 _EIP712_DOMAIN_HASH = 0xaea25658c273c666156bd427f83a666135fcde6887a6c25fc1cd1562bc4f3f34;

        bytes32 hash;
        assembly {
            let ptr := mload(64)
            mstore(add(ptr,   0), _EIP712_ORDER_SCHEMA_HASH)     // EIP712_ORDER_SCHEMA_HASH
            mstore(add(ptr,  32), mload(add(order, 128)))        // order.amountS
            mstore(add(ptr,  64), mload(add(order, 160)))        // order.amountB
            mstore(add(ptr,  96), mload(add(order, 640)))        // order.feeAmount
            mstore(add(ptr, 128), mload(add(order, 192)))        // order.validSince
            mstore(add(ptr, 160), mload(add(order, 480)))        // order.validUntil
            mstore(add(ptr, 192), mload(add(order,  32)))        // order.owner
            mstore(add(ptr, 224), mload(add(order,  64)))        // order.tokenS
            mstore(add(ptr, 256), mload(add(order,  96)))        // order.tokenB
            mstore(add(ptr, 288), mload(add(order, 288)))        // order.dualAuthAddr
            mstore(add(ptr, 320), mload(add(order, 320)))        // order.broker
            mstore(add(ptr, 352), mload(add(order, 416)))        // order.orderInterceptor
            mstore(add(ptr, 384), mload(add(order, 448)))        // order.wallet
            mstore(add(ptr, 416), mload(add(order, 768)))        // order.tokenRecipient
            mstore(add(ptr, 448), mload(add(order, 608)))        // order.feeToken
            mstore(add(ptr, 480), mload(add(order, 800)))        // order.walletSplitPercentage
            mstore(add(ptr, 512), mload(add(order, 704)))        // order.tokenSFeePercentage
            mstore(add(ptr, 544), mload(add(order, 736)))        // order.tokenBFeePercentage
            mstore(add(ptr, 576), mload(add(order, 576)))        // order.allOrNone
            let message := keccak256(ptr, 608)                   // 19 * 32

            mstore(add(ptr,  0), 0x1901)                         // EIP191_HEADER
            mstore(add(ptr, 32), _EIP712_DOMAIN_HASH)            // EIP712_DOMAIN_HASH
            mstore(add(ptr, 64), message)                        // message
            hash := keccak256(add(ptr, 30), 66)                  // 2 + 32 + 32
  * */
  def generateHash: String = {
    val EIP712_HEADER = "0x1901"
    val EIP712_ORDER_SCHEMA_HASH = "0x5632ff1bdfbe9ca7ecbcb1bd8c61f364e0debfed45fd8be4e459081586292fff"
    val EIP712_DOMAIN_HASH = "0xaea25658c273c666156bd427f83a666135fcde6887a6c25fc1cd1562bc4f3f34"

    val data = ByteStream()
    data.addPadHex(EIP712_ORDER_SCHEMA_HASH)
    data.addUint(amountS)
    data.addUint(amountB)
    data.addUint(feeAmount)
    data.addUint(BigInt(validSince))
    data.addUint(BigInt(validUntil))
    data.addAddress(owner, 32, true)
    data.addAddress(tokenS, 32,true)
    data.addAddress(tokenB, 32,true)
    data.addAddress(dualAuthAddress, 32, true)
    data.addAddress(broker,32,true)
    data.addAddress(orderInterceptor, 32, true)
    data.addAddress(wallet, 32,true)
    data.addAddress(tokenReceipt, 32,true)
    data.addAddress(feeToken, 32,true)
    data.addUint16(walletSplitPercentage, 32)
    data.addUint16(tokenSFeePercentage, 32)
    data.addUint16(tokenBFeePercentage, 32)
    data.addBoolean(allOrNone, 32)

    val message = Numeric.toHexString(web3Hash.sha3(data.getBytes))

    val eip = ByteStream()
    eip.addHex(EIP712_HEADER)
    eip.addHex(EIP712_DOMAIN_HASH)
    eip.addHex(message)

    Numeric.toHexString(web3Hash.sha3(eip.getBytes))
  }
}
