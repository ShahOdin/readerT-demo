package readerT.demo.server

import cats.effect.{ExitCode, IO, IOApp}
import readerT.demo.Config
import readerT.demo.handlers.Handlers
import fs2.Stream

import cats.syntax.functor._

object ServiceApp extends IOApp {
  val config: Config = ???

  def stream: Stream[IO, Unit] = for {
    handlers <- Stream.eval(Handlers.readerT[IO].run(config))
    _ <- StreamA(handlers).merge(StreamA(handlers))
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = stream.compile.drain.as(ExitCode.Success)
}
