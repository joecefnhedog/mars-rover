# Joseph Bowen - Mars Rover


## Description

Defining a framework to represent a Rover on a _Square_ Grid, the allowed operations are:
1. moveForward
2. rotate clockWise
3. rotate antiClockWise
4. 
The Autopilot function `Autopilot.routeBetweenGridPoints` can be used to find the shortest path between grid points, and points can be forbidden along the way.
It can cross the boundary and re-appear on the opposite side as a means to arrive quicker.
It will return the route and detail the operations needed by the rover.
Errors will be returned if the route cannot be completed or the input grid coordinates are out of bounds.

## Getting Started
The `MarsRover` object extends App to "pretty print" one of the shortest routes between grid points, while avoiding a series of forbidden routes. 

`OperationSpec` shows a full run through where the _operations_ required by the rover are used to instruct the Rover to move to the Grid Destination.

Tests have been written to:
* Ensure the route is traversed succesfully, without crossing a forbidden square.
* Errors if the route cannot be completed or the input is out of grid bounds.

