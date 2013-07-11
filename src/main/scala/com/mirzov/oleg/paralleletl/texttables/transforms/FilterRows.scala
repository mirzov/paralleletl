package com.mirzov.oleg.paralleletl.texttables.transforms

import com.mirzov.oleg.paralleletl.texttables._
import com.mirzov.oleg.paralleletl.core.FlowTransform
import play.api.libs.iteratee.Enumeratee

class FilterRows(val original: TextTable, pred: TextTableRow => Boolean) extends
						FlowTransform[TextTableRow, TextTableRow] with TextTable{

	override lazy val columnNames: Seq[String] = original.columnNames
	final override def transform = Enumeratee.filter(pred)
	
}