package com.mirzov.oleg.paralleletl

import texttables.transforms.DropColumns
import texttables.transforms.FilterRows
import play.api.libs.iteratee.Iteratee
import scala.concurrent.Future
import com.mirzov.oleg.paralleletl.core.FlowSink
import scala.concurrent.ExecutionContext

package object texttables{

	implicit class TextTableEnriched(tbl: TextTable)(implicit exc: ExecutionContext) {
	  
		def getTsvString: Future[String] = {
		  	val header = tbl.columnNames.mkString("", "\t", "\n")
		  	(tbl |>>> TextTableStringSink).map(s => s.mkString(header, "\n", ""))
		}
		
		def dropColumns(cols: String*): TextTable = new DropColumns(tbl, cols:_*)
		
		def where(filter: TextTableRow => Boolean): TextTable = new FilterRows(tbl, filter)
	}
	
	private object TextTableStringSink extends FlowSink[TextTableRow, Seq[String]]{
		
		override final val iter: Iteratee[TextTableRow, Seq[String]] =
			Iteratee.fold(Seq[String]())((s, r: TextTableRow) => {
				s :+ r.mkString("\t")
			})
			
	}

}