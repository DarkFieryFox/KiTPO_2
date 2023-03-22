package git.group.List

import git.group.Builder.Builder
import git.group.Comparator.Comparator
import java.io.Serializable
import java.util.*

class TList : Serializable {
    inner class Node(var data: Any) : Serializable {
        var next: Node? = null
    }

    private var head: Node? = null
    private var tail: Node? = null
    private var size = 0
    var builder: Builder? = null
        private set
    private var comparator: Comparator? = null

    constructor(builder: Builder?) {
        this.builder = builder

            comparator = builder?.comparator

        head = null
        tail = null
        size = 0
    }

    constructor(arr: Array<Builder?>) {
        for (i in arr.indices) {
            pushEnd(arr[i])
        }
    }

    fun clear(): Boolean {
        if (head == null) {
            return false
        }
        while (head != null) {
            delete(0)
        }
        return true
    }

    fun pushFront(obj: Any?): Boolean {
        val nNode: Node = Node(obj!!)
        if (head == null) {
            head = nNode
            tail = nNode
        } else {
            val temp: Node = head as Node
            head = nNode
            head!!.next = temp
        }
        size++
        return true
    }

    fun pushEnd(data: Any?): Boolean {
        val nNode: Node = Node(data!!)
        if (head == null) {
            head = nNode
            tail = nNode
        } else {
            tail!!.next = nNode
            tail = tail!!.next
        }
        size++
        return true
    }

    private fun pushEnd(toInsert: TList?) {
        if (toInsert != null) {
            tail!!.next = toInsert.head
            tail = toInsert.tail
            size += toInsert.size
        }
    }

    fun add(data: Any?, index: Int): Boolean {
        val nNode: Node = Node(data!!)
        if (head == null) {
            head = nNode
            tail = nNode
        } else {
            var temp: Node?
            var current: Node?
            temp = head
            current = null
            for (i in 0 until index) {
                current = temp
                temp = temp!!.next
            }
            current!!.next = nNode
            nNode.next = temp
        }
        size++
        return true
    }

    fun delete(index: Int): Boolean {
        if (size < 0 || index < 0) {
            return false
        }
        var toDel: Node?
        var toDelPrev: Node? = null
        if (head == null) {
            println("Список пуст")
            return false
        } else {
            if (head !== tail) {
                if (index > 0) {
                    toDelPrev = findNode(index - 1)
                    toDel = toDelPrev!!.next
                } else {
                    toDel = head
                }
                if (toDelPrev != null) {
                    toDelPrev.next = toDel!!.next
                    toDel = null
                } else {
                    head = toDel!!.next
                    toDel = null
                }
            } else {
                tail = null
                head = tail
            }
        }
        size--
        return true
    }

    fun find(index: Int): Any {
        val dataNode: Any
        var current = head
        if (index == 0) {
            dataNode = current!!.data
            return dataNode
        }
        for (i in 0 until index) {
            current = current!!.next
        }
        dataNode = current!!.data
        return dataNode
    }

    fun forEach(action: DoIt) {
        var i:Int = 0
        var cur:Node? = head
        while (i < size)
        {
            action.doIt(cur?.data.toString() + " ")
            cur = cur?.next
            i += 1
        }
    }

    fun sort(comparator: Comparator?) {
        head = quicksort(head, comparator)
    }


    fun quicksort(p3:Node?, comparator: Comparator?): Node? {
        var m:Node?
        var q:Node?
        var p1:Node?
        var p2:Node?
        var p:Node? = p3

        if(p==null || p.next == null)
        {
            return p
        }
        m = p
        p = p.next!!
        p1 = null
        p2 = null
        while(p != null)
        {
            q = p
            p = p.next
            var comp:Int = comparator!!.compare(q.data, m.data)
            if(comp < 0 || comp == 0)
            {
                q.next = p1
                p1 = q
            }
            else
            {
                q.next = p2
                p2 = q
            }
        }

        p1 = quicksort(p1, comparator)
        p2 = quicksort(p2, comparator)

        m.next = p2
        if (p1 == null)
        {
            return m
        }
        q = p1
        while (q!!.next != null) q = q.next
        q.next = m
        return p1
    }

    private fun findNode(id: Int): Node? {
        var res = head
        for (i in 0 until id) {
            res = res!!.next
        }
        return res
    }

    override fun toString(): String {
        var cur = head
        var str = ""
        for (i in 0 until size) {
            str += cur!!.data.toString()
            str += "\n"
            cur = cur.next
        }
        return str
    }
}