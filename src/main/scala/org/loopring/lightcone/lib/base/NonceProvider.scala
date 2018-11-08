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

trait NonceProvider {
  def setNonce(address: String, nonce: BigInt)
  def getNonce(address: String): BigInt
  def getAndIncNonce(address: String): BigInt
}

class SimpleNonceProvider extends NonceProvider {
  var nonces = Map.empty[String, BigInt]

  def getNonce(address: String): BigInt = nonces.getOrElse(address, BigInt(0))
  def getAndIncNonce(address: String): BigInt = {
    val currentNonce = nonces.getOrElse(address, BigInt(0))
    nonces += address → (currentNonce + BigInt(1))
    currentNonce
  }
  def setNonce(address: String, nonce: BigInt): Unit = nonces += address → nonce
}
