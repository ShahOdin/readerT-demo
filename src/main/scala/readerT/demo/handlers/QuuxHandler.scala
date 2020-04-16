package readerT.demo.handlers

import cats.Monad
import cats.data.ReaderT
import readerT.demo.clients.{Clients, BarClient, BazClient}

trait QuuxHandler[F[_]]

object QuuxHandler {
  def readerT[F[_] : Monad]: ReaderT[F, Clients[F], QuuxHandler[F]] = readerT_.local(
    clients => (clients.barClient, clients.bazClient)
  )

  private def readerT_[F[_]]: ReaderT[F, (BarClient[F], BazClient[F]), QuuxHandler[F]] = ???
}
