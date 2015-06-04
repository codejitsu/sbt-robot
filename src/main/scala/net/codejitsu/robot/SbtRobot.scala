package net.codejitsu.robot

import net.codejitsu.tasks.dsl.TaskM
import net.codejitsu.tasks.dsl.VerbosityLevel._
import sbt._

import scala.util.Success

/**
 * Sbt Robot plugin main class
 */
object SbtRobot extends sbt.AutoPlugin {
  object autoImport {
    /**
     * Configuration for the plugin.
     */
    lazy val Robot = config("robot") extend (Compile)
  }

  import autoImport._

  /**
   * Adds new task to the 'Robot' configuration.
   *
   * @param task the task definition
   * @param name name of the new task
   * @param description description of the new task
   * @param verboseLevel verbosity level of the new task
   * @return a sbt task
   */
  def defineTask(task: TaskM[Boolean], name: String = "", description: String = "no description",
                 verboseLevel: VerbosityLevel = Verbose) = {
    //TODO check if the name already exists

    (TaskKey[Option[String]](name, description) in Robot) <<= Def.task {
      println(s"running task '$name' ($description)")
      val (result, _, _) = task.run(verboseLevel)

      result match {
        case Success(r) if r =>
          Option(s"task '$name' ($description) completed successfully.")
        case _ =>
          sys.error(s"task '$name' ($description) failed.")
          None
      }
    }
  }
}
