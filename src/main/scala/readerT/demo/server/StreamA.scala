package readerT.demo.server

import readerT.demo.handlers.Handlers
import fs2.Stream

object StreamA {

  def apply[F[_]](handlers: Handlers[F]): Stream[F, Unit] = ???

}
