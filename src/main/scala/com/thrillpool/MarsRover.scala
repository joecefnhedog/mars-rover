package com.thrillpool

import com.thrillpool.navigation.{
  AntiClockwise,
  AutoPilot,
  Clockwise,
  Forward,
  Operation
}
import com.thrillpool.navigation.representation.CoordinatesSystem.{
  Bounds,
  OutOfBoundsError,
  Xaxis,
  Yaxis
}
import com.thrillpool.navigation.representation.{
  BorderMovement,
  East,
  GridCoordinate,
  North,
  Orientation,
  South,
  West
}

case class MarsRover(gridPosition: GridCoordinate, orientation: Orientation) {

  def moveForward: MarsRover = {
    orientation match {
      case North =>
        this.copy(gridPosition =
          gridPosition.copy(y = gridPosition.y.increment)
        )
      case East =>
        this.copy(gridPosition =
          gridPosition.copy(x = gridPosition.x.increment)
        )
      case South =>
        this.copy(gridPosition =
          gridPosition.copy(y = gridPosition.y.decrement)
        )
      case West =>
        this.copy(gridPosition =
          gridPosition.copy(x = gridPosition.x.decrement)
        )
    }

  }

  def rotateClockwise: MarsRover =
    this.copy(orientation = orientation.clockwise)

  def rotateAnticlockwise: MarsRover =
    this.copy(orientation = orientation.antiClockwise)

  private[thrillpool] def validate: Either[OutOfBoundsError, MarsRover] = for {
    xaxis <- gridPosition.x.validate
    yaxis <- gridPosition.y.validate
  } yield MarsRover(GridCoordinate(xaxis, yaxis), orientation)

  private[thrillpool] def border: List[BorderMovement] =
    List(
      BorderMovement(List(Forward), this.moveForward),
      BorderMovement(
        List(Forward, Clockwise),
        this.rotateClockwise.moveForward
      ),
      BorderMovement(
        List(Forward, Clockwise, Clockwise),
        this.rotateClockwise.rotateClockwise.moveForward
      ),
      BorderMovement(
        List(Forward, AntiClockwise),
        this.rotateAnticlockwise.moveForward
      )
    )

}

object MarsRover extends App {

  implicit val bounding: Bounds = new Bounds {
    override val upperBound: Int = 6
    override val lowerBound: Int = 1
  }


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

  pathTaken.path.value.reverse.foreach { (roverWithOperations) =>
    val operations = roverWithOperations.operations

    val movement =
      if (operations.isEmpty) s"Starting at:"
      else
        s"Performed the following operations: (${operations.mkString(",")}) to get to:"

    println(s"${movement}  $roverWithOperations.rover")

  }

}
