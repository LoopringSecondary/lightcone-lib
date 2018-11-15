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

import org.web3j.crypto.Hash
import org.web3j.utils.Numeric

case class EIP712TypeData(
    types: EIP712Types,
    primaryType: String,
    domain: EIP712Domain,
    message: EIP712Data
) {

  def signatureHash(): String = {
    val bytes = Numeric.hexStringToByteArray("1901") ++
      hashStruct("EIP712Domain", this.domain.toMap()) ++
      hashStruct(this.primaryType, this.message)

    val crypbs = Hash.sha3(bytes)
    Numeric.toHexString(crypbs)
  }

  def hashStruct(primaryType: String, data: EIP712Data): Array[Byte] = {
    Hash.sha3(encodeData(primaryType, data))
  }

  def encodeData(primaryType: String, data: EIP712Data): Array[Byte] = {
    var encodedTypes = Array[String]("bytes32")
    var encodedValues = hashType(primaryType)

    this.types(primaryType).foreach { field ⇒
      val encType = field.typ
      val encValue = data(field.name)

      if (this.types.contains(field.typ)) {
        encodedTypes :+= "bytes32"
        val value = encValue match {
          case d: EIP712Data ⇒ Hash.sha3(encodeData(field.typ, d))
          case _             ⇒ Array.emptyByteArray
        }
        encodedValues ++= value
      } else {
        val (primiviteEncType, primiviteEncValue) = primivite(encType, encValue)
        encodedTypes :+= primiviteEncType
        encodedValues ++= primiviteEncValue
      }
    }

    encodedValues
  }

  // 订单中包含字段address, bool, uint, uint16, int16
  private[lib] def primivite(encType: String, encValue: Any): (String, Array[Byte]) = {
    val ENCODE_LENGTH = 32

    encType match {
      case "address" ⇒ encValue match {
        case v: String ⇒ ("address", Numeric.toBytesPadded(v, ENCODE_LENGTH))
        case _         ⇒ throw new Exception("invalid address:encValue not match")
      }

      case "bool" ⇒ encValue match {
        case v: Boolean ⇒ ("uint256", Numeric.toBytesPadded(v, ENCODE_LENGTH))
        case _          ⇒ throw new Exception("invalid bool:encValue not match")
      }

      case "uint16" | "int16" | "uint" ⇒ encValue match {
        case v: BigInt ⇒ ("uint256", Numeric.toBytesPadded(v.bigInteger, ENCODE_LENGTH))
        case _         ⇒ throw new Exception("invalid uint:encValue not match")
      }
      //      case "bytes" | "string" ⇒ encValue match {
      //        case v: Array[Byte] ⇒ ("bytes32", Numeric.toBytesPadded(Hash.sha3(v), ENCODE_LENGTH))
      //        case v: String ⇒ ("bytes32", Numeric.toBytesPadded(Hash.sha3(v.getBytes()), ENCODE_LENGTH))
      //      }

      case _ ⇒ throw new Exception("order field type undefined")
    }
  }

  def hashType(primaryType: String): Array[Byte] = {
    Hash.sha3(encodeType(primaryType))
  }

  def encodeType(primaryType: String): Array[Byte] = {
    var result = ""
    var deps = dependencies(primaryType, Array.empty[String])
    deps = deps.filter(dep ⇒ !dep.equals(primaryType))
    deps = deps.sorted
    deps = Array[String](primaryType) ++ deps

    deps.foreach { dep ⇒
      if (this.types.contains(dep)) {
        throw new Exception(s"No type definition specified: $dep")
      }
      result += s"$dep(${this.types(dep).map(o ⇒ s"${o.typ} ${o.name},")})"
    }

    result.getBytes()
  }

  def dependencies(primaryType: String, found: Array[String]): Array[String] = {
    if (found.contains(primaryType) || !this.types.contains(primaryType)) return found

    var results = found :+ primaryType
    this.types(primaryType).foreach { field ⇒
      dependencies(field.typ, found).foreach { dep ⇒
        if (results.contains(dep)) {
          results :+= dep
        }
      }
    }
    results
  }
}
