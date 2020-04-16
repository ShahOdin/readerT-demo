package readerT.demo

case class Config(fooConfig: Config.FooConfig, barConfig: Config.BarConfig, bazConfig: Config.BazConfig)

object Config {
  trait FooConfig
  trait BarConfig
  trait BazConfig
}
