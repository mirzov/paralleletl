package paralleletl.texttables

import org.scalatest.FunSuite
import com.mirzov.oleg.paralleletl.texttables.ArrayTextTable
import scala.concurrent.ExecutionContext.Implicits.global
import com.mirzov.oleg.paralleletl.texttables.transforms.LeftJoin
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class LeftJoinTest extends FunSuite {
  
	private val left = ArrayTextTable(Seq("A", "B"))(Seq(
	    Array("a1", "b1"),
	    Array("a2", "b2")
	))
	
	private val right = ArrayTextTable(Seq("B", "C"))(Seq(
	    Array("b1", "c1"),
	    Array("b2", "c2")
	))
	
	private def wait(fut: Future[String]) = Await.result(fut, Duration.Inf)
	
	test("Simplest left join"){
		val expected = ArrayTextTable(Seq("A", "B", "C"))(Seq(
		    Array("a1", "b1", "c1"),
		    Array("a2", "b2", "c2")
		)).getTsvString
		
		val actual = new LeftJoin(left, right, Seq("B"), Seq("B")).getTsvString
		
		assert(wait(actual) === wait(expected))
	}
	
	test("Redundant right rows and orphan left rows"){
		val right = ArrayTextTable(Seq("B", "C"))(Seq(
		    Array("b1", "c1"),
		    Array("b3", "c3")
		))
		val expected = ArrayTextTable(Seq("A", "B", "C"))(Seq(
		    Array("a1", "b1", "c1"),
		    Array("a2", "b2", LeftJoin.emptyCellValue)
		)).getTsvString
		
		val actual = new LeftJoin(left, right, Seq("B"), Seq("B")).getTsvString
		
		assert(wait(actual) === wait(expected))
	}
}