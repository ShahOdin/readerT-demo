package readerT.demo.server

import readerT.demo.handlers.Handlers
import fs2.Stream

object StreamB {

  def apply[F[_]](handlers: Handlers[F]): Stream[F, Unit] = ???

}