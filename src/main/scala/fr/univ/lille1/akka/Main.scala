package fr.univ.lille1.akka

import akka.actor.{ActorRef, ActorSystem, Props}
import fr.univ.lille1.akka.actors._

object Main extends App {

  val system: ActorSystem = ActorSystem("akka-counter-scala")

  val master: ActorRef = system.actorOf(Props[Master])

  master ! "fra_mixed_2009_10K-sentences.txt"
  //master ! "small-text.txt"
}
