package com.mirzov.oleg.paralleletl.core

import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.iteratee.Iteratee
import scala.concurrent.ExecutionContext

private object DistributedFlowSource {
  
	def apply[T](inner: FlowSource[T], n: Int)(implicit exc: ExecutionContext): IndexedSeq[FlowSource[T]] = {
	  
		val bcs = (1 to n).map(_ => Concurrent.broadcast[T])
		val enums: IndexedSeq[Enumerator[T]] = bcs.map(_._1)
		val channels: IndexedSeq[Channel[T]] = bcs.map(_._2)
		
		val nl = n.toLong
		val iter = Iteratee.fold[T, Long](0)((i,t) => {
			channels((i % nl).toInt).push(t)
			i + 1
		})
		
		val countFut = inner.enum |>>> iter
		countFut.onComplete(_ => channels.foreach(_.eofAndEnd()))
		
		enums.map(FlowSource(_))
	}
}