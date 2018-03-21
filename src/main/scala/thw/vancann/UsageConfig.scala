package thw.vancann

import scopt.OptionParser

trait Config

case class UsageConfig(date: String = "") extends Config

class UsageOptionParser
    extends OptionParser[UsageConfig]("job config") {
  head("scopt", "3.x")

  opt[String]('d', "date").required
    .action((value, arg) => {
      arg.copy(date = value)
    })
    .text("The date")
}
