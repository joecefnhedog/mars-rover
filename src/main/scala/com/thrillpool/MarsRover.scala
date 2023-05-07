package com.thrillpool

import scala.annotation.tailrec

//object MarsRover extends App {
//
//  abstract class NewGrid(orientation: Orientation) {}
//
//  sealed trait Yaxis {
//    def increment: Yaxis
//    def decrement: Yaxis
//  }
//
//  case object One extends Yaxis {
//    override def increment: Yaxis = Two
//    override def decrement: Yaxis = Three
//  }
//
//  case object Two extends Yaxis {
//    override def increment: Yaxis = Three
//    override def decrement: Yaxis = One
//  }
//
//  case object Three extends Yaxis {
//    override def increment: Yaxis = One
//    override def decrement: Yaxis = Two
//  }
//
//  sealed trait Xaxis {
//    def increment: Xaxis
//    def decrement: Xaxis
//  }
//  case object A extends Xaxis {
//    override def increment: Xaxis = B
//    override def decrement: Xaxis = C
//  }
//  case object B extends Xaxis {
//    override def increment: Xaxis = C
//    override def decrement: Xaxis = A
//  }
//  case object C extends Xaxis {
//    override def increment: Xaxis = A
//    override def decrement: Xaxis = B
//  }
//
//  sealed trait Orientation
//  case object up extends Orientation
//  case object down extends Orientation
//  case object left extends Orientation
//  case object right extends Orientation
//
//  val operandPlus: Int = 1 + 1
//
//  //y | a3 | b3 | c3 |
//  //y | a2 | b2 | c2 |
//  //y | a1 | b1 | c1 |
//  //y xxxxxxxxxxxxxxxx
//
//  case class Grid(x: Xaxis, y: Yaxis, orientation: Orientation) {
//    def moveForward: Grid = orientation match {
//      case up    => this.copy(y = y.increment)
//      case down  => this.copy(y = y.decrement)
//      case left  => this.copy(x = x.decrement)
//      case right => this.copy(x = x.increment)
//    }
//
//    def rotateClockwise = ???
//    def rotateAnticlockwise = ???
//
//  }
//
//  val init = Grid(A, One, up)
//
//  println(init)
//  println(init.moveForward)
//
//}

object solution2 extends App {

  trait Bounds {
    val upperBound = 5
    val lowerBound = 1
  }
  trait Operations extends Bounds {

    def isUpper(value: Int): Boolean = value == upperBound
    def isLower(value: Int): Boolean = value == lowerBound

  }

  case class X(value: Int) extends Operations {
    override def toString: String = s"X$value"
    def decrement: X =
      this.copy(value = if (isLower(value)) upperBound else value - 1)
    def increment: X =
      this.copy(value = if (isUpper(value)) lowerBound else value + 1)
  }

  case class Y(value: Int) extends Operations {
    override def toString: String = s"Y$value"
    def decrement: Y =
      this.copy(value = if (isLower(value)) upperBound else value - 1)
    def increment: Y =
      this.copy(value = if (isUpper(value)) lowerBound else value + 1)
  }

  trait Orientation {
    def clockwise: Orientation
    def antiClockwise: Orientation
  }
  case object Up extends Orientation {
    override def clockwise: Orientation = Right
    override def antiClockwise: Orientation = Left
  }
  case object Right extends Orientation {
    override def clockwise: Orientation = Down
    override def antiClockwise: Orientation = Up
  }
  case object Down extends Orientation {
    override def clockwise: Orientation = Left
    override def antiClockwise: Orientation = Right
  }
  case object Left extends Orientation {
    override def clockwise: Orientation = Up
    override def antiClockwise: Orientation = Down
  }

  case class Grid(x: X, y: Y, orientation: Orientation) {

    def moveForward: Grid = {
      orientation match {
        case Up    => this.copy(y = y.increment)
        case Right => this.copy(x = x.increment)
        case Down  => this.copy(y = y.decrement)
        case Left  => this.copy(x = x.decrement)
      }

    }

    def rotateClockwise: Grid = this.copy(orientation = orientation.clockwise)

    def rotateAnticlockwise: Grid =
      this.copy(orientation = orientation.antiClockwise)

  }

  object Grid extends Bounds {

    case class ResultWithHistory(grid: Grid, gridMovement: List[Grid])

    def apply(x: Int, y: Int, orientation: Orientation): Grid =
      Grid(X(x), Y(y), orientation)

    def pathBetweenPoints(start: Grid, end: Grid) = {

      val newDx = Math.min(dx,2)

      val dx = (end.x.value - start.x.value)
      val XdirectionToMove: Orientation = if (dx < 0) Left else Right
      val dy = (start.y.value - end.y.value)
      val YdirectionToMove: Orientation = if (dy < 0) Up else Down

      def repeat(grid: Grid, n: Int, history: List[Grid]): ResultWithHistory = {
        if (n > 0) {
          val movement = grid.moveForward
          repeat(movement, n - 1, history :+ (movement))
        } else {
          ResultWithHistory(grid, history)
        }
      }
      val newOrientation = start.copy(orientation = XdirectionToMove)

      val moveDx = repeat(
        grid = start.copy(orientation = XdirectionToMove),
        dx.abs,
        List(newOrientation)
      )

      val moveDy = repeat(
        moveDx.grid.copy(orientation = YdirectionToMove),
        dy.abs,
        moveDx.gridMovement
      )

      println("startGrid", start)
      println("resGrid", moveDy.grid)
      println("path taken", moveDy.gridMovement)
      println("finisGrid", end)

//      val dxLeft = (start.x.value - lowerBound) + (end.x.value-upperBound) +1

    }

  }

  val start = Grid(1, 3, Up)
  val end = Grid(5, 2, Up)

  println(Grid.pathBetweenPoints(start, end))

}
