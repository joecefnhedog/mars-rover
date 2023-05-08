package com.thrillpool.navigation

import com.thrillpool.Rover
import com.thrillpool.navigation.AutoPilot.{Path, PathTaken}
import com.thrillpool.navigation.representation.North
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import com.thrillpool.navigation.representation.Grid
import com.thrillpool.navigation.representation.CoordinatesSystem.{Xaxis, Yaxis}

class AutoPilotSpec extends AnyFlatSpec with Matchers {

  "AutoPilot" should "navigate between start and end grid point,  while avoiding forbidden points" in {
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

    forbidden.foreach(forbiddenPoint =>
      pathTaken.path.route shouldNot contain(forbiddenPoint)
    )

//    pathTaken shouldBe PathTaken(
//      end.grid,
//      Path(
//        List(
//          Grid(Xaxis(4), Yaxis(3)),
//          Grid(Xaxis(4), Yaxis(2)),
//          Grid(Xaxis(4), Yaxis(1)),
//          Grid(Xaxis(3), Yaxis(1)),
//          Grid(Xaxis(2), Yaxis(1)),
//          Grid(Xaxis(2), Yaxis(6)),
//          Grid(Xaxis(2), Yaxis(5))
//        )
//      )
//    )

  }

}
