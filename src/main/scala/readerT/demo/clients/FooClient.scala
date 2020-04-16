package readerT.demo.clients

import cats.data.ReaderT
import readerT.demo.Config
import Config._

trait FooClient[F[_]]
object FooClient {
  def readerT[F[_]]: ReaderT[F, Config, FooClient[F]] = readerT_.local(_.fooConfig)
  private def readerT_[F[_]]: ReaderT[F, FooConfig, FooClient[F]] = ???
}
