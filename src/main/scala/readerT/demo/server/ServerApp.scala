package readerT.demo.server

import cats.effect.{ExitCode, IO, IOApp}
import readerT.demo.Config
import fs2.Stream
import readerT.demo.streams.Streams
import cats.syntax.functor._

object ServerApp extends IOApp {
  val config: Config = ???

  def stream: Stream[IO, Unit] = for {
    streams <- Stream.eval(Streams.readerT[IO].run(config))
    _ <- streams.streamA.merge(streams.streamB)
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = stream.compile.drain.as(ExitCode.Success)
}
