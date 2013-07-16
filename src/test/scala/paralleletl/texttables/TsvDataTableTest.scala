package paralleletl.texttables

import org.scalatest.FunSuite
import com.mirzov.oleg.paralleletl.texttables.TsvDataTable
import scala.concurrent.ExecutionContext.Implicits.global
import TestHelp._
import java.io.File

class TsvDataTableTest extends FunSuite{

	test("Basic TSV string parsing"){
		val str = """A	B
a1	b1"""
		val resFuture = TsvDataTable(str).getTsvString

		assert(await(resFuture) === str)
	}

	test("Basic TSV string parsing twice"){
		val str = """A	B
a1	b1
a2	b2"""
		val tbl = TsvDataTable(str)
		await(tbl.getTsvString)
		val res = await(tbl.getTsvString)

		assert(res === str)
	}
	
	test("Reading basic TSV from file"){
		val tbl = TsvDataTable(() => getClass.getResourceAsStream("/testtsv1.txt"))
		val expected = """A	B
a1	b1
a2	b2"""
		assert(await(tbl.getTsvString) === expected)
	}
  
}