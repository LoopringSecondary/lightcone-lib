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

case class ByteStream() {

  var data: String = ""

  def getData: String = {
    if (data.length.equals(0)) {
      "0x0"
    } else {
      "0x" + data
    }
  }

  def getBytes: Array[Byte] = Numeric.hexStringToByteArray(data)

  /////////////////////////
  // functions for pack
  //
  /////////////////////////
  def addAddress(x: String, numBytes: Int = 20, forceAppend: Boolean = false): Int =
    insert(Numeric.toHexStringNoPrefixZeroPadded(Numeric.toBigInt(x), numBytes * 2), forceAppend)

  def addUint8(num: BigInt, numBytes: Int = 1, forceAppend: Boolean = true): Int = addBigInt(num, numBytes, forceAppend)

  def addUint16(num: BigInt, numBytes: Int = 2, forceAppend: Boolean = true): Int = addBigInt(num, numBytes, forceAppend)

  def addUint32(num: BigInt, numBytes: Int = 4, forceAppend: Boolean = true): Int = addBigInt(num, numBytes, forceAppend)

  def addUint(num: BigInt, numBytes: Int = 32, forceAppend: Boolean = true): Int = addBigInt(num, numBytes, forceAppend)

  def addX(num: BigInt, numBytes: Int, forceAppend: Boolean = true): Int = addBigInt(num, numBytes, forceAppend)

  def addBoolean(b: Boolean, numBytes: Int = 1, forceAppend: Boolean = true): Int =
    addBigInt(if (b) 1 else 0, numBytes, forceAppend)

  def addHex(str: String, forceAppend: Boolean = true): Int =
    insert(Numeric.cleanHexPrefix(str), forceAppend)

  def addPadHex(str: String, numBytes: Int = 32, forceAppend: Boolean = true): Int = {
    insert(Numeric.toHexStringNoPrefixZeroPadded(hexString2BigInt(str).bigInteger, numBytes * 2), forceAppend)
  }

  def addRawBytes(str: String, forceAppend: Boolean = true): Int =
    insert(Numeric.cleanHexPrefix(str), forceAppend)

  // todo: fuk 负数问题
  private def addBigInt(num: BigInt, numBytes: Int, forceAppend: Boolean = true): Int =
    insert(Numeric.toHexStringNoPrefixZeroPadded(num.bigInteger, numBytes * 2), forceAppend)

  private def insert(x: String, forceAppend: Boolean): Int = {
    var offset = length()

    if (!forceAppend) {
      // Check if the data we're inserting is already available in the bitstream.
      // If so, return the offset to the location.
      var start = 0
      while (start != -1) {
        start = data.indexOf(x, start)
        if (start != -1) {
          if ((start % 2) == 0) {
            offset = start / 2
            return offset
          } else {
            start += 1
          }
        }
      }
    }

    data ++= x
    offset
  }

  def length(): Int = data.length / 2

}
