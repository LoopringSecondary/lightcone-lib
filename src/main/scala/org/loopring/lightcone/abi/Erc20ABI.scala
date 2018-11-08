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

import org.loopring.lightcone.lib.solidity.{Abi ⇒ SABI}

import scala.collection.mutable.{ HashMap ⇒ MMap}

class Erc20ABI(contractAddress: String, abiJsonStr: String) extends ABI {

  val abi:SABI = SABI.fromJson(abiJsonStr)
  val supportedMap = MMap.empty[BigInt, String]

  override def isFunctionSupported(input: String): Boolean = {

  }
}
