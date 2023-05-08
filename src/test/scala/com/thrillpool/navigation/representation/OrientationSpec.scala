package com.thrillpool.navigation.representation
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class OrientationSpec extends AnyFlatSpec with Matchers {

  "North.clockwise" should "ensure clockwise operation obeys compass relationships" in {
    North.clockwise shouldBe East
    North.clockwise.clockwise shouldBe South
    North.clockwise.clockwise.clockwise shouldBe West
    North.clockwise.clockwise.clockwise.clockwise shouldBe North
  }

  "North.antiClockwise" should "ensure anticlockwise operation obeys compass relationships" in {
    North.antiClockwise shouldBe West
    North.antiClockwise.antiClockwise shouldBe South
    North.antiClockwise.antiClockwise.antiClockwise shouldBe East
    North.antiClockwise.antiClockwise.antiClockwise.antiClockwise shouldBe North
  }



  trait OrientationTestScope {
    val compassOrder = Set(North, East, South, West)
  }
}
