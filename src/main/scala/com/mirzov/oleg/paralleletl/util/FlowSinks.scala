package com.mirzov.oleg.paralleletl.util

import com.mirzov.oleg.paralleletl.core.FlowSink
import play.api.libs.iteratee.Iteratee

object FlowSinks {
	def counting[T]: FlowSink[T, Long] = new CountingFlowSink
	def summing[T : Integral]: FlowSink[T, Long] = new SummingFlowSink
}

private class SummingFlowSink[T : Integral] extends FlowSink[T, Long] {
	val num = implicitly[Integral[T]]
	override val iter: Iteratee[T, Long] = Iteratee.fold[T, Long](0)((i,t) => i + num.toLong(t))
}

private class CountingFlowSink[T] extends FlowSink[T, Long] {
	override val iter: Iteratee[T, Long] = Iteratee.fold[T, Long](0)((i,t) => i + 1)
}