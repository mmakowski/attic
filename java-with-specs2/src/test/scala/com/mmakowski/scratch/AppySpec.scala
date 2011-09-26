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
    "know its name"           ! name   ^
                                     end
    
  def scared = (new Appy).scared === true
  def name = (new Appy).name === "Appy"
}