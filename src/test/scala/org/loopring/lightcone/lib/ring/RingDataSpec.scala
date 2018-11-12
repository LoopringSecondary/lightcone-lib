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

import java.math.BigInteger

import org.scalatest._
import org.web3j.utils.Numeric

class RingDataSpec extends FlatSpec with Matchers {

  val originInput = "0x00000002000100030008000d00120000002b00300035003a0042004a00000001004b00000000005000000055006e00000000003a0000000000000000000a00000087003500300042003a008c00020002008d0000000000500000009200ab00000000003a00000000000000000014020001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000009516587fc660af188facc5f064171122eb280201688abcf7c3b4448a39b721f28d80228557ffd735000000000000000000000000000000000000000000000000000000000000004300411c80c7a80b99295ea1b62fb140e998566c981a4ca47caaaed17eafa063d43e256048d4e237bc10699e6b7c6915f1284b92aef75e8776aaed141e9878fe431eebb70095e962fde99604eafcf7217e93ab85e4d7e340a7b28ef8f8e54b2a3fe4aafddebe149ba3873b8eb23fd8c70470f17cff079b27c90ee4c5e8d0a044a30000000000000000000000000000000000000000000000000de0b6b3a764000000000000000000000000000000000000000000000000003635c9adc5dea000005be91dcde5aebe40dafc3fd95690432084150d25ce4d044a772005d5533b0affa85e49e22d49a924abc903ba000000000000000000000000000000000000000000000000000000000000004300411be87cc96ecec7a8fdaf2cd988b63c9dd999a1dab16cc67d75ca436ee30b53d1d56bcd5328af64483e8b3a088924215306160752ab2f539508317fab87ef05896700000000000000000000000000000000000000000000000000000000000000004300411c474ae1a49e0eae1f52c8ee5a1b66c831dca5d520d2127870435f983e066680df5056cb099c367ac4491308aca351d305cd737414e57e2190affecc234cdf338000870656bcb79d0b610d1625add6fa7027bb7dfc555be91dce3ebfb9dcb169ecd65606d9d5e4251effedee1a1d000000000000000000000000000000000000000000000000000000000000004300411b9e3eb014cc60902d9acc0fd59453d3ff56334c57b6679c5f9c3564927c1340484008e2620955a1214cdf723b72e806aca75bc0f31c2d71edfe15dca3477c59a200000000000000000000000000000000000000000000000000000000000000004300411ba4da12ba7cc08372493ace8dde927f41b96251f46cdc70820ac938a8c978a5b60eea1b8dcae78524c99adc4219745acc3b224da67032de4e324b79224ad15c8f00"
                    "0x00000002000100030008000d00120000002b00300035003a0042004a00000001004b00000000005000000055006e00000000003a0000000000000000000a00000087003500300042003a004a00020002008c0000000000500000009100aa00000000003a00000000000000000014020001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000009516587fc660af188facc5f064171122eb280201688abcf7c3b4448a39b721f28d80228557ffd735000000000000000000000000000000000000000000000000000000000000004300411c80c7a80b99295ea1b62fb140e998566c981a4ca47caaaed17eafa063d43e256048d4e237bc10699e6b7c6915f1284b92aef75e8776aaed141e9878fe431eebb700e5f9d599e79acc52b2a2a5fa2e98652548252831e1b15b9937ae4da7fc50bce23aac3a1f94fe1c02c2bbb6ff7987c2a0a924b9d6eea90a580ba2afe90000000000000000000000000000000000000000000000000de0b6b3a764000000000000000000000000000000000000000000000000003635c9adc5dea000005be930bbb345f56dd00e11a9e06d36dff4921c5e4e4cbddab5eb3fd0a6011cb1a47ddbde913744d544597d3e000000000000000000000000000000000000000000000000000000000000004300411cee4b959e52a8645f020a92cf38d9c0e1a01e983bb0c39e8e546cf802fef758c4325540955456d7bc55c19e766a6fda8b5ef676ca97dd7894e40921d16035a9fa00000000000000000000000000000000000000000000000000000000000000004300411c74035c566372885663e00b0d6d4be1e8e2ae85f2a802d231b53f3564619bc28f724452f333a75462a7a63238f937f52351f2ef705c25731595c5a80317da826400b0b12b5ded3a6ae85f234bb60726f00b1ecd2ec1a876ab5c19ff8c8065b1114cf7dfe9d09686afbf000000000000000000000000000000000000000000000000000000000000004300411c31d6c83a4c4a164654bf67e6dfa104c3535c032dbd5a2de899cd00725fd5a1e33768bf367d46879900511ce42b543b72bfddd8ab94e3a51be80172ab31d125f800000000000000000000000000000000000000000000000000000000000000004300411c2456c27cfc3b72bb63cea499e53ea92a4fdf20689633716b6760e3c74259ccc064eda06ea545a3a1a5f0432c82616c033f7961b82672424ee315a22ea866e24d00"
  val one = BigInt("1000000000000000000")
  val lrcAddress = "0x50f3d353a2489f921569b78f56a5bffc488f4158"
  val generator: RingSerializer = new RingSerializerImpl(lrcAddress)
  val deserializer: RingDeserializer = new RingDeserializerImpl(lrcAddress)

