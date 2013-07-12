package com.mirzov.oleg.paralleletl.texttables.transforms

import com.mirzov.oleg.paralleletl.texttables.TextTable
import com.mirzov.oleg.paralleletl.core.LeftJoinFlowSource
import scala.concurrent.ExecutionContext
import com.mirzov.oleg.paralleletl.texttables.TextTableRow
import LeftJoin._
import com.mirzov.oleg.paralleletl.texttables.ArrayTableRow


class LeftJoin(left: TextTable, right: TextTable,
		leftKeyCols: Seq[String], rightKeyCols: Seq[String])
		(implicit exc: ExecutionContext)
		extends TextTableLeftJoinFlowSource(left, right) with TextTable{
  
	private def getIndices(tbl: TextTable, keyCols: Seq[String]): Seq[Int] = 
		tbl.columnNames.zipWithIndex.filter(tpl => keyCols.contains(tpl._1)).map(_._2)
		
	private val leftIndices = getIndices(left, leftKeyCols)
	private val rightIndices = getIndices(right, rightKeyCols)
	
	def leftKey(l: TextTableRow): Seq[String] = leftIndices.map(l(_))
	def rightKey(r: TextTableRow): Seq[String] = rightIndices.map(r(_))
	
	private val leftColsSet: Set[String] = Set(left.columnNames: _*)
	private val usedRightCols = right.columnNames.filter(cn => !leftColsSet.contains(cn))
	private val nOfUsedRightCols = usedRightCols.length
	val columnNames: Seq[String] = left.columnNames ++: usedRightCols
	
	private val colToIndexLookup: Map[String, Int] = columnNames.zipWithIndex.toMap
	private val usedRightIndices = getIndices(right, usedRightCols)
	
	def join(l: TextTableRow, rs: Iterable[TextTableRow]): Iterable[TextTableRow] = {
		if(rs.isEmpty){
			val rowStrings = (l ++ Seq.fill(nOfUsedRightCols)(emptyCellValue)).toArray
			Seq(new ArrayTableRow(rowStrings, colToIndexLookup))
		}else rs.map(r => {
			val rowStrings = (l ++ usedRightIndices.map(r(_))).toArray
			new ArrayTableRow(rowStrings, colToIndexLookup)
		})
	}
	
}

object LeftJoin{
	val emptyCellValue = ""
  
	type TextTableLeftJoinFlowSource = LeftJoinFlowSource[TextTableRow, TextTableRow, Seq[String], TextTableRow] 
}
