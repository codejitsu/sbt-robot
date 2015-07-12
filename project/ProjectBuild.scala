import sbt.Keys._
import sbt._

object ProjectBuild extends Build {
  import Settings._

  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = parentSettings ++ defaultSettings ++ Seq(libraryDependencies ++= Dependencies.robotDeps)
  )
}

object Dependencies {
  import Versions._

  object Compile {
    val tasks     = "net.codejitsu" % "tasks-dsl_2.11" % TasksVer
  }

  import Compile._

  val robotDeps = Seq(tasks)
}
