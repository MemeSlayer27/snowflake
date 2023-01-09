package s1.demo.effects
import s1.image.ImageExtensions.*
import s1.demo.*

import java.awt.Color.*
import java.awt.{BasicStroke, Color}
import java.awt.image.BufferedImage
import scala.util.Random
import scala.math.*
import scala.collection.mutable.Buffer



class SnowFlake extends Effect(800, 800, "Snowflake"):

  var flakes = Buffer[Flake]()
  val poistettavat = Buffer[Flake]()

  val värit= Vector(RED, BLUE, BLACK, WHITE, Color.cyan,Color.orange)
  val koot = Vector(50, 80, 100, 120, 150, 200,300)
  val sarakkeet = Vector(100, 80, 60,120,90,53,77,69,5,6,7,13)


  val sectorsStochastic = Vector(5,9,50,13,23,40)


  var clock = 0

  val newCoords = Buffer[(Double,Double)]()

  def makePic(): java.awt.image.BufferedImage =
        // Get an empty space where to draw
    val pic      = emptyImage


    // Get the tools to draw with
    var graphics = pic.graphics

    for hiutale <- flakes do
      graphics.setColor(hiutale.color)
      hiutale.points.toVector.foreach( (sector: Int, pointCoords: Vector[(Double, Double)]) => draw(pointCoords) )

    def draw(pointVector: Vector[(Double,Double)]) =
      val pointsAndNext = pointVector.zip(pointVector.tail)
      pointsAndNext.foreach((f: (Double, Double), s: (Double, Double)) => graphics.drawLine(f._1.toInt, f._2.toInt, s._1.toInt, s._2.toInt))


    pic





  def newInstance: s1.demo.Effect = new SnowFlake
  def next: Boolean = this.clock > 1000

  override def mousePress(x: Int, y: Int): Unit = newCoords.append((x.toDouble,y.toDouble))

  def flakeCreator() =

    Random.nextInt(13) + 1 match
      case 8|7 => ()
      case 1|2|3 =>
        flakes += Flake(Random.nextInt(800), Random.nextInt(800),
          Random.shuffle(sectorsStochastic).head, Random.shuffle(värit).head, None) // Should create a stochastic flake
        flakes.last.koko = Random.shuffle(koot).head
      case 4|5|6 =>
        val sectors = Vector(100, 80, 60,120,90,53,77,69)
        flakes += Flake(Random.nextInt(800), Random.nextInt(800),
          Random.shuffle(sectors).head, Random.shuffle(värit).head, Some(x => sqrt(x)*35*sin(1/18.0*x)))
        flakes.last.koko = Random.shuffle(koot).head
      case _ =>
        flakes += Flake(Random.nextInt(800), Random.nextInt(800),
          Random.shuffle(sarakkeet).head, Random.shuffle(värit).head, Some(x => 20*sin(1.0/20*x)))
        flakes.last.koko = Random.shuffle(koot).head


  def tick(): Unit =



    clock += 1

    if newCoords.nonEmpty then
      for pari <- newCoords do
        flakes += Flake(pari._1, pari._2, Random.shuffle(sectorsStochastic).head, Random.shuffle(värit).head,None)
        flakes.last.koko = Random.shuffle(koot).head
      newCoords.clear()


    if clock % 25 == 0 then
      flakeCreator()


    for hiutale <- flakes do
      if hiutale.aika <= hiutale.koko then
        hiutale.aika += 1
        hiutale.pointGenerator()


      else if hiutale.aika > hiutale.koko && hiutale.color.getAlpha >= 5 then
        hiutale.color = Color(hiutale.color.getRed, hiutale.color.getGreen, hiutale.color.getBlue, hiutale.color.getAlpha - 7)
      else if hiutale.color.getAlpha <= 5 then
        poistettavat += hiutale

    poistettavat.foreach(f => flakes.remove(flakes.indexOf(f)))
    poistettavat.clear()




end SnowFlake




class Flake(val x: Double, val y:Double, val sectors: Int, var color: Color, val fn: Option[Double => Double]):
  var points: Map[Int, Vector[(Double, Double)]] = (0 until this.sectors).map( sector => sector -> Vector() ).toMap
  var aika = 0
  var koko = 500

  var lastPoints = Vector((0.0, 0.0),(0.0,0.0))

  def addPoint(xCoord: Double, yCoord: Double) =
    val length = sqrt(pow(xCoord, 2)+pow(yCoord,2))
    val originalAngle = fn match
      case None => anglePicker((0.0,0.0),(xCoord,yCoord))
      case Some(_) => atan(yCoord / xCoord)
    var coordPairs = Vector((this.x + xCoord ,this.y - yCoord)) // creates the first coordinate pair
    (1 until this.sectors).foreach(
      s =>
        coordPairs = coordPairs :+
          (this.x + (length * cos(originalAngle + s * (2*Pi)/sectors)),
           this.y - (length * sin(originalAngle + s * (2*Pi)/sectors))) )  // creates the rest into the same vector. The index indicates to which sector each belongs
    points = points.toVector.map( (sector: Int, pointPair: Vector[(Double, Double)]) => sector -> (pointPair :+ coordPairs(sector)) ).toMap // adds the new coordinatepairs into points map

  def pointGenerator() =
    this.fn match       // Given a function, the flake will be generated with it, otherwise make it stochastic
      case Some(function) =>
        this.points.getOrElse(0, Vector((100.0, 100.0))).lift(0) match
          case None =>
            val point = (0.000000000001, function(0.000000000001))
            this.addPoint(point._1, point._2)
            this.lastPoints = this.lastPoints.tail :+ point
          case Some(_) =>
            val point = (lastPoints(lastPoints.size-1)._1 + 1, function(lastPoints(lastPoints.size-1)._1 + 1))
            this.addPoint(point._1, point._2)
            this.lastPoints = this.lastPoints.tail :+ point

      case None =>
        this.points.getOrElse(0, Vector((100.0, 100.0))).lift(0) match
          case None =>
            val point = (0.000000000001, 0.000000000001)
            this.addPoint(point._1, point._2)
            this.lastPoints = this.lastPoints.tail :+ point
          case Some(_) =>
            val previousAngle = this.anglePicker(this.lastPoints(0), this.lastPoints(1))//atan((this.lastPoints(1)._2 - this.lastPoints(0)._2)/(this.lastPoints(1)._1 - this.lastPoints(0)._1))
            val newAngle = previousAngle + Pi*random() - 1.0/2.0*Pi
            val point = (lastPoints(lastPoints.size-1)._1 + 3.0*cos(newAngle), lastPoints(lastPoints.size-1)._2 + 3.0*sin(newAngle))
            this.addPoint(point._1,point._2)
            this.lastPoints = this.lastPoints.tail :+ point

  private def anglePicker(point1: (Double,Double), point2: (Double,Double)) =
    val xDiff = point2._1 - point1._1
    val yDiff = point2._2 - point1._2

    var angleIfUpAndToTheRight = atan(abs(yDiff)/abs(xDiff))

    (xDiff,yDiff) match
      case x if xDiff == 0 => Pi/2
      case y if yDiff == 0 => 0.0
      case a if xDiff > 0 && yDiff > 0 => angleIfUpAndToTheRight
      case b if xDiff < 0 && yDiff > 0 => Pi - angleIfUpAndToTheRight
      case c if xDiff < 0 && yDiff < 0 => angleIfUpAndToTheRight + Pi
      case d if xDiff > 0 && yDiff < 0 => 2*Pi - angleIfUpAndToTheRight




end Flake