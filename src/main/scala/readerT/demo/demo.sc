import cats.data.ReaderT
import cats.Monad
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

import cats.syntax.apply._
import cats.syntax.functor._

case class Config(fooConfig: Config.FooConfig, barConfig: Config.BarConfig, bazConfig: Config.BazConfig)
object Config {
  trait FooConfig
  trait BarConfig
  trait BazConfig
}


case class Clients[F[_]](fooClient: Clients.FooClient[F], barClient: Clients.BarClient[F], bazClient: Clients.BazClient[F])
object Clients {
  def readerT[F[_] : Monad]: ReaderT[F, Config, Clients[F]] = (
    FooClient.readerT[F],
    BarClient.readerT[F],
    BazClient.readerT[F]
    ).mapN(Clients.apply)

  trait BarClient[F[_]]
  object BarClient {
    def readerT[F[_]]: ReaderT[F, Config, BarClient[F]] = readerT_.local(_.barConfig)
    private def readerT_[F[_]]: ReaderT[F, Config.BarConfig, BarClient[F]] = ???
  }

  trait BazClient[F[_]]
  object BazClient {
    def readerT[F[_]]: ReaderT[F, Config, BazClient[F]] = readerT_.local(_.bazConfig)
    private def readerT_[F[_]]: ReaderT[F, Config.BazConfig, BazClient[F]] = ???
  }

  trait FooClient[F[_]]
  object FooClient {
    def readerT[F[_]]: ReaderT[F, Config, FooClient[F]] = readerT_.local(_.fooConfig)
    private def readerT_[F[_]]: ReaderT[F, Config.FooConfig, FooClient[F]] = ???
  }

}

case class Handlers[F[_]](quxHandler: Handlers.QuxHandler[F], quuxHandler: Handlers.QuuxHandler[F])
object Handlers {
  def readerT[F[_] : Monad]: ReaderT[F, Config, Handlers[F]] = {
    Clients.readerT.andThen(
      (
        QuxHandler.readerT[F],
        QuuxHandler.readerT[F]
      ).mapN(Handlers.apply)
    )
  }

  trait QuxHandler[F[_]]
  object QuxHandler {
    def readerT[F[_] : Monad]: ReaderT[F, Clients[F], QuxHandler[F]] = readerT_.local(
      clients => (clients.fooClient, clients.barClient)
    )

    private def readerT_[F[_]]: ReaderT[F, (Clients.FooClient[F], Clients.BarClient[F]), QuxHandler[F]] = ???
  }

  trait QuuxHandler[F[_]]
  object QuuxHandler {
    def readerT[F[_] : Monad]: ReaderT[F, Clients[F], QuuxHandler[F]] = readerT_.local(
      clients => (clients.barClient, clients.bazClient)
    )

    private def readerT_[F[_]]: ReaderT[F, (Clients.BarClient[F], Clients.BazClient[F]), QuuxHandler[F]] = ???
  }

}

case class Streams[F[_]](streamA: Stream[F, Unit], streamB: Stream[F, Unit])
object Streams {
  object StreamA {
    def readerT[F[_]]: ReaderT[F, Handlers[F], Stream[F, Unit]] = ???
  }
  object StreamB {
    def readerT[F[_]]: ReaderT[F, Handlers[F], Stream[F, Unit]] = ???
  }

  def readerT[F[_]: Monad]: ReaderT[F, Config, Streams[F]] = Handlers.readerT[F].andThen(
    (
      StreamA.readerT[F],
      StreamB.readerT[F]
      ).mapN(Streams.apply)
  )

}

object ServiceApp extends IOApp {
  val config: Config = ???

  def stream: Stream[IO, Unit] = for {
    streams <- Stream.eval(Streams.readerT[IO].run(config))
    _ <- streams.streamA.merge(streams.streamB)
  } yield ()
  override def run(args: List[String]): IO[ExitCode] = stream.compile.drain.as(ExitCode.Success)
}