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
    feePercentage: Int,
    tokenReceipt: String,
    walletSplitPercentage: Int,
    hash: String,
    sig: String,
    dualAuthSig: String,
    // option
    validUntil: Long = 0,
    broker: String = "0x0",
    orderInterceptor: String = "0x0",
    version: String = "0",
    tokenSFeePercentage: Int = 0,
    tokenBFeePercentage: Int = 0,
    waiveFeePercentage: Int = 0,
    tokenSpendableS: BigInt = 0,
    tokenSpendableFee: BigInt = 0,
    brokerSpendableS: BigInt = 0,
    brokerSpendableFee: BigInt = 0,
)
