package com.thrillpool.navigation.representation

import cats.implicits._

object CoordinatesSystem {

  trait Bounds {
    val upperBound: Int
    val lowerBound: Int
  }

  case class Xaxis(value: Int)(implicit bounds:Bounds)  {

    override def toString: String = s"X$value"

    val isUpper: Boolean = value == bounds.upperBound

    val isLower: Boolean = value == bounds.lowerBound

    def withinBounds = (value >= bounds.lowerBound && value <= bounds.upperBound)

    def validate: Either[OutOfBoundsError, Xaxis] = if (withinBounds)
      this.asRight
    else OutOfBoundsError(value).asLeft

    def decrement: Xaxis =
      this.copy(value = if (isLower) bounds.upperBound else value - 1)

    def increment: Xaxis =
      this.copy(value = if (isUpper) bounds.lowerBound else value + 1)
  }

  case class Yaxis(value: Int)(implicit bounds:Bounds) {
    override def toString: String = s"Y$value"

    val isUpper: Boolean = value == bounds.upperBound

    val isLower: Boolean = value == bounds.lowerBound

    def withinBounds: Boolean = (value >= bounds.lowerBound && value <= bounds.upperBound)

    def validate: Either[OutOfBoundsError, Yaxis] = if (withinBounds)
      this.asRight
    else OutOfBoundsError(value).asLeft

    def decrement: Yaxis =
      this.copy(value = if (isLower) bounds.upperBound else value - 1)

    def increment: Yaxis =
      this.copy(value = if (isUpper) bounds.lowerBound else value + 1)
  }

  case class OutOfBoundsError(value: Int)(implicit bounds: Bounds)
      extends RuntimeException(
        s"Index: $value is out of bounds. The given bounds are; upperBound: ${bounds.upperBound} and lowerBound: ${bounds.lowerBound}"
      )

}
