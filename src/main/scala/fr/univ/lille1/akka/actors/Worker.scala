package fr.univ.lille1.akka.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.collection.mutable

object Worker {
  def props(char: String, masterActor: ActorRef): Props = Props(new Worker(char, masterActor))
}

class Worker(char: String, masterActor: ActorRef) extends Actor with ActorLogging {
  println("Worker start")

  def receive = {
    case line: String => {
      val lineNumberOfOccurences = line.count(_ == this.char)

      //log.info(s"The number of '${this.char}' in ${line} is ${lineNumberOfOccurences}")

      val counts = mutable.Map.empty[String, Int].withDefaultValue(0)
      for (rawWord <- line.split("[ ,!.]+")) {
        val word = rawWord.toLowerCase
        counts(word) += 1
      }

      masterActor ! lineNumberOfOccurences
    }
  }
}