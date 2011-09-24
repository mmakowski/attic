package com.mmakowski.scratch

import org.specs2._
import org.junit.runner._
import runner._

@RunWith(classOf[JUnitRunner])
class AppySpec extends Specification { def is =
  "Appy specification"                 ^
                                      p^
  "Appy should"                        ^
    "be scared"               ! scared ^
                                     end
    
  def scared = (new Appy).scared === true
}