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

package org.loopring.lightcone

import org.web3j.utils.Numeric

package object lib {
  type Amount = BigInt
  type Address = String
  type Hash = String
  type TimeStamp = Long

  implicit class RichHex(src: String) {

    def asAmount: Amount = Numeric.toBigInt(src)
  }

  implicit class RichAddress(src: Address) {

    def safeeq(that: Address): Boolean = src.toLowerCase == that.toLowerCase
    def safeneq(that: Address): Boolean = src.toLowerCase != that.toLowerCase

  }

  implicit class RichAmount(src: Amount) {

    def moreThanZero: Boolean = src.compare(BigInt(0)) > 0
  }
}
