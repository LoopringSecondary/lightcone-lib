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
    val originRinghash = "0xa70ab0fc363ab839f07e6ce67ea014ad14350e2334eb34dd568d7cc0b5f3ec18"

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
    ring.hash should be(originRinghash)
    serializeRes should be(originInput)

    val deserializeRes = deserializer.deserialize(originInput)
    deserializeRes.orders.size should be(2)
    compare(deserializeRes.orders.head, order1) should be(true)
    compare(deserializeRes.orders.last, order2) should be(true)
  }

  "simpleTest2" should "serialize and deserialize" in {
    info("[sbt lib/'testOnly *RingDataSpec -- -z simpleTest2']")

    val originInput = "0x00000002000100040008000d00120000002b00300035003a0042004a00000001004b00000000005000000055006e00000000003a0000000000000000000a00000087003500300042003a008c00020003008d0000000000500000009200ab00000000003a000000000000000000140200010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000019f456a70dac98ff7ac7086c1fe96a9a7373e7e2f8faa8ebd2259530e1b549725f2054215e107d0b000000000000000000000000000000000000000000000000000000000000004300411b49539f0ddae9317a8e8c2ba0bb69dbfe20dcbbef1755b5dde4ce7235059e5d9c4b84846090b8612a72c23c3a409e1a4302d3bb39e2f8cc1e86379c14bfbe987d000caa3b83de27bcdd275d407b858b8e879584e209fb66977409a186f8ae0d25ffb6eb6e22d0d5b85dec701c6f6dead34fa871786554f7a2e0a5f34e770000000000000000000000000000000000000000000000000de0b6b3a76400000000000000000000000000000000000000000000000000a2a15d09519be000005bea7a04b019e8aebcfefb8a5876694d7cd98639b9b2b1e3b596d1ece18ab450b3ead9b6a207f6382f47d4bf000000000000000000000000000000000000000000000000000000000000004300411ce4b9bfc2bb25e9201f6eed444cd5a20f93a37c6c1b440fa9fb0371814973ed18269d9fd20a268258aa0e98fa569c14028caebf2e19247a42b484e80a2e67da8000000000000000000000000000000000000000000000000000000000000000004300411cf7796149e980ba65f56ab6b3f66f86a30ef411e2cc4baad33e22a6e7131e33d2076f1f6f9ee340454556cee00f7fc71740d2d32e925e3f31f36d3d73f27fb41c0081bc531303f8fd214160545b82f447c784c724f55bea7a072357e1f239f78e6e817052b480ea25fd17747c3d000000000000000000000000000000000000000000000000000000000000004300411c8cbc303b64e8c1767381e7142a85d3511fa112a6050b4d4a5d53a121f00c29474395e2be4185f71db4b19d3593fc03802bac9d67f53f984939c418a2bb2928dc00000000000000000000000000000000000000000000000000000000000000004300411c32cbb9c564b66e58b9003937bcab6dbd5f6198f5b2ee8c2575d1e1614192161851bb81c925c7f0e5bb5131dd66fa13ef60cf3d69072d6f1fd39c9877dbdebb2200"
    val originOrderhash1 = "0x1bce01d913bc85526a3de4e0dc32c5162dc825611be840c4c1f5949c2c6819d8"
    val originOrderhash2 = "0xda532281849cf6ecbf39aa2b733291e960e58d73ea2d1deb0327c36fe0f5ebd3"
    val originRinghash = "0x898d9f0bf21d43e277af2736445eabb6f10a580644043564624f22be13e98d5a"

    val token1 = "0xfb66977409a186f8ae0d25ffb6eb6e22d0d5b85d"
    val token2 = "0xec701c6f6dead34fa871786554f7a2e0a5f34e77"
    val feetoken = "0x78fdbd8bf534e544ca28c9f5ab109d0efa8d81f8"
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
      amountB = BigInt(3000) * one,
      owner = "0x0caa3b83de27bcdd275d407b858b8e879584e209",
      feeAmount = BigInt("1000000000000000000"),
      dualAuthAddress = "0xb019e8aebcfefb8a5876694d7cd98639b9b2b1e3",
      allOrNone = false,
      validSince = 1542093316,
      wallet = wallet,
      walletSplitPercentage = 10,
      tokenReceipt = "0x0caa3b83de27bcdd275d407b858b8e879584e209",
      feeToken = feetoken,
      tokenSpendableFee = 1,
      sig = "0x00411ce4b9bfc2bb25e9201f6eed444cd5a20f93a37c6c1b440fa9fb0371814973ed18269d9fd20a268258aa0e98fa569c14028caebf2e19247a42b484e80a2e67da80",
      dualAuthSig = "0x00411cf7796149e980ba65f56ab6b3f66f86a30ef411e2cc4baad33e22a6e7131e33d2076f1f6f9ee340454556cee00f7fc71740d2d32e925e3f31f36d3d73f27fb41c"
    )
    val order1 = raworder1.copy(hash = raworder1.generateHash)

    val raworder2 = Order(
      tokenS = token2,
      tokenB = token1,
      amountS = BigInt(3000) * one,
      amountB = BigInt("1000000000000000000"),
      owner = "0x81bc531303f8fd214160545b82f447c784c724f5",
      feeAmount = BigInt("1000000000000000000"),
      dualAuthAddress = "0x2357e1f239f78e6e817052b480ea25fd17747c3d",
      allOrNone = false,
      validSince = 1542093319,
      wallet = wallet,
      walletSplitPercentage = 20,
      tokenReceipt = "0x81bc531303f8fd214160545b82f447c784c724f5",
      feeToken = feetoken,
      sig = "0x00411c8cbc303b64e8c1767381e7142a85d3511fa112a6050b4d4a5d53a121f00c29474395e2be4185f71db4b19d3593fc03802bac9d67f53f984939c418a2bb2928dc",
      dualAuthSig = "0x00411c32cbb9c564b66e58b9003937bcab6dbd5f6198f5b2ee8c2575d1e1614192161851bb81c925c7f0e5bb5131dd66fa13ef60cf3d69072d6f1fd39c9877dbdebb22",
      tokenSpendableS = 2,
      tokenSpendableFee = 2
    )
    val order2 = raworder2.copy(hash = raworder2.generateHash)

    val ring = Ring(
      orders = Seq(order1, order2),
      miner = miner,
      feeReceipt = feeReceipt,
      transactionOrigin = transactionOrigin,
      sig = "0x00411b49539f0ddae9317a8e8c2ba0bb69dbfe20dcbbef1755b5dde4ce7235059e5d9c4b84846090b8612a72c23c3a409e1a4302d3bb39e2f8cc1e86379c14bfbe987d",
      ringOrderIndex = Seq(Seq(0, 1))
    )

    val serializeRes = generator.serialize(ring)
    info("generated ring data: " + serializeRes)
    order1.hash should be(originOrderhash1)
    order2.hash should be(originOrderhash2)
    ring.hash should be(originRinghash)
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
