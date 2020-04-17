package readerT.demo

import cats.Monad
import cats.data.ReaderT
import fs2.Stream
import readerT.demo.handlers.Handlers
import cats.syntax.apply._

package object streams {

  case class Streams[F[_]](streamA: Stream[F, Unit], streamB: Stream[F, Unit])
  object Streams{
    def readerT[F[_]: Monad]: ReaderT[F, Config, Streams[F]] = Handlers.readerT[F].andThen(
      (
        StreamA.readerT[F],
        StreamB.readerT[F]
        ).mapN(Streams.apply)
    )
  }

}
