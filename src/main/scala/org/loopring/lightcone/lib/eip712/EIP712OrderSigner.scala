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

final class EIP712OrderSigner() {

  def getOrderHash(order: Order): String = {
    val typeData = this.toTypeData(order)
    typeData.signatureHash()
  }

  def toTypeData(order: Order): EIP712TypeData = {
    var types = new EIP712Types
    types += "EIP712Domain" → Seq(
      EIP712TypeUnit(name = "name", typ = "string"),
      EIP712TypeUnit(name = "version", typ = "string")
    )
    types += "Order" → Seq(
      EIP712TypeUnit(name = "amountS", typ = "uint"),
      EIP712TypeUnit(name = "amountB", typ = "uint"),
      EIP712TypeUnit(name = "feeAmount", typ = "uint"),
      EIP712TypeUnit(name = "validSince", typ = "uint"),
      EIP712TypeUnit(name = "validUntil", typ = "uint"),
      EIP712TypeUnit(name = "owner", typ = "address"),
      EIP712TypeUnit(name = "tokenS", typ = "address"),
      EIP712TypeUnit(name = "tokenB", typ = "address"),
      EIP712TypeUnit(name = "dualAuthAddr", typ = "address"),
      EIP712TypeUnit(name = "broker", typ = "address"),
      EIP712TypeUnit(name = "orderInterceptor", typ = "address"),
      EIP712TypeUnit(name = "wallet", typ = "address"),
      EIP712TypeUnit(name = "tokenRecipient", typ = "address"),
      EIP712TypeUnit(name = "feeToken", typ = "address"),
      EIP712TypeUnit(name = "walletSplitPercentage", typ = "uint16"),
      EIP712TypeUnit(name = "tokenSFeePercentage", typ = "uint16"),
      EIP712TypeUnit(name = "tokenBFeePercentage", typ = "uint16"),
      EIP712TypeUnit(name = "allOrNone", typ = "bool")
    )

    val domain = EIP712Domain(name = "Loopring Protocol", version = "2")

    var message = new EIP712Data
    message += "amountS" → order.amountS
    message += "amountB" → order.amountB
    message += "feeAmount" → order.feeAmount
    message += "validSince" → BigInt(order.validSince)
    message += "validUntil" → BigInt(order.validUntil)
    message += "owner" → order.owner
    message += "tokenS" → order.tokenS
    message += "tokenB" → order.tokenB
    message += "dualAuthAddr" → order.dualAuthAddress
    message += "broker" → order.broker
    message += "orderInterceptor" → order.orderInterceptor
    message += "wallet" → order.wallet
    message += "tokenRecipient" → order.tokenReceipt
    message += "feeToken" → order.feeToken
    message += "walletSplitPercentage" → order.walletSplitPercentage
    message += "tokenSFeePercentage" → order.tokenSFeePercentage
    message += "tokenBFeePercentage" → order.tokenBFeePercentage
    message += "allOrNone" → order.allOrNone

    EIP712TypeData(
      types = types,
      primaryType = "Order",
      domain = domain,
      message = message
    )
  }
}
