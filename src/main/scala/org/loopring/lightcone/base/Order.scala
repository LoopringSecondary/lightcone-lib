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
    owner: Address,
    tokenS: Address,
    tokenB: Address,
    amountS: Amount,
    amountB: Amount,
    validSince: TimeStamp,
    validUntil: TimeStamp = 0,
    dualAuthAddress: Address,
    wallet: Address,
    allOrNone: Boolean,
    feeToken: Address,
    feeAmount: Amount,
    feePercentage: Int,
    tokenReceipt: Address,
    walletSplitPercentage: Int,
    hash: Hash,
    sig: String,
    dualAuthSig: String,
    // option
    broker: Address = "0x0",
    orderInterceptor: Address = "0x0",
    version: String = "0",
    tokenSFeePercentage: Int = 0,
    tokenBFeePercentage: Int = 0,
    waiveFeePercentage: Int = 0,
        tokenSpendableS: Amount = 0,
        tokenSpendableFee: Amount = 0,
        brokerSpendableS: Amount = 0,
        brokerSpendableFee: Amount = 0,
)
