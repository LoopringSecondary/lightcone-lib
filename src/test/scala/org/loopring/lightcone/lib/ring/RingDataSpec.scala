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

class RingDataSpec extends FlatSpec with Matchers {

  val one = BigInt("1000000000000000000")

  "simpleTest1" should "serialize and deserialize" in {
    info("[sbt lib/'testOnly *RingDataSpec -- -z simpleTest1']")

    val originInput = "0x00000002000100030008000d00120000002b00300035003a0042004a00000001004b00000000005000000055006e00000000003a0000000000000000000a00000087003500300042003a004a00020002008c0000000000500000009100aa00000000003a000000000000000000140200010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000019f456a70dac98ff7ac7086c1fe96a9a7373e7e2f8faa8ebd2259530e1b549725f2054215e107d0b000000000000000000000000000000000000000000000000000000000000004300411c570c09c5bed869350c52473d86018caf3499685a7a65f66d241757d140ca56625b2fbb5e3adb46738f1b3241c4f775f96967acb5f072ced1fb15b23ac40bd255000caa3b83de27bcdd275d407b858b8e879584e209fb66977409a186f8ae0d25ffb6eb6e22d0d5b85d78fdbd8bf534e544ca28c9f5ab109d0efa8d81f80000000000000000000000000000000000000000000000000de0b6b3a764000000000000000000000000000000000000000000000000003635c9adc5dea000005bea79fdb019e8aebcfefb8a5876694d7cd98639b9b2b1e3b596d1ece18ab450b3ead9b6a207f6382f47d4bf000000000000000000000000000000000000000000000000000000000000004300411b9c2252f2f293925fecc71b80a828854bb246ed6605040263454e3101dd01ed074e0c2bf0215d1d8a5477f17e7239d7ae1d96501e31db03d216f9c768fa4221d400000000000000000000000000000000000000000000000000000000000000004300411cd5b690d31ba9e985b7145874b0d651c074abb0b32e9dc2b19a4126766d3557b5386d03b511bd98d219740c98fce47b971fc1cd583365866db661e4199528bbe80081bc531303f8fd214160545b82f447c784c724f52357e1f239f78e6e817052b480ea25fd17747c3d000000000000000000000000000000000000000000000000000000000000004300411c3310e6bcfdd40e1b9f674ce31123d007cb5a05884d17c0f17a9ac1596431ce5c776c419d0650b73475309baeccb5c281c4f7958dd22c666b14c0eeeca443f24e00000000000000000000000000000000000000000000000000000000000000004300411c35fa11406751a71189cddf0c588096dc34e283d194985f38faa19ca8852cae645a2742772e33a8c0b6b3c0c69f3a3ac70557c0c0e534750b3f89bd3885d338dd00"
    val originOrderhash1 = "0x25756293052c4c755ab44826560878e704da886371dfa8d687967204bf992027"
    val originOrderhash2 = "0x2107a01f458033a8cbcb8ae6ba5e186e3b204a39628efb965447be49c7ee5c03"

    val originRinghash = "0x9edf234e367a96c9746880997cd12017f70f5754bbd8981ea7b466235720a13b"
    val originRinghashData = "0x25756293052c4c755ab44826560878e704da886371dfa8d687967204bf99202700002107a01f458033a8cbcb8ae6ba5e186e3b204a39628efb965447be49c7ee5c030000"
    val afterOriginRinghash = "0xa70ab0fc363ab839f07e6ce67ea014ad14350e2334eb34dd568d7cc0b5f3ec18"

    val token1 = "0xfb66977409a186f8ae0d25ffb6eb6e22d0d5b85d"
    val token2 = "0x78fdbd8bf534e544ca28c9f5ab109d0efa8d81f8"
    val feetoken = token2
    val wallet = "0xb596d1ece18ab450b3ead9b6a207f6382f47d4bf"
    val miner = "0xf8faa8ebd2259530e1b549725f2054215e107d0b"
    val feeReceipt = "0x19f456a70dac98ff7ac7086c1fe96a9a7373e7e2"
    val transactionOrigin = "0x3b111629a7949d63455d0e4a377ee64f6458329d"

    val generator: RingSerializer = new RingSerializerImpl(feetoken)
    val deserializer: RingDeserializer = new RingDeserializerImpl(feetoken)

    val raworder1 = Order(
      tokenS = token1,
      tokenB = token2,
      amountS = BigInt("1000000000000000000"),
      amountB = BigInt(1000) * one,
      owner = "0x0caa3b83de27bcdd275d407b858b8e879584e209",
      feeAmount = BigInt("1000000000000000000"),
      dualAuthAddress = "0xb019e8aebcfefb8a5876694d7cd98639b9b2b1e3",
      allOrNone = false,
      validSince = 1542093309,
      wallet = wallet,
      walletSplitPercentage = 10,
      tokenReceipt = "0x0caa3b83de27bcdd275d407b858b8e879584e209",
      feeToken = feetoken,
      tokenSpendableFee = 1,
      sig = "0x00411b9c2252f2f293925fecc71b80a828854bb246ed6605040263454e3101dd01ed074e0c2bf0215d1d8a5477f17e7239d7ae1d96501e31db03d216f9c768fa4221d4",
      dualAuthSig = "0x00411cd5b690d31ba9e985b7145874b0d651c074abb0b32e9dc2b19a4126766d3557b5386d03b511bd98d219740c98fce47b971fc1cd583365866db661e4199528bbe8"
    )
    val order1 = raworder1.copy(hash = raworder1.generateHash)

    val raworder2 = Order(
      tokenS = token2,
      tokenB = token1,
      amountS = BigInt(1000) * one,
      amountB = BigInt("1000000000000000000"),
      owner = "0x81bc531303f8fd214160545b82f447c784c724f5",
      feeAmount = BigInt("1000000000000000000"),
      dualAuthAddress = "0x2357e1f239f78e6e817052b480ea25fd17747c3d",
      allOrNone = false,
      validSince = 1542093309,
      wallet = wallet,
      walletSplitPercentage = 20,
      tokenReceipt = "0x81bc531303f8fd214160545b82f447c784c724f5",
      feeToken = feetoken,
      sig = "0x00411c3310e6bcfdd40e1b9f674ce31123d007cb5a05884d17c0f17a9ac1596431ce5c776c419d0650b73475309baeccb5c281c4f7958dd22c666b14c0eeeca443f24e",
      dualAuthSig = "0x00411c35fa11406751a71189cddf0c588096dc34e283d194985f38faa19ca8852cae645a2742772e33a8c0b6b3c0c69f3a3ac70557c0c0e534750b3f89bd3885d338dd",
      tokenSpendableS = 2,
      tokenSpendableFee = 2
    )
    val order2 = raworder2.copy(hash = raworder2.generateHash)

    val ring = Ring(
      orders = Seq(order1, order2),
      miner = miner,
      feeReceipt = feeReceipt,
      transactionOrigin = transactionOrigin,
      sig = "0x00411c570c09c5bed869350c52473d86018caf3499685a7a65f66d241757d140ca56625b2fbb5e3adb46738f1b3241c4f775f96967acb5f072ced1fb15b23ac40bd255",
      ringOrderIndex = Seq(Seq(0, 1))
    )

    val serializeRes = generator.serialize(ring)
    info("generated ring data: " + serializeRes)
    order1.hash should be(originOrderhash1)
    order2.hash should be(originOrderhash2)
    info("my ringhash:" + ring.hash)
    //ring.hash should be(originRinghash)
    serializeRes should be(originInput)

    val deserializeRes = deserializer.deserialize(originInput)
    deserializeRes.orders.size should be(2)
    compare(deserializeRes.orders.head, order1) should be(true)
    compare(deserializeRes.orders.last, order2) should be(true)
  }

