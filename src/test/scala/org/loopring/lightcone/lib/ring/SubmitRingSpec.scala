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
import org.web3j.crypto.{ RawTransaction, Sign }
import org.web3j.utils.Numeric

class SubmitRingSpec extends FlatSpec with Matchers {

  val debug = true

  val chainId = BigInt(151).byteValue()
  val miner = "0x4bad3053d574cd54513babe21db3f09bea1d387d"
  val privateKey = "8e0f7f4f5a49ada14726b90412722055da6899a0a673e8350803429da97bc7d3"
  implicit val ringSigner: Signer = new Signer(privateKey, chainId)

  val lrcAddress = "0xcd36128815ebe0b44d0374649bad2721b8751bef"
  val wethAddress = "0xf079E0612E869197c5F4c7D0a95DF570B163232b"
  implicit val serializer: RingSerializer = new RingSerializerImpl(lrcAddress)

  val ringSubmitterAbiJsonStr = "[{\"constant\":false,\"inputs\":[{\"name\":\"data\",\"type\":\"bytes\"}],\"name\":\"submitRings\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"FEE_PERCENTAGE_BASE\",\"outputs\":[{\"name\":\"\",\"type\":\"uint16\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"_ringIndex\",\"type\":\"uint256\"},{\"indexed\":true,\"name\":\"_ringHash\",\"type\":\"bytes32\"},{\"indexed\":true,\"name\":\"_feeRecipient\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"_fills\",\"type\":\"bytes\"}],\"name\":\"RingMined\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"_ringHash\",\"type\":\"bytes32\"}],\"name\":\"InvalidRing\",\"type\":\"event\"}]"
  implicit val ringSubmitterAbi = new RingSubmitterABI(ringSubmitterAbiJsonStr)

  val methodId = Numeric.toHexString(ringSubmitterAbi.submitRing.encodeSignature())
  val one: BigInt = BigInt("1000000000000000000")

  val protocol = "0x20eb7225eaddf4d729c9c938663d6afef654f6d7"

  val account1 = "0x1b978a1d302335a6f2ebe4b8823b5e17c3c84135"
  val account1PrivateKey = "5b791c6c9f4b7aa95ccb58f0f939397d1dcd047a5c0231e77ca353ebfea306f3"
  val account2 = "0xb1018949b241d76a1ab2094f473e9befeabb5ead"
  val account2PrivateKey = "ba7c9144fe2351c208287f9204b7c5940b0732ac577b771587ea872c4f46da9e"

  val nonce = BigInt(5236)
  val validSince = 1541779200
  val validUntil = 1542621731
  // todo: ring generator broker为"0x0"时 合约debug为owner 合约bug？？？？？

  "simpleTest1" should "serialize and deserialize" in {
    info("[sbt lib/'testOnly *SubmitRingSpec -- -z simpleTest1']")

    val raworder1 = Order(
      tokenS = lrcAddress,
      tokenB = wethAddress,
      amountS = one * 100,
      amountB = one / 100,
      owner = account1,
      feeAmount = one * 10,
      dualAuthAddress = account1,
      allOrNone = false,
      validSince = validSince,
      validUntil = validUntil,
      wallet = miner,
      tokenReceipt = miner,
      feeToken = lrcAddress,
      sig = "0x",
      dualAuthSig = "0x"
    )
    val order1 = fullOrder(raworder1, account1PrivateKey)

    val raworder2 = Order(
      tokenS = wethAddress,
      tokenB = lrcAddress,
      amountS = one / 100,
      amountB = one * 100,
      owner = account2,
      feeAmount = one * 10,
      dualAuthAddress = account2,
      allOrNone = false,
      validSince = validSince,
      validUntil = validUntil,
      wallet = miner,
      tokenReceipt = miner,
      feeToken = lrcAddress,
      sig = "0x",
      dualAuthSig = "0x"
    )
    val order2 = fullOrder(raworder2, account2PrivateKey)

    val ring = Ring(
      orders = Seq(order1, order2),
      miner = miner,
      feeReceipt = miner,
      transactionOrigin = miner,
      sig = "0x",
      ringOrderIndex = Seq(Seq(0, 1))
    )
    val input = ring.getInputData()
    val tx = generateTxData(input, nonce)
    info(Numeric.toHexString(tx))
  }

  private def fullOrder(raworder: Order, privKey: String): Order = {
    val hash = raworder.generateHash
    val signer = new Signer(privKey, chainId)
    val sig = signer.signHash(SignAlgorithm.ALGORITHM_ETHEREUM, hash)
    raworder.copy(hash = hash, sig = sig, dualAuthSig = sig)
  }

  private def generateTxData(inputData: String, nonce: BigInt)(implicit signer: Signer): Array[Byte] = {
    val rawTransaction = RawTransaction.createTransaction(
      nonce.bigInteger,
      BigInt("18000000000").bigInteger,
      BigInt("6000000").bigInteger,
      protocol,
      BigInt(0).bigInteger,
      inputData
    )
    signer.signTx(rawTransaction)
  }
}
