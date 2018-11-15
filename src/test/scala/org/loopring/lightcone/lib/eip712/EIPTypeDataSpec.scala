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
import org.web3j.utils.Numeric

class EIPTypeDataSpec extends FlatSpec with Matchers {

  "addAddress" should "add address and return lenght of string" in {
    info("[sbt lib/'testOnly *EIPTypeDataSpec -- -z addAddress']")
    val num: BigInt = "0x8d8812b72d1e4ffCeC158D25f56748b7d67c1e78"
    val data = Numeric.toBytesPadded(num.bigInteger, 32)
    val str = Numeric.toHexString(data)
    info(str)
    info(data.length.toString)

    val t: Any = "0x8d8812b72d1e4ffCeC158D25f56748b7d67c1e78"
    t match {
      case x:String ⇒
      case _ ⇒
    }
  }

}
