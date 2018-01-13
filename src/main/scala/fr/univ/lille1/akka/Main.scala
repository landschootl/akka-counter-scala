package fr.univ.lille1.akka

import akka.actor.{ActorRef, ActorSystem, Props}
import fr.univ.lille1.akka.actors._
import scala.io.StdIn

object Main extends App {

  println("[INFO] Création du système : akka-counter-scala")
  val system: ActorSystem = ActorSystem("akka-counter-scala")

  var textFiles = Map(1 -> "fra_mixed_2009_10K-sentences.txt", 2 -> "small-text.txt")
  var choiceTextFile: Int = 1
  var typeCounter: Int = 1
  var occurenceToFind: String = null
  var numberOfWorkers: Int = 0

  println("[MENU] Bienvenue dans le menu")
  try {
    println("Que souhaitez-vous faire ? (default 1)")
    println("\t1) Compter le nombre d'occurence de chacun des mots")
    println("\t2) Compter le nombre d'occurence d'un mot en particulier")
    this.typeCounter = StdIn.readInt()
    if(this.typeCounter != 1 && this.typeCounter != 2)
      throw new NumberFormatException
  } catch {
    case e: NumberFormatException => {
      println("[ERROR] Valeur incorrect, nous avons pris le choix par default")
      this.typeCounter = 1
    };
  }
  if(typeCounter == 2){
    println("Vous souhaitez le nombre d'occurence de quel mot ?")
    this.occurenceToFind = StdIn.readLine()
  }
  try {
    println("Quel texte voulez-vous analyser ? (default 1)")
    for ((key,value) <- textFiles) printf("\t%s) %s\n", key, value)
    this.choiceTextFile = StdIn.readInt()
    if(this.choiceTextFile < 1 || this.choiceTextFile > textFiles.size)
      throw new NumberFormatException
  } catch {
    case e: NumberFormatException => {
      println("[ERROR] Valeur incorrect, nous avons pris le choix par default")
      this.choiceTextFile = 1
    };
  }
  try {
    println("Combien de worker voulez-vous (default 5) ?")
    this.numberOfWorkers = StdIn.readInt()
  } catch {
    case e: NumberFormatException => {
      println("[ERROR] Valeur incorrect, nous avons pris le choix par default")
      this.numberOfWorkers = 5
    };
  }

  println("[INFO] Création & configuration du master")
  val master: ActorRef = system.actorOf(Props(new Master(this.numberOfWorkers, occurenceToFind)))

  master ! textFiles(choiceTextFile)
}
