package com.mirzov.oleg.paralleletl.texttables

import play.api.libs.iteratee.Enumerator
import scala.concurrent.ExecutionContext

trait ArrayTextTable extends TextTable{
  
	protected def arrays: TraversableOnce[Array[String]]
	implicit val exeCtxt: ExecutionContext
	
	override def enum = Enumerator.enumerate[TextTableRow]({
		val colNames = columnNames
		val nOfCols = colNames.length
		val colIndexLookup = colNames.zipWithIndex.toMap
		arrays.toIterator.map{array =>
			assert(array.length == nOfCols, "TextTable must have the same number of columns in every row!")
			new ArrayTableRow(array, colIndexLookup)
		}
	})(exeCtxt)
}

object ArrayTextTable{
	def apply(colNames: Seq[String])(arrs: TraversableOnce[Array[String]])(implicit ctxt: ExecutionContext): TextTable = {
		new ArrayTextTable{
			override val columnNames = colNames
			override val arrays = arrs
			override val exeCtxt = ctxt
		}
	}
}
