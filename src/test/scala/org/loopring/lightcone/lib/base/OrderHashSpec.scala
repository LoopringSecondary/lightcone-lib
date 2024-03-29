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

import org.scalatest._

class OrderHashSpec extends FlatSpec with Matchers {

  val one = BigInt("1000000000000000000")

  "generateOrderHash" should "generate Hash" in {
    info("[sbt lib/'testOnly *OrderHashSpec -- -z generateOrderHash']")

    val originOrderhash = "0x2696da234c3ee71f8dbf09ad102dc3d7c6b5b94e25e40de84d6c26c15b1a5984"

    val tokenS = "0x7ccf005d25eedddd91d80eee18eb7db7185ccf81"
    val tokenB = "0x5609f4b4d351fea84854d452aa06c615ec5f8510"
    val validSince = 1542351917
    val feeToken = tokenB
    val sig = "0x00411b2cf735a2ce7cbb5afe06547ea7f2a26f2d46aec8aed7462a5dd1d510fd91d6f96f1b0f06ea5552202d8bc22d89e881761b46377652249af569b6d636dafbb7c8"
    val dualAuthSig = "0x00411c38c053215ab48b3d1d099fa8170d0e1fe17678eb861d0c7dab3b39feacc02485068d20053c3c1455ffde6e112bd25ab1cd59d4209e8efddf4b5ec2183e155c3e"

    val order = Order(
      tokenS = tokenS,
      tokenB = tokenB,
      amountS = BigInt("1000000000000000000"),
      amountB = BigInt(1000) * one,
      owner = "0x6feaf3926489df7b21b49d591fd556f4fc912d7e",
      feeAmount = BigInt("1000000000000000000"),
      dualAuthAddress = "0x58df38c79f2d9e8ced060c5221a5ce4f034c6c31",
      allOrNone = false,
      validSince = validSince,
      wallet = "0xcdc65a2beb0df0ff92890170a2129d1078a9943e",
      walletSplitPercentage = 10,
      tokenReceipt = "0x6feaf3926489df7b21b49d591fd556f4fc912d7e",
      feeToken = feeToken,
      tokenSpendableFee = 1,
      sig = sig,
      dualAuthSig = dualAuthSig
    )
    val orderhash = order.generateHash
    info(orderhash)
    orderhash should be(originOrderhash)
  }

}
