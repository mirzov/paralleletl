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

class TsvDataTable private(ioReader: () => Reader)(implicit val exeCtxt: ExecutionContext)
						extends ArrayTextTable {

	private def getCsvReader = new CSVReader(ioReader(), '\t')
	private def getCsvHeader(reader: CSVReader): Seq[String] = {
		val header = reader.readNext
		assert(header != null, "No table header in a TSV data stream")
		header
	}
  
	private[this] val reader = getCsvReader
	
	override val columnNames: Seq[String] = try{
		getCsvHeader(reader)
	}finally{
		reader.close()
	}
	
	final override protected def arrays: Stream[Array[String]] = {
		val reader = getCsvReader
		getCsvHeader(reader)
	
		def getRows: Stream[Array[String]] = {
			val row = reader.readNext()
			if(row == null) {
				reader.close()
				Stream.empty 
			}
			else Stream.cons(row, getRows)
		}
		getRows
	}
	
}

object TsvDataTable{
  
	def apply(file: File)(implicit ctxt: ExecutionContext) =
		new TsvDataTable(() => new BufferedReader(new FileReader(file)))
  
	def apply(stream: () => InputStream)(implicit ctxt: ExecutionContext) =
		new TsvDataTable(() => new InputStreamReader(stream()))
	
	def apply(str: String)(implicit ctxt: ExecutionContext) =
		new TsvDataTable(() => new StringReader(str))
}

