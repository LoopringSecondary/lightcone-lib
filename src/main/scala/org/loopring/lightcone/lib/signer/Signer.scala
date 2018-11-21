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

import org.web3j.crypto.Sign
import org.web3j.crypto.Credentials
import org.web3j.utils.Numeric

class Signer(privateKey: String) {

  val ALGORITHM_ETHEREUM = 0
  val ALGORITHM_EIP712 = 1

  val credentials = Credentials.create(privateKey)

  def getAddress: String = credentials.getAddress

  def sign(algorithm: Int, hash: String): String = {
    require(Seq(ALGORITHM_EIP712, ALGORITHM_ETHEREUM).contains(algorithm))

    val signatureData = Sign.signMessage(
      Numeric.hexStringToByteArray(hash),
      credentials.getEcKeyPair
    )

    val v = signatureData.getV
    val r = Numeric.toHexString(signatureData.getR)
    val s = Numeric.toHexString(signatureData.getS)

    val sig = ByteStream()
    sig.addUint8(algorithm)
    sig.addUint8(1 + 32 + 32)
    sig.addUint8(v)
    sig.addPadHex(r)
    sig.addPadHex(s)

    sig.getData
  }
}
