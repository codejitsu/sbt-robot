sbtPlugin := true

organization := "net.codejitsu"

name := "sbt-robot"

scalaVersion := "2.10.4"

scalacOptions ++= Seq("-deprecation", "-feature")

resolvers += Resolver.sonatypeRepo("snapshots")

resolvers += "codejitsu at bintray" at "http://dl.bintray.com/codejitsu/maven"

libraryDependencies ++= Seq(
  "net.codejitsu" % "tasks-dsl_2.11" % "0.0.1"
)

publishMavenStyle := false

/** Console */
initialCommands in console := "import net.codejitsu.robot._"
