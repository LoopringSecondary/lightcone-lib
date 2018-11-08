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
import org.loopring.lightcone.lib.solidity.{ Abi ⇒ SABI }

import scala.collection.mutable.{ HashMap ⇒ MMap }

abstract class ABI(abiJson: String) {

  val abi: SABI = SABI.fromJson(abiJson)
  val functionLength = 8

  def getTransactionHeader(txInput: String): BigInt = {
    Numeric.decodeQuantity(txInput)
  }

  val supportedFunctions: MMap[String, SABI.Function] = MMap.empty[String, SABI.Function]
  val supportedEventLogs: MMap[String, SABI.Event] = MMap.empty[String, SABI.Event]

  abi.toArray().foreach {
    _ match {
      case x: SABI.Function ⇒
        val sig = x.encodeSignature()
        val key = Numeric.toHexStringWithPrefixZeroPadded(sig.bigInteger, 8)
        supportedFunctions += key.toLowerCase → x
      case x: SABI.Event ⇒
        val sig = x.encodeSignature()
        val key = Numeric.toHexString(sig)
        supportedEventLogs += key.toLowerCase -> x
      case _ ⇒
    }
  }

  def getFunction(input: String): Option[SABI.Function] = {
    val sig = Numeric.decodeQuantity(input)
    val key = Numeric.toHexStringWithPrefixZeroPadded(sig, 8).toLowerCase
    supportedFunctions.get(key)
  }

  def getEvent(firstTopic: String): Option[SABI.Event] = {
    val key = firstTopic.toLowerCase
    supportedEventLogs.get(key)
  }

  // def decode[I,R](d: I): R
}
