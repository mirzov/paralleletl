package com.mirzov.oleg.paralleletl.core

import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import scala.concurrent.Future
import play.api.libs.iteratee.Enumeratee
import scala.concurrent.ExecutionContext

trait FlowSource[T] { self =>

	protected[core] def enum: Enumerator[T]
	
	final def |>>>[R](sink: FlowSink[T,R]): Future[R] = enum |>>> sink.iter
	final def sendInto[R](sink: FlowSink[T,R]) = |>>>(sink)
	
	def filter(pred: T => Boolean): FlowSource[T] = new FlowTransform[T,T]{
		final override val original = self
		final override val transform = Enumeratee.filter(pred)
	}
	
	def >-(other: FlowSource[T]): FlowSource[T] = new InterleavedFlowSource(self, other)
	def interleaved(other: FlowSource[T]): FlowSource[T] = >-(other)
	
	def distribute(n: Int)(implicit exc: ExecutionContext): IndexedSeq[FlowSource[T]] = -<(n)
	def -<(n: Int)(implicit exc: ExecutionContext): IndexedSeq[FlowSource[T]] = 
								if(n == 1) Array(this) else DistributedFlowSource(this, n)
}

object FlowSource{
  
	def apply[T](enum: Enumerator[T]): FlowSource[T] =
		new EnumeratorBasedFlowSource(enum)
	
	def apply[T](t: TraversableOnce[T])(implicit exc: ExecutionContext): FlowSource[T] =
	  	new TraversableBasedFlowSource(t)
}

private class EnumeratorBasedFlowSource[T](override val enum: Enumerator[T]) extends FlowSource[T]

private class TraversableBasedFlowSource[T](t: TraversableOnce[T])(implicit exc: ExecutionContext)
																		extends FlowSource[T] {
	final override val enum: Enumerator[T] = Enumerator.enumerate(t)
}