  info("origin ring data:" + originInput)

  def getHash(essential: Order): String = {
    import org.web3j.crypto.Hash

    implicit class RichByteArray(bytes: Array[Byte]) {
      def asBigInteger(): BigInteger = Numeric.toBigInt(bytes)

      def addAddress(address: String): Array[Byte] = bytes ++ Numeric.toBytesPadded(Numeric.toBigInt(address), 20)

      def addUint256(num: BigInteger): Array[Byte] = bytes ++ Numeric.toBytesPadded(num, 32)

      def addUint16(num: BigInteger): Array[Byte] = bytes ++ Numeric.toBytesPadded(num, 2)

      def addBoolean(b: Boolean): Array[Byte] = bytes :+ (if (b) 1 else 0).toByte

      def addRawBytes(otherBytes: Array[Byte]): Array[Byte] = bytes ++ otherBytes

      def addHex(hex: String): Array[Byte] = bytes ++ Numeric.hexStringToByteArray(hex)

    }

    val data = Array[Byte]()
      .addUint256(essential.amountS.bigInteger)
      .addUint256(essential.amountB.bigInteger)
      .addUint256(essential.feeAmount.bigInteger)
      .addUint256(BigInt(essential.validSince).bigInteger)
      .addUint256(BigInt(essential.validUntil).bigInteger)
      .addAddress(essential.owner)
      .addAddress(essential.tokenS)
      .addAddress(essential.tokenB)
      .addAddress(essential.dualAuthAddress)
      .addAddress(essential.broker)
      .addAddress(essential.orderInterceptor)
      .addAddress(essential.wallet)
      .addAddress(essential.tokenReceipt)
      .addAddress(essential.feeToken)
      .addUint16(BigInt(essential.walletSplitPercentage).bigInteger)
      .addUint16(BigInt(essential.tokenSFeePercentage).bigInteger)
      .addUint16(BigInt(essential.tokenBFeePercentage).bigInteger)
      .addBoolean(essential.allOrNone)

    Numeric.toHexString(Hash.sha3(data))
  }

