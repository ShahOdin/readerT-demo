package readerT.demo.streams

import cats.data.ReaderT
import fs2.Stream
import readerT.demo.handlers.Handlers

object StreamB {
  def readerT[F[_]]: ReaderT[F, Handlers[F], Stream[F, Unit]] = ???
}