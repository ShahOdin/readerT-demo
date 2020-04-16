package readerT.demo.clients

import cats.data.ReaderT
import readerT.demo.Config
import Config._

trait BarClient[F[_]]
object BarClient {
  def readerT[F[_]]: ReaderT[F, Config, BarClient[F]] = readerT_.local(_.barConfig)
  private def readerT_[F[_]]: ReaderT[F, BarConfig, BarClient[F]] = ???
}
