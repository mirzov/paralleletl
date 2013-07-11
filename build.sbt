name := "ParallelEtl"

version := "0.1"

scalaVersion := "2.10.2"

scalacOptions += "-feature"

libraryDependencies ++= Seq(
	"org.scalatest" %% "scalatest" % "1.9.1" % "test",
	"net.sf.opencsv" % "opencsv" % "2.3",
	"play" % "play-iteratees_2.10" % "2.1.2"
)

