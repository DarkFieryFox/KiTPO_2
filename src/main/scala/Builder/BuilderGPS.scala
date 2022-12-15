package git.group
package Builder
import Comaparator.{Comparator, ComparatorGPS, ComparatorInteger}

import scala.util.Random


object BuilderGPS  {
  def getName:String = "GPS"

}
class BuilderGPS extends Builder with Serializable {
  val typename = new String("GPS")

  private val MAX = 100
  private val MIN = -100
  private val minHour = 0
  private val maxHour = 23
  private val minTime = 0
  private val maxTime = 59

  var latitude = .0 //шитрота

  var longitude = .0 //долгота

  var hour = 0
  var minute = 0
  var second = 0

  def this(latitude: Double, longitude: Double, hour: Int, minute: Int, second: Int) {
    this()
    this.latitude = latitude
    this.longitude = longitude
    this.hour = hour
    this.minute = minute
    this.second = second
  }

  def createObject: Any = {
    val rand = new Random
    val latitude = MIN + (MAX - MIN) * rand.nextDouble
    val longitude = MIN + (MAX - MIN) * rand.nextDouble
    val hour = rand.nextInt(maxHour - minHour)
    val minute = rand.nextInt(maxTime - minTime)
    val second = rand.nextInt(maxTime - minTime)
    val gps = new BuilderGPS(latitude, longitude, hour, minute, second)
    gps
  }

  override def parseObject(ss: String): Any = {
    val numStr = ss.split(";|:")
    latitude = numStr(0).toDouble
    longitude = numStr(1).toDouble
    hour = numStr(2).toInt
    minute = numStr(3).toInt
    second = numStr(4).toInt
    this
  }

  override def getComparator: Comparator = new ComparatorGPS()

  override def toString: String = latitude + ";" + longitude + ":" + hour + ":" + minute + ":" + second
  override def getName: String = BuilderGPS.getName
}