package readerT.demo

import cats.data.ReaderT
import cats.Applicative
import cats.syntax.apply._

package object clients {

  case class Clients[F[_]](fooClient: FooClient[F], barClient: BarClient[F], bazClient: BazClient[F])
  object Clients {
    def readerT[F[_] : Applicative]: ReaderT[F, Config, Clients[F]] = (
      FooClient.readerT[F],
      BarClient.readerT[F],
      BazClient.readerT[F]
      ).mapN(Clients.apply)
  }

}
