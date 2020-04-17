import cats.{Applicative, Monad}
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream
import cats.syntax.apply._
import cats.syntax.functor._
import cats.syntax.flatMap._

case class Config(fooConfig: Config.FooConfig, barConfig: Config.BarConfig, bazConfig: Config.BazConfig)
object Config {
  trait FooConfig
  trait BarConfig
  trait BazConfig
}

case class Clients[F[_]](fooClient: Clients.FooClient[F], barClient: Clients.BarClient[F], bazClient: Clients.BazClient[F])
object Clients {
  def apply[F[_]: Applicative](config: Config): F[Clients[F]] = (
    FooClient[F](config),
    BarClient[F](config),
    BazClient[F](config)
    ).mapN(Clients.apply)

  trait FooClient[F[_]]
  object FooClient {
    def apply[F[_]](config: Config): F[FooClient[F]] = apply_[F](config.fooConfig)
    private def apply_[F[_]](config: Config.FooConfig): F[FooClient[F]] = ???
  }

  trait BarClient[F[_]]
  object BarClient {
    def apply[F[_]](config: Config): F[BarClient[F]] = apply_[F](config.barConfig)
    private def apply_[F[_]](config: Config.BarConfig): F[BarClient[F]] = ???
  }

  trait BazClient[F[_]]
  object BazClient {
    def apply[F[_]](config: Config): F[BazClient[F]] = apply_[F](config.bazConfig)
    private def apply_[F[_]](config: Config.BazConfig): F[BazClient[F]] = ???
  }
}

case class Handlers[F[_]](quxHandler: Handlers.QuxHandler[F], quuxHandler: Handlers.QuuxHandler[F])
object Handlers {
  def apply[F[_]: Monad](config: Config): F[Handlers[F]] = for {
    clients <- Clients(config)
    qux <- QuxHandler(clients)
    quux <- QuuxHandler(clients)
  } yield Handlers(qux, quux)

  trait QuxHandler[F[_]]
  object QuxHandler {
    def apply[F[_] : Monad](clients: Clients[F]): F[QuxHandler[F]] = apply_(clients.fooClient, clients.barClient)
    private def apply_[F[_]](foo: Clients.FooClient[F], bar: Clients.BarClient[F]): F[QuxHandler[F]] = ???
  }

  trait QuuxHandler[F[_]]
  object QuuxHandler {
    def apply[F[_] : Monad](clients: Clients[F]): F[QuuxHandler[F]] = apply_(clients.barClient, clients.bazClient)
    private def apply_[F[_]](foo: Clients.BarClient[F], bar: Clients.BazClient[F]): F[QuuxHandler[F]] = ???
  }
}

case class Streams[F[_]](streamA: Stream[F, Unit], streamB: Stream[F, Unit])
object Streams {
  object StreamA {
    def apply[F[_]](handlers: Handlers[F]): F[Stream[F, Unit]] = ???
  }
  object StreamB {
    def apply[F[_]](handlers: Handlers[F]): F[Stream[F, Unit]] = ???
  }

  def readerT[F[_]: Monad](config: Config): F[Streams[F]] = for {
    handlers <- Handlers(config)
    streamA <- StreamA(handlers)
    streamB <- StreamA(handlers)
  } yield Streams(streamA, streamB)

}

object ServiceApp extends IOApp {
  val config: Config = ???

  def stream: Stream[IO, Unit] = for {
    streams <- Stream.eval(Streams.readerT[IO](config))
    _ <- streams.streamA.merge(streams.streamB)
  } yield ()
  override def run(args: List[String]): IO[ExitCode] = stream.compile.drain.as(ExitCode.Success)
}
