package fr.univ.lille1.akka.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.collection.mutable

object WorkerAllWord {
  def props(masterActor: ActorRef): Props = Props(new WorkerAllWord(masterActor))
}

class WorkerAllWord(masterActor: ActorRef) extends Actor with ActorLogging {
  println("Worker all started")

  def receive = {
    case line: String => {
      val numbers: mutable.Map[String, Int] = mutable.Map.empty[String, Int].withDefaultValue(0)
      line.split("[ ,!.]+").foreach(rawWord => {
        val word = rawWord.toLowerCase
        numbers(word) += 1
      })
      masterActor ! numbers
    }
  }
}