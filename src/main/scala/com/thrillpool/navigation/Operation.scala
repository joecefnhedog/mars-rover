package com.thrillpool.navigation

import com.thrillpool.MarsRover

sealed trait Operation
case object Forward extends Operation
case object Clockwise extends Operation
case object AntiClockwise extends Operation

object Operation {

  def performOperations(
      operations: List[Operation],
      startRover: MarsRover
  ): MarsRover =
    operations.foldLeft(startRover) { (rover, operation) =>
      operation match {
        case Forward       => rover.moveForward
        case Clockwise     => rover.rotateClockwise
        case AntiClockwise => rover.rotateAnticlockwise
      }
    }
}
