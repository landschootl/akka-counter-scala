package fr.univ.lille1.akka.actors

import akka.actor.{Actor, ActorLogging}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

import scala.io.Source

class Master(numberOfWorkers: Int, occurenceToFind : String) extends Actor with ActorLogging {

  var filename : String = ""
  var numberOfOccurences : Int = 0
  var numberOfLines : Int = 0
  var numberOfResponses : Int = 0

  var router = {
    val routees = Vector.fill(numberOfWorkers) {
      val r = context.actorOf(Worker.props(occurenceToFind, self))
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
  }
}