  /*
  * { tokenS: '0x0d0e13874d2efe785583a6d8a0675f8e802d9129',
  tokenB: '0x3b243b0e87228aa330a56e0af3f2733f9c780b44',
  amountS: 1000000000000000000,
  amountB: 1e+21,
  balanceS: 1e+26,
  balanceFee: 1e+26,
  balanceB: 1e+26,
  owner: '0xe5f9d599e79acc52b2a2a5fa2e98652548252831',
  feeAmount: 1000000000000000000,
  dualAuthSignAlgorithm: 0,
  dualAuthAddr: '0xb345f56dd00e11a9e06d36dff4921c5e4e4cbdda',
  allOrNone: false,
  validSince: 1542012717,
  walletAddr: '0xb5eb3fd0a6011cb1a47ddbde913744d544597d3e',
  walletSplitPercentage: 10,
  version: 0,
  validUntil: 0,
  tokenRecipient: '0xe5f9d599e79acc52b2a2a5fa2e98652548252831',
  feeToken: '0x3b243b0e87228aa330a56e0af3f2733f9c780b44',
  waiveFeePercentage: 0,
  tokenSFeePercentage: 0,
  tokenBFeePercentage: 0,
  hash: <Buffer e9 e1 2c ae d8 75 fd 92 b4 6e fb c4 c4 13 d5 6a 79 67 64 ad fd 64 a6 2e e5 32 a5 85 86 76 b7 ef>,
  sig: '0x00411b0a1a07ea138912127cd7701af73b9480296a6101d5acd6bf672678316ab230c339203e93470b68c0e3cae398554344216184fdd9196072038049abf856640e7c',
  dualAuthSig: '0x00411cd1d289a88d3c0e59a3fdcad6588e9488ed42c3b129e7c2bb170cf02bc2a901c1768e0c08dd6f04dda3d92324a4e4d76a721acef529b996d2b9a44b4024cf61b7',
  tokenSpendableS: { index: 0 },
  tokenSpendableFee: { index: 1 } }
  * */
  val raworder1 = Order(
    tokenS = "0x0d0e13874d2efe785583a6d8a0675f8e802d9129",
    tokenB = "0x3b243b0e87228aa330a56e0af3f2733f9c780b44",
    amountS = BigInt("1000000000000000000"),
    amountB = BigInt(1000) * one,
    owner = "0xe5f9d599e79acc52b2a2a5fa2e98652548252831",
    feeAmount = BigInt("1000000000000000000"),
    dualAuthAddress = "0xb345f56dd00e11a9e06d36dff4921c5e4e4cbdda",
    allOrNone = false,
    validSince = 1542012717,
    wallet = "0xb5eb3fd0a6011cb1a47ddbde913744d544597d3e",
    walletSplitPercentage = 10,
    tokenReceipt = "0xe5f9d599e79acc52b2a2a5fa2e98652548252831",
    feeToken = "0x3b243b0e87228aa330a56e0af3f2733f9c780b44",
    tokenSpendableFee = 1,
    sig = "0x00411b0a1a07ea138912127cd7701af73b9480296a6101d5acd6bf672678316ab230c339203e93470b68c0e3cae398554344216184fdd9196072038049abf856640e7c",
    dualAuthSig = "0x00411cd1d289a88d3c0e59a3fdcad6588e9488ed42c3b129e7c2bb170cf02bc2a901c1768e0c08dd6f04dda3d92324a4e4d76a721acef529b996d2b9a44b4024cf61b7"
  )

  val orderhash = "0xe9e12caed875fd92b46efbc4c413d56a796764adfd64a62ee532a5858676b7ef"
  val generatedOrderhash = raworder1.generateHash
  generatedOrderhash should be(orderhash)

  //val order1 = raworder1.copy(hash = getHash(raworder1))

