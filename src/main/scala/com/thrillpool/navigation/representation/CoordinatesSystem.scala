package com.thrillpool.navigation.representation

import cats.implicits._

object CoordinatesSystem {

  trait Bounds {
    val upperBound = 6
    val lowerBound = 1

  }

  trait Operations extends Bounds {

    def isUpper(value: Int): Boolean = value == upperBound

    def isLower(value: Int): Boolean = value == lowerBound

  }

  case class Xaxis(value: Int) extends Operations {

    override def toString: String = s"X$value"

    def withinBounds = (value >= lowerBound && value <= upperBound)

    def validate: Either[OutOfBoundsError, Xaxis] = if (withinBounds)
      this.asRight
    else OutOfBoundsError(value, upperBound, lowerBound).asLeft

    def decrement: Xaxis =
      this.copy(value = if (isLower(value)) upperBound else value - 1)

    def increment: Xaxis =
      this.copy(value = if (isUpper(value)) lowerBound else value + 1)
  }

  case class Yaxis(value: Int) extends Operations {
    override def toString: String = s"Y$value"

    def withinBounds: Boolean = (value >= lowerBound && value <= upperBound)
    def validate: Either[OutOfBoundsError, Yaxis] = if (withinBounds)
      this.asRight
    else OutOfBoundsError(value, upperBound, lowerBound).asLeft

    def decrement: Yaxis =
      this.copy(value = if (isLower(value)) upperBound else value - 1)

    def increment: Yaxis =
      this.copy(value = if (isUpper(value)) lowerBound else value + 1)
  }

  case class OutOfBoundsError(value: Int, upperBound: Int, lowerBound: Int)
      extends RuntimeException(
        s"Index: $value is out of bounds. The given bounds are; upperBound: $upperBound and lowerBound: $lowerBound"
      )

}
