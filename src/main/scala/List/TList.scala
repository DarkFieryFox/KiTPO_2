package git.group
package List

import Builder.Builder
import Comaparator.Comparator

import java.util.function.Consumer

class TList(var builder:Builder) extends Serializable
{
  class Node(data1:Any) extends Serializable
  {
    var data:Any = data1
    var next:Node = null
  }

  private var head:Node = null
  private var tail:Node = null
  private var size:Int = 0
  private var comparator:Comparator = builder.getComparator

  def getBuilder:Builder = builder

  def setBuilder(builder: Builder):Boolean = {
    if (size == 0){
      this.builder = builder
      this.comparator = builder.getComparator
      return true
    }
    false
  }

  def pushFront(data:Any):Boolean =
  {
    var nNode:Node = new Node(data)

    if (head == null)
    {
      head = nNode
      tail = nNode
    }
    else
    {
      val tmp:Node = head


      head = nNode
      head.next = tmp
    }
    size = size + 1

    true
  }

  def pushEnd(data:Any):Boolean =
  {
    val nNode:Node = new Node(data)

    if (head == null){
      head = nNode
      tail = nNode
    }
    else {
      tail.next = nNode
      tail = nNode
    }
    size = size + 1
    true
  }

  private def pushEndd(toInsert: TList): Unit = {
    if (toInsert != null) {
      tail.next = toInsert.head.asInstanceOf[Node]
      tail = toInsert.tail.asInstanceOf[Node]
      size += toInsert.size
    }
  }

  def add(data:Any, index:Int):Boolean =
  {
    if (index == 0){
      return pushFront(data)
    }
    val nNode:Node = new Node(data)

    if (head == null) {
      head = nNode
      tail = nNode
    }
    else {
      var tmp: Node = head
      var current: Node = null
      var n: Int = 0
      while (n < index) {
        current = tmp
        tmp = tmp.next
        n = n + 1
      }
      current.next = nNode
      nNode.next = tmp
    }
    size = size + 1
    true
  }

  def delete(index:Int):Boolean = {
    if (index < 0) {
      return false
    }
    var toDel: Node = null
    var toDelPrev: Node = null

    if (head == null) {
      println("List is empty")
      return false
    }
    else {
      if (head != tail) {
        if (index > 0) {
          toDelPrev = findNode(index-1)//-1?
          toDel = toDelPrev.next
        }
        else toDel = head

        if (toDelPrev != null) {
          toDelPrev.next = toDel.next
          toDel = null
          if(toDelPrev.next == null)
            tail = toDelPrev
        }
        else {
          head = toDel.next
          toDel = null
        }

      }
      else {
        head = null
        tail = null
      }

    }
    size = size - 1
    true
  }



  private def findNode(id:Int):Node = {
    var res:Node = head
    var n:Int = 0
    while (n < id){
      res = res.next
      n = n + 1
    }
    res
  }

  def find(index:Int):Any = {
    var current:Node = head
    var dataNode:Any = current.data
    if (index == 0){
      dataNode = current.data
      return dataNode
    }
    var n:Int = 0
    while (n < index && current.next != null){
      current = current.next
      n = n + 1
    }
    dataNode = current.data
    dataNode
  }

  def finds(obj:Any):Int = {
    var current:Node = head
    var index:Int = 0
    if (head == null){
      return -1
    }
    else {
      while (current != null){
        if (current.data == obj){
          return index
        }
        index = index + 1
        current = current.next
      }
    }
    -1
  }

  def find_quantity(obj:Any):Int = {
    var current:Node = head
    var quantity:Int = 0
    if (head == null){
      return -1
    }
    else {
      while (current != null){
        if (current.data == obj){
          quantity = quantity+1
        }
        current = current.next
      }
      return quantity
    }

    -1
  }



  def sort2(xs: TList): TList ={
    if(xs.size <= 1) xs
    else{
      val pivot = xs.find(size/2)
      concat(
        sort2(xs.filter(x => comparator.compare(pivot, x) > 0)),
        xs.filter(x => comparator.compare(pivot, x) == 0),
        sort2(xs.filter(x => comparator.compare(pivot, x) < 0)),
      )
    }
  }

  def sort(): TList ={
    return sort2(this)
  }

  def forEach(vall:DoIt) = {
    var i:Int = 0
    var cur:Node = head
    while (i < size)
    {
      vall.doIt(cur.data)
      cur = cur.next
      i += 1
    }
  }

  def getSize:Int = size

  def clear():Boolean=
  {
    if (head == null)
      return false

    while (head != null) delete(0)
    true
  }

  def concat(l1 : TList, l2 : TList, l3 : TList) : TList =
  {
    val l = new TList(builder)
    l1.forEach(x => l.pushEnd(x));
    l2.forEach(x => l.pushEnd(x));
    l3.forEach(x => l.pushEnd(x));
    return l;
  }

  def filter(typeToBoolean: Any => Boolean): TList = {
    val l1 = new TList(builder)
    this.forEach((x) => {
      if(typeToBoolean(x))
      {
        l1.pushEnd(x)
      }
    })
    return l1
  }

}
