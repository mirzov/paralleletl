package com.mirzov.oleg.paralleletl.core

import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

abstract class LeftJoinFlowSource[L,R,K<:Equals,T](left: FlowSource[L], right: FlowSource[R])
									(implicit exc: ExecutionContext) extends FlowSource[T]{
	def leftKey(l: L): K
	def rightKey(r: R): K
	def join(l: L, rs: Iterable[R]): Iterable[T]
	
	final val enum: Enumerator[T] = {
	  
		val emptyMap = Map[K, Seq[R]]()
		
		val rightFlowFoldIteratee = Iteratee.fold(emptyMap){(map, r: R) => {
			val key = rightKey(r)
			if(map.contains(key)){
				val currSeq = map(key)
				map - key + ((key, currSeq :+ r))
			}else map + ((key, Seq(r)))
		}}
		
		val mapFuture: Future[Map[K,Seq[R]]] = right.enum |>>> rightFlowFoldIteratee
		
		val enumFuture: Future[Enumerator[T]] = mapFuture.map(map => left.enum.flatMap(l => {
			val key = leftKey(l)
			val rightSeq = map.get(key).getOrElse(Seq())
			Enumerator.enumerate(join(l, rightSeq))
		}))
		
		Enumerator.flatten(enumFuture)
	}
}
