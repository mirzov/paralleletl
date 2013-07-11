package com.mirzov.oleg.paralleletl.texttables

class ArrayTableRow(row: Array[String], colIndexLookup: Map[String, Int]) extends TextTableRow{
	override def length = row.length
	def apply(i: Int) = row(i)
	def apply(colName: String) = row(colIndexLookup(colName))
}
