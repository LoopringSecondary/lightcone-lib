import sbt._

object Dependencies {
  lazy val commonDependency = Seq(
    "org.scalatest" %% "scalatest" % "3.0.0" % Test,
    "tv.cntt" %% "slf4s-api" % "1.7.25", //"org.slf4s" %% "slf4s-api" % "1.7.12",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "com.github.nscala-time" %% "nscala-time" % "2.20.0",
    "org.web3j" % "core" % "3.4.0",
    "org.ethereum" % "ethereumj-core" % "1.8.2-RELEASE")
}
