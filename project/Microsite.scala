import microsites.MicrositesPlugin
import microsites.MicrositesPlugin.autoImport._

object Microsite {

  lazy val plugin = MicrositesPlugin

  lazy val settings = Seq(
    micrositeAuthor := "niqdev",

    micrositeGitterChannel := false,
    micrositeShareOnSocial := false,
    micrositeGithubOwner := "niqdev",
    micrositeGithubRepo := "fp",

    micrositeCompilingDocsTool := WithMdoc
  )

}