  /*
  { tokenS: '0x50f3d353a2489f921569b78f56a5bffc488f4158',
  tokenB: '0x7915d9c0ca68fbc0909c892033d2700a5ec46ba6',
  amountS: 1e+21,
  amountB: 1000000000000000000,
  balanceS: 1e+26,
  balanceFee: 1e+26,
  balanceB: 1e+26,
  owner: '0xb0b12b5ded3a6ae85f234bb60726f00b1ecd2ec1',
  feeAmount: 1000000000000000000,
  dualAuthSignAlgorithm: 0,
  dualAuthAddr: '0xa876ab5c19ff8c8065b1114cf7dfe9d09686afbf',
  allOrNone: false,
  validSince: 1542011930,
  walletAddr: '0xb5eb3fd0a6011cb1a47ddbde913744d544597d3e',
  walletSplitPercentage: 20,
  version: 0,
  validUntil: 0,
  tokenRecipient: '0xb0b12b5ded3a6ae85f234bb60726f00b1ecd2ec1',
  feeToken: '0x50f3d353a2489f921569b78f56a5bffc488f4158',
  waiveFeePercentage: 0,
  tokenSFeePercentage: 0,
  tokenBFeePercentage: 0,
  hash: <Buffer 7b 80 a0 8b 90 0a 36 32 90 46 4d 66 bb 3b ea 4d 13 3e 65 a6 ed 2d 06 27 5e 18 4b b2 e0 5a 6c a5>,
  sig: '0x00411c019939f4ba861ce2c3bb93ac6c8daabdb616ec85f5b4b888bd15a216a4864e945b87e7d7c32dd80f709718baf210235d72d267f3b8f82362ad654acae6177b5a',
  dualAuthSig: '0x00411cd1fda4ba34dcca8bf601f3a471f3754954b6f191929c3f6ecf00fa4ec19a458f574170a6eb3a8ec700d18095759c342f881c7a0242e18c285048f3a9c9b4960b',
  tokenSpendableS: { index: 2 },
  tokenSpendableFee: { index: 2 } }
  */
//  val raworder2 = Order(
//    tokenS = "0x50f3d353a2489f921569b78f56a5bffc488f4158",
//    tokenB = "0x7915d9c0ca68fbc0909c892033d2700a5ec46ba6",
//    amountS = BigInt(1000) * one,
//    amountB = BigInt("1000000000000000000"),
//    owner = "0xb0b12b5ded3a6ae85f234bb60726f00b1ecd2ec1",
//    feeAmount = BigInt("1000000000000000000"),
//    dualAuthAddress = "0xa876ab5c19ff8c8065b1114cf7dfe9d09686afbf",
//    allOrNone = false,
//    validSince = 1542009019,
//    wallet = "0xb5eb3fd0a6011cb1a47ddbde913744d544597d3e",
//    walletSplitPercentage = 20,
//    tokenReceipt = "0xb0b12b5ded3a6ae85f234bb60726f00b1ecd2ec1",
//    feeToken = "0x50f3d353a2489f921569b78f56a5bffc488f4158",
//    sig = "0x00411c019939f4ba861ce2c3bb93ac6c8daabdb616ec85f5b4b888bd15a216a4864e945b87e7d7c32dd80f709718baf210235d72d267f3b8f82362ad654acae6177b5a",
//    dualAuthSig = "0x00411cd1fda4ba34dcca8bf601f3a471f3754954b6f191929c3f6ecf00fa4ec19a458f574170a6eb3a8ec700d18095759c342f881c7a0242e18c285048f3a9c9b4960b",
//    tokenSpendableS = 2,
//    tokenSpendableFee = 2
//  )
//  val order2 = raworder2.copy(hash = getHash(raworder2))
//
//  "serialize" should "get ring data" in {
//    info("[sbt lib/'testOnly *RingDataSpec -- -z serialize']")
//
//    val ring = Ring(
//      orders = Seq(order1, order2),
//      miner = "0xcd48d3d60bd4f0c2c5d9188f6e4f4d5d6f0b0d39",
//      feeReceipt = "0xfff0e52e473c384a57e32b9394fba174b4849756",
//      transactionOrigin = "0x77ddd79b1c8345809b5b7f25cd0058d211471eb0",
//      sig = "0x00411c86b0880664b63f0e40bf8d657f491c6ad2fc5def10127485e0bb4be632a5a6141c21b249398b896a251df0bc9c8b11892231db1cbcaacc6dab2bba590f6ce357",
//      ringOrderIndex = Seq(Seq(0, 1))
//    )
//
//    val result = generator.serialize(ring)
//    info("generated ring data: " + result)
//    result should be(originInput)
//  }
//
//  "deserialize" should "parse ring to orders" in {
//    info("[sbt lib/'testOnly *RingDataSpec -- -z deserialize']")
//    // val ringhash = "0x6cacf9c57af230d0d1d75364196dc144f049b23138200586a7e8d7e467e9355c"
//    val result = deserializer.deserialize(originInput)
//
//    result.orders.size should be(2)
//    info(result.orders.head.toString)
//    info(result.orders.last.toString)
//    //compare(result.orders.head, order1) should be(true)
//    //compare(result.orders.last, order2) should be(true)
//  }
//
//  private def compare(src: Order, dst: Order): Boolean = {
//    (src.owner safeeq dst.owner) &&
//      (src.tokenS safeeq dst.tokenS) &&
//      (src.tokenB safeeq dst.tokenB) &&
//      (src.amountS == dst.amountS) &&
//      (src.amountB == dst.amountB) &&
//      (src.feeAmount == dst.feeAmount) &&
//      (src.feePercentage == dst.feePercentage) &&
//      (src.feeToken safeeq dst.feeToken) &&
//      (src.dualAuthAddress safeeq dst.dualAuthAddress) &&
//      (src.allOrNone == dst.allOrNone) &&
//      (src.validSince == dst.validSince) &&
//      (src.validUntil == dst.validUntil) &&
//      (src.wallet safeeq dst.wallet) &&
//      (src.walletSplitPercentage == dst.walletSplitPercentage) &&
//      (src.tokenReceipt safeeq dst.tokenReceipt) &&
//      (src.sig == dst.sig) &&
//      (src.dualAuthSig == dst.dualAuthSig)
//  }
}
