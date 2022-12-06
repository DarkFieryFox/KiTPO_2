package git.group
package Comaparator

import Builder.BuilderGPS

class ComparatorGPS extends Comparator with Serializable {
  override def compare(o1: Any, o2: Any): Int =
    (o1.asInstanceOf[BuilderGPS].latitude - o2.asInstanceOf[BuilderGPS].latitude).toInt

}


