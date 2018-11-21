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

import org.web3j.crypto.{ Credentials, RawTransaction, Sign, TransactionEncoder }
import org.web3j.utils.Numeric

object SignAlgorithm extends Enumeration {
  type AlgorithmType = Value
  val ALGORITHM_ETHEREUM = Value(0)
  val ALGORITHM_EIP712 = Value(1)
}

class Signer(privateKey: String, chainId: Byte = 1.toByte) {

  val credentials = Credentials.create(privateKey)
  val keyPair = credentials.getEcKeyPair
  val address = credentials.getAddress

  def signHash(algorithm: SignAlgorithm.Value, hash: String): String = {
    val data = Numeric.hexStringToByteArray(hash)

    val signatureData = algorithm match {
      case SignAlgorithm.ALGORITHM_ETHEREUM ⇒ Sign.signPrefixedMessage(data, keyPair)
      case SignAlgorithm.ALGORITHM_EIP712 ⇒ Sign.signMessage(data, keyPair, false)
      case _ ⇒ throw new Exception("algorithm invalid")
    }

    val v = signatureData.getV
    val r = Numeric.toHexString(signatureData.getR)
    val s = Numeric.toHexString(signatureData.getS)

    val sig = ByteStream()
    sig.addUint8(algorithm.id)
    sig.addUint8(1 + 32 + 32)
    sig.addUint8(v)
    sig.addPadHex(r)
    sig.addPadHex(s)

    sig.getData
  }

  def signTx(rawTransaction: RawTransaction): Array[Byte] = {
    TransactionEncoder.signMessage(rawTransaction, chainId, credentials)
  }
}