  "simpleTest2" should "serialize and deserialize" in {
    info("[sbt lib/'testOnly *RingDataSpec -- -z simpleTest2']")

    val feetoken = "0x3b243b0e87228aa330a56e0af3f2733f9c780b44"
    val generator: RingSerializer = new RingSerializerImpl(feetoken)
    val deserializer: RingDeserializer = new RingDeserializerImpl(feetoken)

    val originInput = "0x00000002000100040008000d00120000002b00300035003a0042004a00000001004b00000000005000000055006e00000000003a0000000000000000000a00000087003500300042003a008c00020003008d0000000000500000009200ab00000000003a0000000000000000001402000100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000fff0e52e473c384a57e32b9394fba174b4849756cd48d3d60bd4f0c2c5d9188f6e4f4d5d6f0b0d39000000000000000000000000000000000000000000000000000000000000004300411c6de0e2b3f39640d1d9d334c37d99d1caaaaacdb1f3748bef9abcf6a3f60186b26392784ed8bb7041e6ca0eb0faefdc99cd00925033c05d2223758ae82c1b039500e5f9d599e79acc52b2a2a5fa2e986525482528310d0e13874d2efe785583a6d8a0675f8e802d9129186fc679eefba90c7e37805debaffe9b6c2d0d7c0000000000000000000000000000000000000000000000000de0b6b3a76400000000000000000000000000000000000000000000000000a2a15d09519be000005be93f35b345f56dd00e11a9e06d36dff4921c5e4e4cbddab5eb3fd0a6011cb1a47ddbde913744d544597d3e000000000000000000000000000000000000000000000000000000000000004300411c106e9a8fbd5e6c3d28439667267bef0298e1baf199f01edbdaa0907f61dd7ba42ac8d9a3087ec762e83eb4f6e7163c41faa7fabeae5e12a1e26764785e4f55cd00000000000000000000000000000000000000000000000000000000000000004300411b210773ff4ba461287bcc618545ebfce5e42cb3a5a6d6bcda3287387ceeb574950b85d0bfd4ca1b10ac7c93da1ba221108e081b4fb994baaaf5ce275ddaba0cb700b0b12b5ded3a6ae85f234bb60726f00b1ecd2ec15be93f38a876ab5c19ff8c8065b1114cf7dfe9d09686afbf000000000000000000000000000000000000000000000000000000000000004300411ba54418d6f77e03c47461fdca3ec6cc5bbb65edc13d824f43015e2913b1fb84495b33d1071577795e3514ddd5e298abb1c2f1e6562c0c05587edd1063b26eb36e00000000000000000000000000000000000000000000000000000000000000004300411bbe5a02bef3248f86f78c7577fc6b5afb87d3d5e87b57a1e4e582c6ef347bc9ac1c50f9b76d83c4a19f1ead95a982d1ff914d5eaab67554d2df795e7ece86ca0700"

    val raworder1 = Order(
      tokenS = "0x0d0e13874d2efe785583a6d8a0675f8e802d9129",
      tokenB = "0x186fc679eefba90c7e37805debaffe9b6c2d0d7c",
      amountS = BigInt("1000000000000000000"),
      amountB = BigInt(1000) * one * 3,
      owner = "0xe5f9d599e79acc52b2a2a5fa2e98652548252831",
      feeAmount = BigInt("1000000000000000000"),
      dualAuthAddress = "0xb345f56dd00e11a9e06d36dff4921c5e4e4cbdda",
      allOrNone = false,
      validSince = 1542012725,
      wallet = "0xb5eb3fd0a6011cb1a47ddbde913744d544597d3e",
      walletSplitPercentage = 10,
      tokenReceipt = "0xe5f9d599e79acc52b2a2a5fa2e98652548252831",
      feeToken = "0x3b243b0e87228aa330a56e0af3f2733f9c780b44",
      tokenSpendableFee = 1,
      sig = "0x00411c106e9a8fbd5e6c3d28439667267bef0298e1baf199f01edbdaa0907f61dd7ba42ac8d9a3087ec762e83eb4f6e7163c41faa7fabeae5e12a1e26764785e4f55cd",
      dualAuthSig = "0x00411b210773ff4ba461287bcc618545ebfce5e42cb3a5a6d6bcda3287387ceeb574950b85d0bfd4ca1b10ac7c93da1ba221108e081b4fb994baaaf5ce275ddaba0cb7"
    )
    val order1 = raworder1.copy(hash = raworder1.generateHash)

    val raworder2 = Order(
      tokenS = "0x186fc679eefba90c7e37805debaffe9b6c2d0d7c",
      tokenB = "0x0d0e13874d2efe785583a6d8a0675f8e802d9129",
      amountS = BigInt(1000) * one * 3,
      amountB = BigInt("1000000000000000000"),
      owner = "0xb0b12b5ded3a6ae85f234bb60726f00b1ecd2ec1",
      feeAmount = BigInt("1000000000000000000"),
      dualAuthAddress = "0xa876ab5c19ff8c8065b1114cf7dfe9d09686afbf",
      allOrNone = false,
      validSince = 1542012728,
      wallet = "0xb5eb3fd0a6011cb1a47ddbde913744d544597d3e",
      walletSplitPercentage = 20,
      tokenReceipt = "0xb0b12b5ded3a6ae85f234bb60726f00b1ecd2ec1",
      feeToken = "0x3b243b0e87228aa330a56e0af3f2733f9c780b44",
      sig = "0x00411ba54418d6f77e03c47461fdca3ec6cc5bbb65edc13d824f43015e2913b1fb84495b33d1071577795e3514ddd5e298abb1c2f1e6562c0c05587edd1063b26eb36e",
      dualAuthSig = "0x00411bbe5a02bef3248f86f78c7577fc6b5afb87d3d5e87b57a1e4e582c6ef347bc9ac1c50f9b76d83c4a19f1ead95a982d1ff914d5eaab67554d2df795e7ece86ca07",
      tokenSpendableS = 2,
      tokenSpendableFee = 3
    )
    val order2 = raworder2.copy(hash = raworder2.generateHash)

    val ring = Ring(
      orders = Seq(order1, order2),
      miner = "0xcd48d3d60bd4f0c2c5d9188f6e4f4d5d6f0b0d39",
      feeReceipt = "0xfff0e52e473c384a57e32b9394fba174b4849756",
      transactionOrigin = "0x77ddd79b1c8345809b5b7f25cd0058d211471eb0",
      sig = "0x00411c6de0e2b3f39640d1d9d334c37d99d1caaaaacdb1f3748bef9abcf6a3f60186b26392784ed8bb7041e6ca0eb0faefdc99cd00925033c05d2223758ae82c1b0395",
      ringOrderIndex = Seq(Seq(0, 1))
    )

    info("ringHash:" + ring.hash)

    order1.hash should be("0x93b65781da000e15a72736bde8e7288956a68b89868a400f72f17d6aca99924d")
    order2.hash should be("0x71ddf04504887e77e7fc02e23c671305cac94926cb230285d32b5bd5aae78850")

    val serializeRes = generator.serialize(ring)
    info("generated ring data: " + serializeRes)
    serializeRes should be(originInput)

    val deserializeRes = deserializer.deserialize(originInput)
    deserializeRes.orders.size should be(2)
    compare(deserializeRes.orders.head, order1) should be(true)
    compare(deserializeRes.orders.last, order2) should be(true)
  }

  private def compare(src: Order, dst: Order): Boolean = {
    (src.owner safeeq dst.owner) &&
      (src.tokenS safeeq dst.tokenS) &&
      (src.tokenB safeeq dst.tokenB) &&
      (src.amountS == dst.amountS) &&
      (src.amountB == dst.amountB) &&
      (src.feeAmount == dst.feeAmount) &&
      (src.feeToken safeeq dst.feeToken) &&
      (src.dualAuthAddress safeeq dst.dualAuthAddress) &&
      (src.allOrNone == dst.allOrNone) &&
      (src.validSince == dst.validSince) &&
      (src.validUntil == dst.validUntil) &&
      (src.wallet safeeq dst.wallet) &&
      (src.walletSplitPercentage == dst.walletSplitPercentage) &&
      (src.tokenReceipt safeeq dst.tokenReceipt) &&
      (src.sig == dst.sig) &&
      (src.dualAuthSig == dst.dualAuthSig)
  }
}
