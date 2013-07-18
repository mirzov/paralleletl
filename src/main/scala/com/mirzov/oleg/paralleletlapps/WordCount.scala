package com.mirzov.oleg.paralleletlapps

import scala.concurrent.Future
import com.mirzov.oleg.paralleletl.core.FlowTransform
import com.mirzov.oleg.paralleletl.core.FlowSource
import com.mirzov.oleg.paralleletl.util.FlowSinks
import scala.concurrent.ExecutionContext

object WordCount {
  
	def nWords(s: String): Int = s.split("\\s+").map(_.trim).count(_.length > 0)
	
	def apply(src: FlowSource[String], threads: Int = 1)(implicit exc: ExecutionContext): Future[Long] = {
		val lineEnums = src.distribute(threads).map(enum => FlowTransform.map(enum, nWords))
		val lineFutures = lineEnums.map(_ |>>> FlowSinks.summing)
		val linesFuture = Future.sequence(lineFutures)
		linesFuture.map(_.sum)
	}
}