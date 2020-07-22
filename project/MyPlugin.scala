import sbt._

/*
 * https://www.scala-sbt.org/1.x/docs/Plugins.html
 * https://www.scala-sbt.org/1.x/docs/Plugins-Best-Practices.html
 * https://leobenkel.com/2018/11/make-sbt-plugin
 * https://github.com/freewind/my-sbt-plugin
 *
 * sbt 'set logLevel := Level.Debug' hello
 */
object MyPlugin extends AutoPlugin {
  override def requires = plugins.JvmPlugin
  override def trigger  = allRequirements

  object autoImport {
    val greeting = settingKey[String]("greeting")
    val hello    = taskKey[Unit]("say hello")
  }

  import autoImport._

  private[this] lazy val helloTask =
    Def.task {
      val log = sbt.Keys.streams.value.log
      log.info("HELLO_TASK")
      HelloTask.apply(greeting.value)
    }

  override lazy val buildSettings = Seq(
    greeting := "Hi!",
    hello := helloTask.value
  )
}

object HelloTask {
  def apply(value: String): Unit =
    println(value)
}
