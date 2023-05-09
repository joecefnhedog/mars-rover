package com.thrillpool.navigation.representation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import com.thrillpool.navigation.representation.CoordinatesSystem.{
  Bounds,
  Xaxis
}

class CoordinateSystemSpec extends AnyFlatSpec with Matchers {

  "Xaxis" should "modify the coordinate based on operation" in new CoordinateTestScope {

    val xaxisIncrement = xAxis.increment

    xaxisIncrement.value shouldBe 4

    val xaxisDecrement = xAxis.decrement

    xaxisDecrement.value shouldBe 2

  }

  trait CoordinateTestScope {

    implicit val bounding: Bounds = new Bounds {
      override val upperBound: Int = 6
      override val lowerBound: Int = 1
    }
    val xAxis = Xaxis(3)
  }

}
