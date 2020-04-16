package readerT.demo.clients

import cats.data.ReaderT
import readerT.demo.Config
import Config._

trait BazClient[F[_]]
object BazClient {
  def readerT[F[_]]: ReaderT[F, Config, BazClient[F]] = readerT_.local(_.bazConfig)
  private def readerT_[F[_]]: ReaderT[F, BazConfig, BazClient[F]] = ???
}
