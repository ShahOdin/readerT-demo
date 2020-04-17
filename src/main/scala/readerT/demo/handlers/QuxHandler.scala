package readerT.demo.handlers

import cats.data.ReaderT
import readerT.demo.clients.{Clients, BarClient, FooClient}

trait QuxHandler[F[_]]

object QuxHandler {
  def readerT[F[_]]: ReaderT[F, Clients[F], QuxHandler[F]] = readerT_.local(
    clients => (clients.fooClient, clients.barClient)
  )

  private def readerT_[F[_]]: ReaderT[F, (FooClient[F], BarClient[F]), QuxHandler[F]] = ???
}
