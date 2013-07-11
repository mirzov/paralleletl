package com.mirzov.oleg.paralleletl.core

import play.api.libs.iteratee.Enumeratee
import play.api.libs.iteratee.Enumerator

trait FlowTransform[S,T] extends FlowSource[T]{
  
	def original: FlowSource[S]
	protected[this] def transform: Enumeratee[S,T]
	
	final override def enum: Enumerator[T] = original.enum &> transform
	
}