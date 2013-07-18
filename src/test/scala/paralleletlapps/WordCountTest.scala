package paralleletlapps

import org.scalatest.FeatureSpec
import com.mirzov.oleg.paralleletlapps.WordCount
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Enumeratee
import paralleletl.texttables.TestHelp
import com.mirzov.oleg.paralleletl.core.FlowSource
import scala.concurrent.ExecutionContext.Implicits.global


class WordCountTest extends FeatureSpec{
	feature("Single-string word counting function"){
		import WordCount.nWords
		
		scenario("Two words with single space between"){
			assert(nWords("be me") === 2)
		}
		
		scenario("Six words with single/multiple spaces and tabs and their mixture between"){
			assert(nWords("be  me\t\tfe ke\tne\t ku") === 6)
		}
	}
	
	import TestHelp.await
	private def sc1(threads: Int) = scenario("Repeating same 3-word string 100 times"){
		val enum = Enumerator.repeat("word word word") &> Enumeratee.take(100)
		val count = WordCount(FlowSource(enum), threads)
		assert(await(count) === 300)
	}
	
	private def sc2(threads: Int) = scenario("Interleaving 5-word and 3-word strings"){
		val enum1 = Enumerator.repeat("word word word") &> Enumeratee.take(100)
		val enum2 = Enumerator.repeat("one two three four fixe") &> Enumeratee.take(100)
		val count = WordCount(FlowSource(enum1 >- enum2))
		assert(await(count) === 800)
	}
	
	feature("Counting words in a FlowSource[String] in a sequential fashion"){
		sc1(1)
		sc2(1)
	}
	
	feature("Counting words in a FlowSource[String] in a distributed fashion"){
		sc1(2)
		sc2(2)
	}
}