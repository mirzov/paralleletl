package com.mirzov.oleg.paralleletl.texttables

import com.mirzov.oleg.paralleletl.core.FlowTransform
import play.api.libs.iteratee.Enumeratee

trait TextTableTransform extends FlowTransform[TextTableRow, TextTableRow] with TextTable {
  
	def rowTransform(in: TextTableRow): TextTableRow
	
	import scala.language.reflectiveCalls
	final override def transform = Enumeratee.map(rowTransform)
}