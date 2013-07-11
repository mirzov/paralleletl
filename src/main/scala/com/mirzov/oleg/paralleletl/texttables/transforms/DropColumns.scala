package com.mirzov.oleg.paralleletl.texttables.transforms

import scala.collection.Seq
import com.mirzov.oleg.paralleletl.texttables._

class DropColumns(val original: TextTable, columns: String*) extends TextTableTransform {
  
	override val columnNames: Seq[String] = original.columnNames.diff(columns)
	
	private val indicesToKeep = original.columnNames.zipWithIndex.collect{
	  								case (col, i) if !columns.contains(col) => i
	  							}.toArray
	  							
	private val colIndexLookup = columnNames.zipWithIndex.toMap
	
	final override def rowTransform(row: TextTableRow) = new ArrayTableRow(indicesToKeep.map(row(_)), colIndexLookup)
	
}