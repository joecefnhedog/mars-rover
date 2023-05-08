package com.thrillpool.navigation

import com.thrillpool.MarsRover
import com.thrillpool.navigation.AutoPilot.NoRoutesToDestination
import com.thrillpool.navigation.representation.North
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import com.thrillpool.navigation.representation.GridCoordinate
import com.thrillpool.navigation.representation.CoordinatesSystem.{
  OutOfBoundsError,
  Xaxis,
  Yaxis
}

class AutoPilotSpec extends AnyFlatSpec with Matchers {

  "AutoPilot" should "navigate between start and end grid point,  while avoiding forbidden points" in {
    val start = MarsRover(2, 5, North)
    val end = MarsRover(4, 3, North)

    val forbidden = List(
      GridCoordinate(Xaxis(3), Yaxis(2)),
      GridCoordinate(Xaxis(3), Yaxis(3)),
      GridCoordinate(Xaxis(3), Yaxis(4)),
      GridCoordinate(Xaxis(3), Yaxis(5)),
      GridCoordinate(Xaxis(3), Yaxis(6))
    )

    val Right(pathTaken) = AutoPilot
      .routeBetweenGridPoints(start, end.gridPosition, forbidden)

    forbidden.foreach(forbiddenPoint =>
      pathTaken.path.value shouldNot contain(forbiddenPoint)
    )

    pathTaken.lastGrid.gridPosition shouldBe end.gridPosition

  }

  it should "fail to process an out of bounds input" in {
    val start = MarsRover(200, 1, North)
    val end = MarsRover(1, 1, North)

    val Left(error) = AutoPilot
      .routeBetweenGridPoints(start, end.gridPosition, Nil)

    error shouldBe OutOfBoundsError(200, 6, 1)

  }

  it should "fail to find a suitable route" in {
    val start = MarsRover(4, 4, North)
    val end = MarsRover(1, 1, North)

    val forbidden = List(
      GridCoordinate(Xaxis(3), Yaxis(3)),
      GridCoordinate(Xaxis(3), Yaxis(4)),
      GridCoordinate(Xaxis(3), Yaxis(5)),
      GridCoordinate(Xaxis(4), Yaxis(3)),
      GridCoordinate(Xaxis(4), Yaxis(5)),
      GridCoordinate(Xaxis(5), Yaxis(3)),
      GridCoordinate(Xaxis(5), Yaxis(4)),
      GridCoordinate(Xaxis(5), Yaxis(5))
    )

    val Left(error) = AutoPilot
      .routeBetweenGridPoints(start, end.gridPosition, forbidden)

    error shouldBe NoRoutesToDestination(start.gridPosition, end.gridPosition)

  }

}
