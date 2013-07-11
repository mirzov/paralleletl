package com.mirzov.oleg.paralleletl.texttables

import java.io.File
import java.io.BufferedReader
import java.io.FileReader
import au.com.bytecode.opencsv.CSVReader
import scala.concurrent.ExecutionContext

class TsvDataTable(file: File)(implicit val exeCtxt: ExecutionContext) extends ArrayTextTable {

	private val ioReader = new BufferedReader(new FileReader(file))
	private val reader = new CSVReader(ioReader, '\t')
	
	override val columnNames: Seq[String] = reader.readNext().toSeq
	
	final override protected def arrays: Stream[Array[String]] = {
		val row = reader.readNext()
		if(row == null) {
			reader.close()
			Stream.empty 
		}
		else Stream.cons(row, arrays)
	}
	
}