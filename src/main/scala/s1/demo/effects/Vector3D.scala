package Util
import scala.math.* 
/*
* The Vector3D class is a data type that contains all necessary vector operations for vector calculations in this program
*/
case class Vector3D(x: Double, y: Double, z: Double) {

  //add two vectors together
  def +(other: Vector3D) = Vector3D(this.x + other.x, this.y + other.y, this.z + other.z)

  //get the difference of two vectors
  def -(other: Vector3D) = Vector3D(this.x - other.x, this.y - other.y, this.z - other.z)

  //multiply this vector by a double or a set of doubles
  def *(double: Double) = Vector3D(this.x * double, this.y * double, this.z * double)

  def *(other: Vector3D) = Vector3D(this.x * other.x, this.y * other.y, this.z * other.z)

  //divide this vector by a double
  def /(double: Double) = Vector3D(this.x / double, this.y / double, this.z / double)

  //new vector with values forced with x, y or z
  def force(x: Option[Double], y: Option[Double], z: Option[Double]) = Vector3D(x.getOrElse(this.x), y.getOrElse(this.y), z.getOrElse(this.z))

  //get the length of this vector
  lazy val length = Math.sqrt(this dot this) //potential bug faulty length calculation

  //get a vector in the direction of this vector that has a length of 1
  def normalize = if (length != 0) Vector3D(this.x / length, this.y / length, this.z / length) else Vector3D(0, 0, 0)

  //reversing functions
  def reverseX = new Vector3D(-this.x, this.y, this.z)
  def reverseY = new Vector3D(this.x, -this.y, this.z)
  def reverseZ = new Vector3D(this.x, this.y, -this.z)

  //dot product operation of this and other
  def dot(other: Vector3D): Double = this.x * other.x + this.y * other.y + this.z * other.z

  //get a vector that is the parallel component of this in the direction of other
  def parallelComponent(other: Vector3D): Vector3D = other * ((this dot other) / (other dot other))

  //get a vector that is the normal component of this in perpendicular to the direction of other
  def normalComponent(other: Vector3D): Vector3D = this - this.parallelComponent(other)

  //mirror this vector along other
  def mirror(other: Vector3D): Vector3D = this - this.normalComponent(other) * 2

  override def toString =
    "x: "+this.x + ", y: "+this.y + ", z: "+this.z
}

class RotationMatrix3D(xtilt: Double, ytilt: Double, ztilt: Double){
  val (sinx, cosx) = (sin(xtilt), cos(xtilt))
  val (siny, cosy) = (sin(ytilt), cos(ytilt))
  val (sinz, cosz) = (sin(ztilt), cos(ztilt))
  //println("cos,sin values are for x,y,z tilts: ")
  //println(sinx + " "+ cosx + " " + siny + " " + cosy + " " + sinz + " " + cosz)
  //Rotational matrix based on x,y and z tilts (roll, pitch and yaw)
  val mat: Array[Array[Double]] = Array(
    Array(cosy*cosz, sinx*siny*cosz-cosx*sinz, cosx*siny*cosz+sinx*sinz),
    Array(cosy*sinz, sinx*siny*sinz+cosx*cosz, cosx*siny*sinz-sinx*cosz),
    Array(  -siny,          sinx*cosy,                  cosx*cosy      )
  )

  //debug for matrix
  //mat.foreach(_.zipWithIndex.foreach((a,i) => println(i+1 + ": " + a)))

  //Produces a vector that has rotations applied to it.
  def *(vec: Vector3D): Vector3D = 
    val m1 = mat(0)
    val m2 = mat(1)
    val m3 = mat(2)
    Vector3D(
    x = vec.x*m1(0)+vec.y*m1(1)+vec.z*m1(2),
    y = vec.x*m2(0)+vec.y*m2(1)+vec.z*m2(2),
    z = vec.x*m3(0)+vec.y*m3(1)+vec.z*m3(2)
    )
}
