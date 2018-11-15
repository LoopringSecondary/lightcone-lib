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

import java.math.BigInteger

import org.web3j.utils.Numeric

import scala.collection.mutable.{ HashMap ⇒ MMap }

// todo lib里面是否需要定义amount,address,hash等数据结构,与core项目的amount等该如何调用

package object lib {

  case class EIP712TypeUnit(typ: String, name: String)

  type EIP712Type = Seq[EIP712TypeUnit]
  type EIP712Types = MMap[String, EIP712Type]
  type EIP712Data = MMap[String, Any]

  implicit def bytes2BigInt(bytes: Array[Byte]): BigInt = Numeric.toBigInt(bytes)
  implicit def hexString2BigInt(hex: String): BigInt = Numeric.toBigInt(hex)

  implicit def hex2BigInteger(hex: String): BigInteger = hexString2BigInt(hex).bigInteger
  implicit def bool2BigInteger(b: Boolean): BigInteger = BigInt(if (b) 1 else 0).bigInteger
  implicit def bytes2BigInteger(bytes: Array[Byte]): BigInteger = bytes2BigInt(bytes).bigInteger

  implicit class RichString(src: String) {

    def safeeq(that: String): Boolean = src.toLowerCase == that.toLowerCase

    def safeneq(that: String): Boolean = src.toLowerCase != that.toLowerCase

  }
}
