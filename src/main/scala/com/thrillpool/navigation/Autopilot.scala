package com.thrillpool.navigation

import com.thrillpool.Rover
import com.thrillpool.navigation.representation.Grid
import org.slf4j.LoggerFactory

object AutoPilot {
  val logger = LoggerFactory.getLogger("AutoPilot")

//  case class

  case class Path(route: List[Rover], operations: List[Operation]) {
    def size = route.length
  }

  object Path {
    val Empty = Path(Nil, Nil)
  }

  case class PathTaken(lastGrid: Rover, path: Path) {}

  def routeBetweenGridPoints(
      start: Rover,
      end: Grid,
      forbiddenSquares: List[Grid]
  ): Either[RuntimeException, PathTaken] = {

    for {
      startGrid <- start.validateGrid
      endGrid <- end.validateGrid
      allRoutes = checkAllRoutes(
        visited = Set(startGrid.grid),
        border = List(Path(List(startGrid), Nil)),
        result = List(PathTaken(startGrid, Path(List(startGrid), Nil))),
        targetGrid = endGrid,
        forbidden = forbiddenSquares
      )
      successfullRoutes = allRoutes.filter(_.lastGrid.grid == endGrid)
      findSmallestNonEmptyRoute <-
        successfullRoutes
          .filter(_.path.route.nonEmpty)
          .minByOption(_.path.route.size)
          .toRight(NoRoutesToDestination(start.grid, end))

      _ = logger.info(findSmallestNonEmptyRoute.toString)
      _ = println(findSmallestNonEmptyRoute)

    } yield findSmallestNonEmptyRoute

  }

  private def checkAllRoutes(
      visited: Set[Grid],
      border: List[Path],
      result: List[PathTaken],
      targetGrid: Grid,
      forbidden: List[Grid]
  ): List[PathTaken] = {
    if (border.isEmpty) result
    else {
      val path: Path = border.head
      val node = path.route.head
      if (node.grid == targetGrid) result
      else {

        val newVisited: Set[Grid] = visited + node.grid

        val neighbourOperation: List[node.NeighbourOperation] =
          node.neighbourWithMove.filterNot(neighbourOperation =>
            visited.contains(neighbourOperation.newGrid.grid) || forbidden
              .contains(neighbourOperation.newGrid.grid)
          )

        val newBorder: List[Path] =
          border.tail ++ neighbourOperation.map{neighbourOperation =>
            Path(
              neighbourOperation.newGrid :: path.route,
              neighbourOperation.operation ++ path.operations
            )
          }

        val newResult: List[PathTaken] = neighbourOperation.foldLeft(result) {
          (pathsTaken, operations) =>
            pathsTaken :+ PathTaken(
              operations.newGrid,
              Path(
                operations.newGrid :: path.route,
                operations.operation ++ path.operations
              )
            )
        }

        checkAllRoutes(newVisited, newBorder, newResult, targetGrid, forbidden)
      }
    }
  }

  case class NoRoutesToDestination(start: Grid, destination: Grid)
      extends RuntimeException(
        s"No Routes found from: $start to $destination"
      )
}
