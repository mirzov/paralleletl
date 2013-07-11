package com.mirzov.oleg.paralleletl.core

import play.api.libs.iteratee.Enumerator
import scala.concurrent.ExecutionContext

class TraversableBasedFlow[T]
						(t: TraversableOnce[T])
						(implicit exc: ExecutionContext)
						extends FlowSource[T] {
  
	final override val enum: Enumerator[T] = Enumerator.enumerate(t)
	
}