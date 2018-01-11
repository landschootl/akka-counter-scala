package fr.univ.lille1.akka.actors

import akka.actor.{Actor, ActorLogging}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

import scala.io.Source

class Master extends Actor with ActorLogging {
  val OCCURENCE_TO_FIND = 'a'

  var filename = ""
  var numberOfOccurences = 0
  var numberOfLines = 0
  var numberOfResponses = 0

  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Worker.props(OCCURENCE_TO_FIND, self))
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
        log.info(s"Il y a au total ${this.numberOfOccurences} fois '${OCCURENCE_TO_FIND}' dans le fichier ${filename}")
      }
    }
  }
}