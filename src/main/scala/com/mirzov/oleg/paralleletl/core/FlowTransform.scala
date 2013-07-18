package com.mirzov.oleg.paralleletl.core

import play.api.libs.iteratee.Enumeratee
import play.api.libs.iteratee.Enumerator

trait FlowTransform[S,T] extends FlowSource[T]{
  
	def original: FlowSource[S]
	protected[this] def transform: Enumeratee[S,T]
	
	final override def enum: Enumerator[T] = original.enum &> transform
	
}

object FlowTransform{

	def map[S,T](source: FlowSource[S], map: S => T): FlowSource[T] = 
			new MappingFlowTransform(source, map)
}

private class MappingFlowTransform[S,T](override val original: FlowSource[S], map: S => T)
												extends FlowTransform[S,T]{
	import scala.language.reflectiveCalls
	final override def transform = Enumeratee.map(map)
}
