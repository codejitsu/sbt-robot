sbtPlugin := true

organization := "net.codejitsu"

name := "sbt-robot"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.4"

scalacOptions ++= Seq("-deprecation", "-feature")

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "net.codejitsu" % "tasks-dsl_2.11" % "0.0.1-SNAPSHOT"
)

publishMavenStyle := false

/** Console */
initialCommands in console := "import net.codejitsu.robot._"
