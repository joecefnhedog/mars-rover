package com.thrillpool


import com.thrillpool.navigation.{AntiClockwise, Clockwise, Forward, Operation}
import com.thrillpool.navigation.representation.CoordinatesSystem.{Bounds, OutOfBoundsError, Xaxis, Yaxis}
import com.thrillpool.navigation.representation.{East, Grid, North, Orientation, South, West}

case class Rover(grid: Grid, orientation: Orientation) {

  def moveForward: Rover = {
    orientation match {
      case North => this.copy(grid = grid.copy(y = grid.y.increment))
      case East  => this.copy(grid = grid.copy(x = grid.x.increment))
      case South => this.copy(grid = grid.copy(y = grid.y.decrement))
      case West  => this.copy(grid = grid.copy(x = grid.x.decrement))
    }

  }

  def rotateClockwise: Rover = this.copy(orientation = orientation.clockwise)

  def rotateAnticlockwise: Rover =
    this.copy(orientation = orientation.antiClockwise)

  def validateGrid: Either[OutOfBoundsError, Rover] = for {
    xaxis <- grid.x.validate
    yaxis <- grid.y.validate
  } yield Rover(Grid(xaxis, yaxis), orientation)

  case class NeighbourOperation(operation: List[Operation], newGrid: Rover)

  private[thrillpool] def neighbourWithMove: List[NeighbourOperation] =
    List(
      NeighbourOperation(List(Forward), this.moveForward),
      NeighbourOperation(
        List(Clockwise, Forward),
        this.moveForward.rotateClockwise
      ),
      NeighbourOperation(
        List(Clockwise, Clockwise, Forward),
        this.rotateClockwise.rotateClockwise.moveForward
      ),
      NeighbourOperation(
        List(AntiClockwise, Forward),
        this.rotateAnticlockwise.moveForward
      )
    )

}

object Rover extends Bounds {

  def apply(x: Int, y: Int, orientation: Orientation): Rover =
    Rover(Grid(Xaxis(x), Yaxis(y)), orientation)


}
