package fr.univ.lille1.akka.actors

import akka.actor.{Actor, ActorLogging, Props}
import fr.univ.lille1.akka.actors.Printer._

object Printer {
  def props: Props = Props[Printer]
  final case class Greeting(greeting: String)
}

class Printer extends Actor with ActorLogging {
  def receive = {
    case Greeting(greeting) =>
      log.info(s"Greeting received (from ${sender()}): $greeting")
  }
}
