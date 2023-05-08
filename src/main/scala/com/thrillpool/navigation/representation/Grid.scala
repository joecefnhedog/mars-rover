package com.thrillpool.navigation.representation

import com.thrillpool.navigation.representation.CoordinatesSystem.{OutOfBoundsError, Xaxis, Yaxis}

case class Grid(x: Xaxis, y: Yaxis) {

  def validateGrid: Either[OutOfBoundsError, Grid] = for {
    xaxis <- x.validate
    yaxis <- y.validate
  } yield Grid(xaxis, yaxis)

}
