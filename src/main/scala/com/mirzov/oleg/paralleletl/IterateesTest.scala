package com.mirzov.oleg.paralleletl

import play.api.libs.iteratee._
import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import core.TraversableBasedFlow
import core.FlowSink
import com.mirzov.oleg.paralleletl.core.FlowSource

object IterateesTest extends App{

	def slowSource[T](tr: TraversableOnce[T], delay: Int): FlowSource[T] =
		new TraversableBasedFlow(tr.map{t =>
			Thread.sleep(delay)
			println("Producing " + t)
			t
		})

	
	val source1 = slowSource(1 to 5, 100)
	val source2 = slowSource(100 to 110, 50)
	
	val sink = FlowSink.foreach[Int](i => println("Got " + i))
	val fut = source1 >- source2 |>>> sink
	Await.result(fut, Duration.Inf)
	println("Waiting for the result is successfuly over!")
}
