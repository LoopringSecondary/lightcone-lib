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

import scala.collection.mutable.{ HashMap ⇒ MMap }

case class EIP712Domain(
    name: String = "",
    version: String = "",
    chainId: BigInt = 0,
    verifyingContract: String = "",
    salt: Array[Byte] = Array.emptyByteArray
) {

  def toMap(): MMap[String, Any] = {

    val data = MMap.empty[String, Any]

    if (this.name.nonEmpty) {
      data += "name" → this.name
    }
    if (this.version.nonEmpty) {
      data += "version" → this.version
    }
    if (this.verifyingContract.nonEmpty) {
      data += "verifyingContract" → this.verifyingContract
    }
    if (this.salt.nonEmpty) {
      data += "salt" → this.salt
    }

    data
  }
}
