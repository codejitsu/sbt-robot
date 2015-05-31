sbtPlugin := true

organization := "net.codejitsu"

name := "sbt-robot"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-deprecation", "-feature")

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq()

publishMavenStyle := false

/** Console */
initialCommands in console := "import net.codejitsu.robot._"
