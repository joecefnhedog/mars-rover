package com.thrillpool.navigation

import com.thrillpool.MarsRover
import com.thrillpool.navigation.representation.CoordinatesSystem.{
  Bounds,
  Xaxis,
  Yaxis
}
import com.thrillpool.navigation.representation.{GridCoordinate, North}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class OperationSpec extends AnyFlatSpec with Matchers {

  it should "use the operations retrieved from the grid search to navigate to the end" in new OperationScope {

    val start = MarsRover(GridCoordinate(Xaxis(2), Yaxis(5)), North)
    val end = MarsRover(GridCoordinate(Xaxis(4), Yaxis(3)), North)

    val forbidden = List(
      GridCoordinate(Xaxis(3), Yaxis(2)),
      GridCoordinate(Xaxis(3), Yaxis(3)),
      GridCoordinate(Xaxis(3), Yaxis(4)),
      GridCoordinate(Xaxis(3), Yaxis(5)),
      GridCoordinate(Xaxis(3), Yaxis(6))
    )

    val Right(pathTaken) = AutoPilot
      .routeBetweenGridPoints(start, end.gridPosition, forbidden)

    val squaresVisited = pathTaken.path.value.map(_.rover.gridPosition)

    forbidden.foreach(forbiddenPoint =>
      squaresVisited shouldNot contain(forbiddenPoint)
    )

    val operations = pathTaken.path.value.flatMap(_.operations)

    Operation
      .performOperations(operations.reverse, start)
      .gridPosition shouldBe end.gridPosition

  }

  trait OperationScope {
    implicit val bounding: Bounds = {
      new Bounds {
        override val upperBound: Int = 6
        override val lowerBound: Int = 1
      }
    }
  }
}
