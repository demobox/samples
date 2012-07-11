package org.jclouds.conferenceapp

import collection.JavaConversions._
import scalafx.collections.ObservableBuffer
import ru.circumflex.orm.TextField

object ConferenceModel {
  val filteredSpeakers = ObservableBuffer[Speaker]()

  def loadLocal() {
    ZipUtility.getZipFiles(getClass.getResourceAsStream("conferenceData.zip"), "")
  }

  def loadCloud() {
    val cloudStream = JcloudsUtility.loadFromCloud("com.steveonjava.conferencedata", "conferenceData.zip")
    ZipUtility.getZipFiles(cloudStream, "")
    JcloudsUtility.close()
  }

  def load() {
    loadLocal()
    clear()
  }

  def clear() {
    val speakers = Speaker.criteria.list()
    filteredSpeakers.setAll(speakers)
  }

  def filter(field: TextField[Speaker], filterString: String) {
    val speakers = Speaker.criteria.add(field LIKE "%" + filterString + "%").list()
    filteredSpeakers.setAll(speakers)
  }

  load()
}
