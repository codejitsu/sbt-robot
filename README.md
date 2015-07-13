sbt-robot
=========
```sbt-robot``` is a SBT-Plugin for custom (shell)-scripts/tasks defined and executed in SBT.

It uses the ```tasks```-library for task definition (see https://github.com/codejitsu/tasks).

The plugin can be used for ```continuous integration``` and ```continuous deployment``` tasks.

Installation
============

Add the following line to your ```project/plugins.sbt``` file:

```scala
resolvers += "codejitsu at bintray" at "http://dl.bintray.com/codejitsu/maven"

resolvers += Resolver.url(
   "codejitsu-sbt-plugin-releases",
     url("https://dl.bintray.com/codejitsu/sbt-plugins"))(Resolver.ivyStylePatterns)
         
addSbtPlugin("net.codejitsu" %% "sbt-robot" % "0.0.1")
```

Define a task file in your ```project``` directory: for example ```DeploymentTasks.scala```.

Add tasks to your ```DeploymentTasks.scala```:

```scala
import net.codejitsu.tasks._
import net.codejitsu.tasks.dsl._
import net.codejitsu.tasks.dsl.Tasks._
import projectbuildinfo.BuildInfo._

import scala.concurrent.duration._

object DeploymentTasks {
  val hosts = "my.host" | (1 to 4) | ".hosting.net"

  implicit val user = User.load
  implicit val stage = new Dev

  val artifact = s"$name-$version.war"
  val webapps = "/tomcat/webapps"

  val NotifyMessageOk = PostRequest(...)

  val NotifyMessageFail = PostRequest(...)

  val deployApp =
      Par ~ RmIfExists(hosts, user.home / s"$artifact*") andThen
      Par ~ Upload(hosts, targetPath / artifact, user.home) andThen
      Sudo ~ Par ~ StopTomcat(hosts) andThen
      Sudo ~ Par ~ RmIfExists(hosts, webapps / s"$name*") andThen
      Sudo ~ Par ~ Mv(hosts, user.home / artifact, webapps) andThen
      Sudo ~ Par ~ StartTomcat(hosts) andThen
      Wait(5 seconds) andThen
      Par ~ CheckUrl(hosts, s"/$name-$version/info/", 8080, _.contains("Status: OK")) andThen
      NotifyMessageOk orElse
      NotifyMessageFail
}
```

So, now install your task(s) in ```build.sbt``` or ```Build.scala```.
 
To install a task in ```build.sbt```:
 
```scala
import SbtRobot._
import DeploymentTasks._

defineTask(deployApp, "deploy", "deploy webapp with status check")

defineTask(restartCassandra, "restartCassandra", "restart Cassandra cluster")
``` 

To install a task in ```Build.scala``` simply add the ```defineTask``` call to your Settings:

```scala
  lazy val deploySettings = Seq(
    defineTask(deployApp, "deploy", "deploy webapp with status check"),
    defineTask(restartCassandra, "restartCassandra", "restart Cassandra cluster")
  )
```

Make sure that your project has the ```deploySettings``` as dependency:

```scala
  lazy val root = Project(
    id = "robot-test",
    base = file("."),
    settings = deploySettings
  )
```

Features
========

* Build complex scripts and run them in SBT
  * Universal tasks like ```Mkdir```, ```Upload```, ```CheckUrl``` available
* Register common tasks like ```restartCassandra```
* Run ```ssh``` tasks on multiple hosts in parallel      
* Compile time access control for each task via explicit `stage` permissions
  
Get your artifact information
==============================
   
In order to use some artifacts infos like ```version``` and ```name``` please add the following script to the ```build.sbt```:
   
```scala
val infoFiles = taskKey[Seq[File]]("")

infoFiles := {
  val base = (baseDirectory in Compile).value
  val file = base / "project" / "BuildInfo.scala"
  val content = "package projectbuildinfo\n" +
                "object BuildInfo {\n" +
                "val name = \"" + name.value + "\"\n" +
                "val version = \"" + version.value + "\"\n" +
                "val targetPath = \"" + target.value.getPath + "\"\n" +
                "}\n"
  IO.write(file, content)
  Seq(file)
}

sourceGenerators in Compile <+= infoFiles
```   

After the ```BuildInfo.scala``` is generated it can be imported and used in your scripts like described above.
 
Enjoy
=====

All tasks are enabled in the ```Robot``` configuration. So, to call the ```deploy```-task do:

```
> robot:deploy
```

All tasks can be combined together to produce a new task, for example:
   
```scala
  val restartTomcats =
    Sudo ~ Par ~ StopTomcat(hosts) andThen
    Sudo ~ Par ~ StartTomcat(hosts)
```

Functional tasks composition with `andThen` is also short circuit upon error - when an error occurs during a task 
then the rest of the script logic isn't executed and the result is a type containing the first error encountered.

After adding ```defineTask(restartTomcats, "restartTomcats")``` your task is available in SBT shell: ```robot:restartTomcats```.

(more detailed topics available at https://github.com/codejitsu/tasks). 
