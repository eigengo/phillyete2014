package org.eigengo.phillyete.core

import scala.io.Source

trait SentimentSets {
  def positiveWords: Set[String]
  def negativeWords: Set[String]
}

trait CSVLoadedSentimentSets extends SentimentSets {
  lazy val positiveWords = loadWords("/positive_words.csv")
  lazy val negativeWords = loadWords("/negative_words.csv")

  private def loadWords(fileName: String): Set[String] = {
    Source.
      fromInputStream(getClass.getResourceAsStream(fileName)).
      getLines().
      map(line => line.split(",")(1)).
      toSet
  }
}

class SentimentAnalysis {
  this: SentimentSets =>

  private val counts = collection.mutable.Map[String, Int]()
  private val languages = collection.mutable.Map[String, Int]()
  private val places = collection.mutable.Map[String, Int]()

  private def update(data: collection.mutable.Map[String, Int])(category: String, delta: Int): Unit = data.put(category, data.getOrElse(category, 0) + delta)
  val updateCounts = update(counts)_
  val updateLanguages = update(languages)_
  val updatePlaces = update(places)_

  def onTweet(tweet: Tweet): Map[String, Map[String, Int]] = {
    val positive: Int = if (positiveWords.exists(word => tweet.text.toLowerCase contains word)) 1 else 0
    val negative: Int = if (negativeWords.exists(word => tweet.text.toLowerCase contains word)) 1 else 0

    updateCounts("positive", positive)
    updateCounts("negative", negative)
    if (tweet.user.followersCount > 200) {
      updateCounts("positive.gurus", positive)
      updateCounts("negative.gurus", negative)
    }

    updateLanguages(tweet.user.lang, 1)
    updatePlaces(tweet.place.toString, 1)

    Map("counts" -> counts.toMap, "languages" -> languages.toMap, "places" -> places.toMap)
  }
}