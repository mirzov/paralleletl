package paralleletl.texttables

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TestHelp {

	def await(fut: Future[String]) = Await.result(fut, Duration.Inf)

}