package paralleletlapps

import java.util.concurrent.Executors

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

import org.scalatest.FunSuite

import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee

class IterateesConcurrenceTest extends FunSuite{
  
	test("Parrallel Concurrent.broadcast"){
	  
		//Fixing thread pool size for better reproducibility
		val exServ = Executors.newFixedThreadPool(2)
		implicit val exc: ExecutionContext = ExecutionContext.fromExecutorService(exServ)
	  
		val (enum1, ch1) = Concurrent.broadcast[Int]
		val (enum2, ch2) = Concurrent.broadcast[Int]
		
		val pushIter1 = Iteratee.foreach[Int](i => ch1.push(i))
		val pushIter2 = Iteratee.foreach[Int](i => ch2.push(i))
		
		val N = 10
		val srcEnum = Enumerator.enumerate(1 to N)
		
		(srcEnum |>>> pushIter1).onComplete(_ => ch1.eofAndEnd())
		(srcEnum |>>> pushIter2).onComplete(_ => ch2.eofAndEnd())
		
		val listIter = Iteratee.getChunks[Int]
		
		val list1 = Await.result(enum1 |>>> listIter, Duration.Inf)
		val list2 = Await.result(enum2 |>>> listIter, Duration.Inf)
		
		assert(list1 === (1 to N).toList, "First list is wrong")
		assert(list2 === (1 to N).toList, "Second list is wrong")
	}
}