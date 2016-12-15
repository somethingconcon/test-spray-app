resolvers += sbtResolver.value

libraryDependencies ++= {

  val akka  = "2.4.6"
  val spray = "1.3.4"
  val kamon = "0.6.1"

  Seq(
    "ch.qos.logback"               % "logback-classic"             % "1.0.9",
    "com.typesafe.akka"            %% "akka-actor"                  % "2.3.14",
    "com.typesafe.akka"            %% "akka-slf4j"                  % "2.3.14",
    "io.kamon"                     %% "kamon-akka"                  % kamon,
    "io.kamon"                     %% "kamon-autoweave"             % kamon,
    "io.kamon"                     %% "kamon-core"                  % kamon,
    "io.kamon"                     %% "kamon-log-reporter"          % kamon,
    "io.kamon"                     %% "kamon-newrelic"              % kamon,
    "io.kamon"                     %% "kamon-spray"                 % kamon,
    "io.spray"                     %% "spray-can"                   % spray,
    "io.spray"                     %% "spray-httpx"                 % spray,
    "io.spray"                     %% "spray-routing"               % spray,
    "io.spray"                     %% "spray-testkit"               % spray         % "test",
    "org.scala-sbt"                 % "launcher-interface"          % "1.0.0"       % "provided",
    "org.scalamock"                %% "scalamock-scalatest-support" % "3.3.0"       % "test",
    "org.scalatest"                %% "scalatest"                   % "3.0.0"       % "test",
    "com.lihaoyi"                   % "ammonite-repl"               % "0.7.7"       % "test" cross CrossVersion.full
  )
}
