package net.codejitsu.robot

import net.codejitsu.tasks.dsl.{GenericTask, TaskM, User, NoUser}
import sbt.AutoPlugin
import sbt._
import Keys._
import sbt.complete.Parsers._

/**
 * Sbt Robot plugin main class
 */
class SbtRobot extends AutoPlugin {
  val user = settingKey[User]("user")
  val tasks = settingKey[Seq[GenericTask]]("tasks")

  val RobotConfig = config("robot")

  val settings = inConfig(RobotConfig)(Seq(
    user := NoUser,
    tasks := Seq.empty[GenericTask]
  ))
}
