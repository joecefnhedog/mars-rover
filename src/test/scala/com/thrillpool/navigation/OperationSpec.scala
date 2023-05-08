package com.thrillpool.navigation

import com.thrillpool.Rover
import com.thrillpool.navigation.representation.CoordinatesSystem.{Xaxis, Yaxis}
import com.thrillpool.navigation.representation.{Grid, North}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class OperationSpec extends AnyFlatSpec with Matchers {

  it should "use the operations retrieved fromt the grid search to navigate to the end" in {

    val start = Rover(2, 5, North)
    val end = Rover(4, 3, North)

    val forbidden = List(
      Grid(Xaxis(3), Yaxis(2)),
      Grid(Xaxis(3), Yaxis(3)),
      Grid(Xaxis(3), Yaxis(4)),
      Grid(Xaxis(3), Yaxis(5)),
      Grid(Xaxis(3), Yaxis(6))
    )

    val Right(pathTaken) = AutoPilot
      .routeBetweenGridPoints(start, end.grid, forbidden)


    println(Operation.performOperations(pathTaken.path.operations.reverse,start))




  }


}
