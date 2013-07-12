package com.mirzov.oleg.paralleletl.texttables

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.Reader
import java.io.StringReader
import scala.concurrent.ExecutionContext
import au.com.bytecode.opencsv.CSVReader
import java.io.InputStream
import java.io.InputStreamReader

class TsvDataTable(ioReader: Reader)(implicit val exeCtxt: ExecutionContext) extends ArrayTextTable {

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

object TsvDataTable{
  
	def apply(file: File)(implicit ctxt: ExecutionContext) =
		new TsvDataTable(new BufferedReader(new FileReader(file)))
  
	def apply(stream: InputStream)(implicit ctxt: ExecutionContext) =
		new TsvDataTable(new InputStreamReader(stream))
	
	def apply(str: String)(implicit ctxt: ExecutionContext) =
		new TsvDataTable(new BufferedReader(new StringReader(str)))
}

