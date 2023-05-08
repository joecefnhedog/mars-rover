package com.thrillpool.navigation.representation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import com.thrillpool.navigation.representation.CoordinatesSystem.Xaxis

class CoordinateSystemSpec extends AnyFlatSpec with Matchers {

  "Xaxis" should "modify the coordinate based on operation" in new CoordinateTestScope {

    val xaxisIncrement = xAxis.increment

    xaxisIncrement.value shouldBe 4

    val xaxisDecrement = xAxis.decrement

    xaxisDecrement.value shouldBe 2

  }



  trait CoordinateTestScope {
    val xAxis = Xaxis(3)
  }

}
