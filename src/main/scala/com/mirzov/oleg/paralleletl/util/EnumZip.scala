package com.mirzov.oleg.paralleletl.util

import play.api.libs.iteratee._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global


class EnumZip[A,B,C](ea: Enumerator[A], eb: Enumerator[B], f: (A,B) => C) extends Enumerator[C] {
	  
	def apply[T](i: Iteratee[C,T]): Future[Iteratee[C,T]] = for(
		aopt <- ea.run(Iteratee.head);
		bopt <- eb.run(Iteratee.head);
		res <- {
			val enumOpt = for(a <- aopt; b <- bopt) yield {
			  val eaTail = ea.through(Enumeratee.drop(1))
			  val ebTail = eb.through(Enumeratee.drop(1))
			  Enumerator(f(a,b)).andThen(new EnumZip(eaTail, ebTail, f))
			}
			enumOpt.getOrElse(Enumerator.eof).apply(i)
		}
	) yield res
	
}
