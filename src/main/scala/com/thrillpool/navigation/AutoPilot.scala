package com.thrillpool.navigation

import com.thrillpool.MarsRover
import com.thrillpool.navigation.representation.GridCoordinate
import org.slf4j.LoggerFactory

object AutoPilot {

  val logger = LoggerFactory.getLogger("AutoPilot")

  case class RoverWithOperations(rover: MarsRover, operations: List[Operation])

  case class Path(value: List[RoverWithOperations])


  case class PathTaken(lastGrid: MarsRover, path: Path)

  def routeBetweenGridPoints(
      start: MarsRover,
      end: GridCoordinate,
      forbiddenSquares: List[GridCoordinate]
  ): Either[RuntimeException, PathTaken] = {

    for {
      startGrid <- start.validate
      endGrid <- end.validateGrid

      allRoutes = checkAllRoutes(
        visited = Set(startGrid.gridPosition),
        border = List(Path(List(RoverWithOperations(startGrid, Nil)))),
        result = List(
          PathTaken(startGrid, Path(List(RoverWithOperations(startGrid, Nil))))
        ),
        targetGrid = endGrid,
        forbidden = forbiddenSquares
      )
      successfulRoutes = allRoutes.filter(_.lastGrid.gridPosition == endGrid)

      findShortestNonEmptyRoute <-
        shortestRoute(start, end, successfulRoutes)

      _ = logger.info(findShortestNonEmptyRoute.toString)

    } yield findShortestNonEmptyRoute

  }

  private def shortestRoute(
      start: MarsRover,
      end: GridCoordinate,
      successfulRoutes: List[PathTaken]
  ): Either[NoRoutesToDestination, PathTaken] = {
    successfulRoutes
      .filter(_.path.value.nonEmpty)
      .minByOption(_.path.value.size)
      .toRight(NoRoutesToDestination(start.gridPosition, end))
  }

  private def checkAllRoutes(
      visited: Set[GridCoordinate],
      border: List[Path],
      result: List[PathTaken],
      targetGrid: GridCoordinate,
      forbidden: List[GridCoordinate]
  ): List[PathTaken] = {
    if (border.isEmpty) result
    else {
      val path: Path = border.head
      val node = path.value.head
      if (node.rover.gridPosition == targetGrid) result
      else {

        val newVisited: Set[GridCoordinate] = visited + node.rover.gridPosition

        val allowedBorderMovement =
          node.rover.border.filterNot(neighbourOperation =>
            visited.contains(
              neighbourOperation.updatedPosition.gridPosition
            ) || forbidden
              .contains(neighbourOperation.updatedPosition.gridPosition)
          )

        val newBorder: List[Path] =
          border.tail ++ allowedBorderMovement.map { neighbourOperation =>
            Path(
              RoverWithOperations(
                neighbourOperation.updatedPosition,
                neighbourOperation.roverOperations
              ) :: path.value
            )
          }

        val newResult: List[PathTaken] =
          allowedBorderMovement.foldLeft(result) { (pathsTaken, operations) =>
            pathsTaken :+ PathTaken(
              operations.updatedPosition,
              Path(
                RoverWithOperations(
                  operations.updatedPosition,
                  operations.roverOperations
                ) :: path.value
              )
            )
          }

        checkAllRoutes(newVisited, newBorder, newResult, targetGrid, forbidden)
      }
    }
  }

  case class NoRoutesToDestination(
      start: GridCoordinate,
      destination: GridCoordinate
  ) extends RuntimeException(
        s"No Routes found from: $start to $destination"
      )
}
