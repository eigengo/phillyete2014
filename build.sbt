organization := "org.eigengo"

name := "phillyete2014"

version := "1.0.0"

scalaVersion := "2.10.4"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

resolvers ++= Seq(
  "Spray Releases" at "http://repo.spray.io",
  "Spray Releases" at "http://repo.spray.io"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka"       %% "akka-actor"         % "2.3.2",
  "com.typesafe.akka"       %% "akka-slf4j"         % "2.3.2",
  "io.spray"                 % "spray-routing"      % "1.3.0",
  "io.spray"                 % "spray-client"       % "1.3.0",
  "io.spray"                %% "spray-json"         % "1.2.3",
  "io.spray"                 % "spray-testkit"      % "1.3.0"    % "test",
  "com.typesafe.akka"       %% "akka-testkit"       % "2.3.2"    % "test",
  "org.specs2"              %% "specs2"             % "2.3.11"    % "test"
)

parallelExecution in Test := false

transitiveClassifiers := Seq("sources")

initialCommands in console := "import org.eigengo.phillyete._,akka.actor._"

initialCommands in (Test, console) <<= (initialCommands in console)(_ + ",akka.testkit._")
