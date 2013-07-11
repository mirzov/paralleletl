package com.mirzov.oleg.paralleletl.core

import play.api.libs.iteratee.Iteratee

trait FlowSink[T, R]{
	protected[core] def iter: Iteratee[T, R]
}

object FlowSink{
	def foreach[T](pred: T => Unit) = new FlowSink[T, Unit]{
		final override val iter: Iteratee[T, Unit] = Iteratee.foreach(pred)
	} 
}

