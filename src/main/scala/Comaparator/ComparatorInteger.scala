package git.group
package Comaparator

class ComparatorInteger extends Comparator with Serializable
{
  override def compare(o1: Any, o2: Any): Int =
  {
    if (o1.isInstanceOf[Int] && o2.isInstanceOf[Int])
    {
      val a:Int = o1.asInstanceOf[Int]
      val b:Int = o2.asInstanceOf[Int]
      return a.compareTo(b)
    }
    -2
  }
}
