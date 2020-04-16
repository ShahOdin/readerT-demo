package readerT.demo

import cats.Monad
import cats.data.ReaderT
import readerT.demo.clients.Clients
import cats.syntax.apply._

package object handlers {
  case class Handlers[F[_]](quxHandler: QuxHandler[F], quuxHandler: QuuxHandler[F])

  object Handlers {
    def readerT[F[_] : Monad]: ReaderT[F, Config, Handlers[F]] = {
      Clients.readerT
        .andThen(
          (
            QuxHandler.readerT[F],
            QuuxHandler.readerT[F]
            ).tupled
        ).map { case (qux, quux) =>
        Handlers(qux, quux)
      }
    }
  }

}
