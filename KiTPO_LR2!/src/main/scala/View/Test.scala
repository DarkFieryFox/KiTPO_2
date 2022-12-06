package git.group
package View

import Builder.{Builder, BuilderGPS, BuilderInteger}
import List.{DoIt, TList}

import scala.sys.exit

class Test {
  private var builder:Builder = null
  private var list:TList = null
  private var flag_menu:Boolean = true
  private val os:String = System.getProperty("os.name")

  def toBuilder(name:String):Boolean = {
    try {
      builder = settingBuilder(name)
    }catch {
      case e:Exception => e.printStackTrace()
        return false
    }
    list = new TList(builder)
    true
  }



  private def settingBuilder(name:String):Builder = {
    if (name.equals(BuilderGPS.getName)) {
      return new BuilderGPS
    }else if (name.equals(BuilderInteger.getName)){
      return new BuilderInteger()
    } else {
      var e:Exception = new Exception("Oшибка: нет такого типа")
      throw e
    }
  }


  def run() = {
    if (flag_menu) {
      val elem:Int = 5
      testInt(elem)
      testGPS(elem)


    }
  }



  private def testInt(maxElement:Int): Unit ={

    System.out.print("\tТест Integer")
    try builder = settingBuilder("Integer")
    catch {
      case e: Exception =>
        e.printStackTrace()
    }
    var testlist:TList = new TList(builder)

    var i:Int = 0
    while (i  < maxElement){
      testlist.pushEnd(builder.createObject())
      i += 1
    }
    println("\nСгенерированый список")
    drawList(testlist)

    println("\nПоиск каждого второго эелемента")
    i = 0
    while (i < maxElement){
      println(testlist.find(i))
      i += 2
    }

    println("\nПроиизошла сортировка")
    testlist.sort
    drawList(testlist)

    testlist.clear()

    println("Список удален")
  }

  private def testGPS(maxElement:Int): Unit ={
    System.out.print("\tТест GPS")
    try builder = settingBuilder("GPS")
    var testlist:TList = new TList(builder)

    var i:Int = 0
    while (i  < maxElement){
      testlist.pushEnd(builder.createObject())
      i += 1
    }
    println("\nСгенерированый список")
    drawList(testlist)

    println("\nПоиск каждого второго эелемента")
    i = 0
    while (i < maxElement){
      println(testlist.find(i))
      i += 2
    }

    println("\nПроиизошла сортировка")
    testlist.sort
    drawList(testlist)

    testlist.clear()

    println("Список удален")
  }


  private def drawList(otherlist:TList): Unit ={
    otherlist.forEach(new DoIt {
      override def doIt(o: Any): Unit = println(o.toString())
    })
  }


}
