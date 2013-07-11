package com.mirzov.oleg.paralleletl.core

import play.api.libs.iteratee.Enumerator

class InterleavedFlowSource[T](originals: FlowSource[T]*) extends FlowSource[T] {
	final override val enum: Enumerator[T] = Enumerator.interleave(originals.map(_.enum))
}