package paralleletl.texttables

import org.scalatest.FunSuite
import com.mirzov.oleg.paralleletl.texttables.ArrayTextTable
import scala.concurrent.ExecutionContext.Implicits.global
import com.mirzov.oleg.paralleletl.texttables.transforms.LeftJoin
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import TestHelp._
import com.mirzov.oleg.paralleletl.texttables.TsvDataTable

class LeftJoinTest extends FunSuite {
  
	private val left = TsvDataTable("""A	B
a1	b1
a2	b2""")
	
	private val right = TsvDataTable("""B	C
b1	c1
b2	c2""") 
	  
	test("Simplest left join"){
		val expected = TsvDataTable("""A	B	C
a1	b1	c1
a2	b2	c2""").getTsvString
		
		val actual = new LeftJoin(left, right, Seq("B"), Seq("B")).getTsvString
		
		assert(await(actual) === await(expected))
	}
	
	test("Redundant right rows and orphan left rows"){
		val right = TsvDataTable("""B	C
b1	c1
b3	c3""") 
		val expected = ArrayTextTable(Seq("A", "B", "C"))(Seq(
		    Array("a1", "b1", "c1"),
		    Array("a2", "b2", LeftJoin.emptyCellValue)
		)).getTsvString
		
		val actual = new LeftJoin(left, right, Seq("B"), Seq("B")).getTsvString
		
		assert(await(actual) === await(expected))
	}
	
	test("Duplicate right rows"){
		val right = TsvDataTable("""B	C
b1	c1
b1	c2
b2	c3""") 
		val expected = TsvDataTable("""A	B	C
a1	b1	c1
a1	b1	c2
a2	b2	c3""").getTsvString
		
		val actual = new LeftJoin(left, right, Seq("B"), Seq("B")).getTsvString
		
		assert(await(actual) === await(expected))
	}
	
	test("Multi-column key"){
		val left = TsvDataTable("""A	B	C
a1	b1	c1
a2	b2	c2""")
		val right = TsvDataTable("""B	C	D
b1	c1	d1
b2	c2	d2""")
		val expected = TsvDataTable("""A	B	C	D
a1	b1	c1	d1
a2	b2	c2	d2""").getTsvString

		val actual = new LeftJoin(left, right, Seq("B", "C"), Seq("B", "C")).getTsvString
		
		assert(await(actual) === await(expected))
	}
	
	test("Same-column conflict resolution"){
		val left = TsvDataTable("""A	B	C
a1	b1	c1_left
a2	b2	c2_left""")
		val right = TsvDataTable("""B	C	D
b1	c1_right	d1
b2	c2_right	d2""")
		val expected = TsvDataTable("""A	B	C	D
a1	b1	c1_left	d1
a2	b2	c2_left	d2""").getTsvString

		val actual = new LeftJoin(left, right, Seq("B"), Seq("B")).getTsvString
		
		assert(await(actual) === await(expected))
	}
}