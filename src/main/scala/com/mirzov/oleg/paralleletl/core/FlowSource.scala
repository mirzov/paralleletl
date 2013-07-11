package com.mirzov.oleg.paralleletl.core

import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import scala.concurrent.Future
import play.api.libs.iteratee.Enumeratee

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
}


