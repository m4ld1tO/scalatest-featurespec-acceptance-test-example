import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val playWs =  "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.0.0-M1"
  lazy val playWsJson = "com.typesafe.play" %% "play-ws-standalone-json" % "2.0.0-M1"
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.5.12"
  lazy val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % "2.5.12"

  // Projects
 val backendDeps =
   Seq(akkaTestKit, akkaActor, playWs, playWsJson, scalaTest % Test)
}
