package com.thrillpool.navigation.representation

import com.thrillpool.MarsRover
import com.thrillpool.navigation.Operation
import com.thrillpool.navigation.representation.CoordinatesSystem.{OutOfBoundsError, Xaxis, Yaxis}

case class GridCoordinate(x: Xaxis, y: Yaxis) {

  def validateGrid: Either[OutOfBoundsError, GridCoordinate] = for {
    xaxis <- x.validate
    yaxis <- y.validate
  } yield GridCoordinate(xaxis, yaxis)

}

case class BorderMovement(roverOperations: List[Operation], updatedPosition: MarsRover)
