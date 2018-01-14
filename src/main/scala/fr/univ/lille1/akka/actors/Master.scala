package fr.univ.lille1.akka.actors

import akka.actor.{Actor, ActorLogging}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

import scala.collection.mutable
import scala.io.Source

class Master(numberOfWorkers: Int, occurenceToFind : String) extends Actor with ActorLogging {

  var filename : String = ""
  var numberOfOccurences : Int = 0
  var numbersOfOccurences: mutable.Map[String, Int] = mutable.Map.empty[String, Int].withDefaultValue(0)
  var numberOfLines : Int = 0
  var numberOfResponses : Int = 0

  var router = {
    val routees = Vector.fill(numberOfWorkers) {
      var worker = WorkerAllWord.props(self)
      if(occurenceToFind != null){
        worker = WorkerOnlyWord.props(occurenceToFind, self)
      }
      val r = context.actorOf(worker)
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case filename: String => {
      this.filename = filename

      Source
        .fromResource(filename)
        .getLines
        .foreach(line => {
          router.route(line, sender())
        })

      numberOfLines = Source
        .fromResource(filename)
        .getLines
        .length
    }

    case number: Int => {
      numberOfOccurences += number
      numberOfResponses += 1

      if (numberOfResponses == numberOfLines) {
        println(s"Il y a au total ${this.numberOfOccurences} fois '${occurenceToFind}' dans le fichier ${filename}")
      }
    }

    case numbers: mutable.Map[String, Int] => {
      numberOfResponses += 1

      for ((key, value) <- numbers) {
        this.numbersOfOccurences(key) += value
      }

      if (numberOfResponses == numberOfLines) {
        println(this.numbersOfOccurences)
      }
    }
  }
}