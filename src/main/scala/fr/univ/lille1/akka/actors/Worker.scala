package fr.univ.lille1.akka.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object Worker {
  def props(char: Char, masterActor: ActorRef): Props = Props(new Worker(char, masterActor))
}

class Worker(char: Char, masterActor: ActorRef) extends Actor with ActorLogging {
  def receive = {
    case line: String => {
      val lineNumberOfOccurences = line.count(_ == this.char)

      //log.info(s"The number of '${this.char}' in ${line} is ${lineNumberOfOccurences}")

      masterActor ! lineNumberOfOccurences
    }
  }
}