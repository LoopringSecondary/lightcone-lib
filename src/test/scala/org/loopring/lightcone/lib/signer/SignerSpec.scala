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

class SignerSpec extends FlatSpec with Matchers {

  // curl http://127.0.0.1:8545/ -X POST --data '{"jsonrpc":"2.0","method":"eth_sign","params":["0x2c99c120bfafc5c748139f2202430afda9d92fcd", "a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1"],"id":1}'

  "ethereumAlgorithm" should "be able to verify signed data" in {
    info("sbt lib/'testOnly *SignerSpec -- -z ethereumAlgorithm'")

    val publicKey = "0x2c99c120bfafc5c748139f2202430afda9d92fcd"
    val privateKey = "0xd7d51bdb8b4072b92d5401eae5e76c327d6a7ab013a637579dc4803b19209ea3"
    val hash = "0xa1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1"
    val encode = "0x00411b4be4e9cd8f233e6a95816f540918ccf02462f9ada2c91e5f51fd260b02984a89741864d4fa5a6607e37104f9c11f5ab38fe94ac5bfbc4424c24ee9b9d50e0c35"

    val signer = new Signer(privateKey)
    signer.address should be(publicKey)

    val sig = signer.signHash(SignAlgorithm.ALGORITHM_ETHEREUM, hash)
    sig should be(encode)
  }

  "eipAlgorithm" should "be able to verify signed data" in {
    info("sbt lib/'testOnly *SignerSpec -- -z eipAlgorithm'")

    val publicKey = "0x5b88d580cef81e8c7a30b34f5ea7c79c301fe215"
    val privateKey = "0x0e42f327ee3cfa7ccfc084a0bb68d05eb627610303012a67afbf1ecd9b0d32fa"
    val hash = "0xa1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1a1"
    val encode = "0x01411b1baa95b372065d60efbb768c95e8c80625802e67c98059624ab3c2debdd4e0e04a5747a36d1482881575e1b0e45e103c53081c4f8b86b8c9bdfd1abf9352aa7d"

    val signer = new Signer(privateKey)
    signer.address should be(publicKey)

    val sig = signer.signHash(SignAlgorithm.ALGORITHM_EIP712, hash)
    sig should be(encode)
  }

}
