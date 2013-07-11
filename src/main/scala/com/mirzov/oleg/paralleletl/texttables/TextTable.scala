package com.mirzov.oleg.paralleletl.texttables

import com.mirzov.oleg.paralleletl.core.FlowSource

trait TextTableRow extends IndexedSeq[String] {
  
	def apply(colName: String): String
	
}

trait TextTable extends FlowSource[TextTableRow]{

	def columnNames: Seq[String]
	
}



