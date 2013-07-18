package paralleletl.texttables

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TestHelp {

	def await[T](fut: Future[T]) = Await.result(fut, Duration.Inf)

}