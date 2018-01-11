package fr.univ.lille1.akka

import akka.actor.{ActorRef, ActorSystem}
import fr.univ.lille1.akka.actors.Greeter._
import fr.univ.lille1.akka.actors._

import scala.io.Source

object Main extends App {

  val system: ActorSystem = ActorSystem("helloAkka")

  val printer: ActorRef = system.actorOf(Printer.props, "printerActor")

  val howdyGreeter: ActorRef =
    system.actorOf(Greeter.props("Howdy", printer), "howdyGreeter")
  val helloGreeter: ActorRef =
    system.actorOf(Greeter.props("Hello", printer), "helloGreeter")
  val goodDayGreeter: ActorRef =
    system.actorOf(Greeter.props("Good day", printer), "goodDayGreeter")

  println(Source.fromResource("fra_mixed_2009_10K-sentences.txt").getLines)

  Source.fromResource("fra_mixed_2009_10K-sentences.txt").getLines.foreach(line => println(line))

  howdyGreeter ! WhoToGreet("Akka")
  howdyGreeter ! Greet

  howdyGreeter ! WhoToGreet("Lightbend")
  howdyGreeter ! Greet

  helloGreeter ! WhoToGreet("Scala")
  helloGreeter ! Greet

  goodDayGreeter ! WhoToGreet("Play")
  goodDayGreeter ! Greet
}
