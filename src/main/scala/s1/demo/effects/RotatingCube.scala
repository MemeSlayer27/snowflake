package s1.demo.effects

import Util.Vector3D
import Util.RotationMatrix3D
import s1.image.ImageExtensions.*
import s1.demo.*
import scala.math.* 
import java.awt.Color.*
import java.awt.BasicStroke
import java.awt.image.BufferedImage
import scala.util.Random
import java.awt.Point

/**
 * Simple class to help with the mathematics of the rotations
*/

class RotatingCube extends Effect(500, 500, "Rotating Cube"){
  
  // ...and here is our initial instance

  var size = 60
  var clock = 1
  val deg = Pi/180

  //origo for rotation
  val mid = Vector3D(this.width/2,this.height/2,size)

  /* tilt of the cube's _ axis in radians
    eg. x axis tilt is in yz plane
  */
  var ytilt: Double = 1
  var xtilt: Double = deg*45
  var ztilt: Double = deg*1
  
  var ytiltSpeed: Double = 0
  var xtiltSpeed: Double = 0
  var ztiltSpeed: Double = 0
  
  var mouse1: Vector3D = mid
  var mouse2: Vector3D = Vector3D(0,0,0)
  var mouse3: Vector3D = Vector3D(0,0,0)

  def rMat = RotationMatrix3D(xtilt,ytilt,ztilt) 

  var dirX = rMat*(Vector3D(1, 0, 0)*size); //println(dirX)  
  var dirY = rMat*(Vector3D(0, 1, 0)*size); //println(dirY)
  var dirZ = rMat*(Vector3D(0, 0, 1)*size); //println(dirZ)


  def lines: Vector[Array[Vector3D]] = 
  
    val p1 = mid+dirX+dirY+dirZ
    val p2 = mid-dirX+dirY+dirZ
    val p3 = mid+dirX-dirY+dirZ
    val p4 = mid-dirX-dirY+dirZ

    val p5 = mid+dirX+dirY-dirZ
    val p6 = mid-dirX+dirY-dirZ
    val p7 = mid+dirX-dirY-dirZ
    val p8 = mid-dirX-dirY-dirZ

    Vector(
      Array(p1,p2,p4,p3),
      Array(p1,p3,p7,p5),
      Array(p1,p2,p6,p5),
      Array(p2,p4,p8,p6),
      Array(p5,p6,p8,p7),
      Array(p3,p4,p8,p7)
    )
    
  /**
   * Here we draw a BufferedImage on the current state of the [[Effect]]
   */
  def makePic() =
    // Get an empty space where to draw
    val pic      = emptyImage

    // Get the tools to draw with
    val graphics = pic.graphics

    graphics.setColor(white)
    graphics.setColor(black)
    lines.map(corners => graphics.drawPolygon(corners.map(_.x.toInt), corners.map(_.y.toInt),4))

    pic


  /**
   * Here we modify the state (the tilts of the square)
   */
  def tick() =
    dirX = rMat*(Vector3D(1, 0, 0)*size); //println(dirX)  
    dirY = rMat*(Vector3D(0, 1, 0)*size); //println(dirY)
    dirZ = rMat*(Vector3D(0, 0, 1)*size); //println(dirZ)
    clock += 1

    val dirV = mouse1 - mid
    
    ytiltSpeed = atan(dirV.x)
    xtiltSpeed = atan(dirV.y)

    ytilt += ytiltSpeed*deg
    xtilt += xtiltSpeed*deg    
    ztilt += ztiltSpeed*deg
  
  var press = false
  override def mousePress(x: Int, y: Int) = {
    mouse1 = Vector3D(x,y,size)
  }


  def next = false

  def newInstance = new RotatingCube
}
