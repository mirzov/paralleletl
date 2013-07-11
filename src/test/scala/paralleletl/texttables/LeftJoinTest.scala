package paralleletl.texttables

import org.scalatest.FunSuite
import com.mirzov.oleg.paralleletl.texttables.ArrayTextTable
import scala.concurrent.ExecutionContext.Implicits.global
import com.mirzov.oleg.paralleletl.texttables.transforms.LeftJoin
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class LeftJoinTest extends FunSuite {
	test("Simplest left join"){
		val left = ArrayTextTable(Seq("A", "B"))(Seq(
		    Array("a1", "b1"),
		    Array("a2", "b2")
		))
		val right = ArrayTextTable(Seq("B", "C"))(Seq(
		    Array("b1", "c1"),
		    Array("b2", "c2")
		))
		val expected = ArrayTextTable(Seq("A", "B", "C"))(Seq(
		    Array("a1", "b1", "c1"),
		    Array("a2", "b2", "c2")
		)).getTsvString
		
		val actual = new LeftJoin(left, right, Seq("B"), Seq("B")).getTsvString
		
		def wait(fut: Future[String]) = Await.result(fut, Duration.Inf)
		println(wait(actual))
		assert(wait(actual) === wait(expected))
	}
}