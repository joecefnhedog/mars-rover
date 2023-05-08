package com.thrillpool.navigation.representation

trait Orientation {
  def clockwise: Orientation

  def antiClockwise: Orientation

}

case object North extends Orientation {
  override def clockwise: Orientation = East

  override def antiClockwise: Orientation = West

}

case object East extends Orientation {
  override def clockwise: Orientation = South

  override def antiClockwise: Orientation = North

}

case object South extends Orientation {
  override def clockwise: Orientation = West

  override def antiClockwise: Orientation = East

}

case object West extends Orientation {
  override def clockwise: Orientation = North

  override def antiClockwise: Orientation = South

}
