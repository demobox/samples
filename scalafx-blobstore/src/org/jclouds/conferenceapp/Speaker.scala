package org.jclouds.conferenceapp

import ru.circumflex.orm._
import ru.circumflex.orm.Record

class Speaker extends Record[String, Speaker] {
  val id = "id".VARCHAR(255).NOT_NULL
  val company = "company".VARCHAR(255)
  val firstName = "firstName".VARCHAR(255)
  val jobTitle = "jobTitle".VARCHAR(255)
  val lastName = "lastName".VARCHAR(255)

  def PRIMARY_KEY = id
  def relation = Speaker
}
object Speaker extends Speaker with Table[String, Speaker]
