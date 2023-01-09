package s1.demo.effects
import s1.image.ImageExtensions.*
import s1.demo.*

import java.awt.Color.*
import java.awt.BasicStroke
import java.awt.image.BufferedImage
import scala.util.Random

/**
 * This simple effect features a bouncing ball
 */
class BouncingBall extends Effect(750, 750, "Bouncing ball"){

  // The ball has coordinates, size and speed. We also keep track whether the ball is going forwards or backwards
  class Ball(val x: Int, val y: Int, var xSpeed: Int, var ySpeed: Int, val ballSize: Int, val growing: Boolean)

  class BounceBorder(val x: Int, val y: Int, val size: Int)
  class Shadow(val x: Int, val y: Int)

  // ...and here is our initial instance
  val ySpeedInitial = 10
  var ball = new Ball(200, 200, 8, ySpeedInitial, 30, false)

  // Limits for ball sizes
  val minBallSize = ball.ballSize
  val maxBallSize = ball.ballSize + 75
  val diff = maxBallSize - minBallSize

  // How fast we go from the front to back
  val depthSpeed = 5

  val random = new Random

  // The strength of the bounce, see how the effect reacts when changing this value
  val bounceForce = -25
  var clock  = 0

  // The ball cannot go through this border
  var bounceBorder = BounceBorder(0, 0, width - depthSpeed * diff)

  //val wall = (bounceBorder.size - math.sqrt(2.0)*ball.ballSize).toInt
  val wall = bounceBorder.size - ball.ballSize

  val wallCoord = ((width - wall)/2.0).toInt

  /**
   * Here we draw a BufferedImage on the current state of the [[Effect]]
   */
  def makePic() =
    // Get an empty space where to draw
    val pic      = emptyImage

    // Get the tools to draw with
    val graphics = pic.graphics

    // Set color
    graphics.setColor(BLACK)

    // Draw the image with this color
    graphics.drawRect(wallCoord, wallCoord, wall, wall)
    graphics.drawLine(0,0,wallCoord,wallCoord)
    graphics.drawLine(width, 0, wallCoord + wall, wallCoord)
    graphics.drawLine(0, height, wallCoord, wallCoord + wall)
    graphics.drawLine(width, height, wallCoord + wall, wallCoord + wall)

    //bounceBox, uncomment the below line to see the border for the ball
    //graphics.drawRect(bounceBorder.x, bounceBorder.y, bounceBorder.size, bounceBorder.size)

    // shadow
    graphics.fillOval(ball.x, bounceBorder.y + bounceBorder.size - 10, ball.ballSize, ball.ballSize - 25)

    // the actual ball
    graphics.fillOval(ball.x, ball.y, ball.ballSize, ball.ballSize)

    // Finally we return the picture we created.
    pic

  /**
   * Here we modify the state (the position and speed of the ball)
   */
  def tick() =
    clock += 1

    // Set new variables
    var y = ball.y + ball.ySpeed
    var x = ball.x + ball.xSpeed
    var ax = 0
    var ay = 0
    var borderSize = bounceBorder.size

    // Set the values for growing and ballSize in the next frame
    val (nextGrowing, nextBallSize) =
      if ball.growing && ball.ballSize < maxBallSize then
        borderSize += depthSpeed
        (ball.growing, ball.ballSize + 1)
      else if !ball.growing && ball.ballSize > minBallSize then
        borderSize -= depthSpeed
        (ball.growing, ball.ballSize - 1)
      else
        (!ball.growing, ball.ballSize)

    // Adjust the ball speed slightly every five frames
    if ball.ballSize % 5 == 0 then
      if ball.ySpeed > 0 then ay = 1 else ay = -1
      if ball.xSpeed > 0 then ax = 1 else ax = -1
      if !ball.growing then
        ay *= -1
        ax *= -1

    // Set new values for coordinates and speed
    // When ball hits the border fix the coordinates to the edge of the border
    val (nextY, nextYSpeed) =
      if y > ((bounceBorder.y + bounceBorder.size) - nextBallSize) then
        ((bounceBorder.y + bounceBorder.size) - nextBallSize, -ball.ySpeed + 2)
      else if y < bounceBorder.y then
        (bounceBorder.y, -ball.ySpeed)
      else
         (y, ball.ySpeed + 4 + ay)

    val (nextX, nextXSpeed) =
      if x > ((bounceBorder.x + bounceBorder.size) - nextBallSize) then
        ((bounceBorder.x + bounceBorder.size) - nextBallSize, -ball.xSpeed)
      else if x < bounceBorder.x then
        (bounceBorder.x, -ball.xSpeed)
      else
        (x, ball.xSpeed + ax)

    // Update the border
    val bx = ((width - bounceBorder.size)/2.0).toInt
    val by = ((height - bounceBorder.size)/2.0).toInt

    bounceBorder = BounceBorder(bx, by, borderSize)

    // We could have done this with a ball with it's coordinates in var's
    // It can also be done in a more functional way, replacing the ball
    // itself
    ball = new Ball(nextX, nextY, nextXSpeed, nextYSpeed, nextBallSize, nextGrowing)

  def next = clock > 300

  def newInstance = new BouncingBall
}
