package s1.demo.effects

import java.awt.image.BufferedImage
import java.awt.BasicStroke
import s1.image.ImageExtensions.*

import scala.collection.mutable.Queue
import scala.swing.*
import java.awt.Color.*
import s1.demo.Effect


/**
 * The idea for this effect came from Felix Bade.
 *
 * The effect draws a continuous stream of filled
 * circles that changes it's course randomly.
 */

class Snakes extends Effect(750, 750, "Snakes") {

    class Circle(val x: Int, val y: Int)
  // This variable could hold a background image if wanted
  //  See [[DIssolve]] for an exampleon how to load image files
    val background: Option[BufferedImage] = None

    // The circles we draw are in a queue.The latest is
    // always stored in [[last]]
    // The normal effect circles are stored in snakeCircles
    var last         = new Circle(100, 100)
    val snakeCircles = Queue[Circle](last)
    val mouseCircles = Queue[Circle]()
    var direction    = 0.0
    val step         = 10
    var queueLength  = 1

    val random = new util.Random

    def tick() =
      // Change the direction where to draw the next circle randomly
      direction = direction + (random.nextDouble - 0.5);

      // Calculate the new coordinates
      val xdiff = (math.cos(direction) * step).toInt
      val ydiff = (math.sin(direction) * step).toInt

      val x = (last.x + xdiff + width) % width
      val y = (last.y + ydiff + height) % height
      last  = new Circle(x,y)

      // Store the circle in a queue (Think of a buffer where you add in one end and take stuff out from the other)
      snakeCircles.enqueue( last )
      queueLength += 1

      // If the queue gets big, we delete older circles for a fun effect
      while (queueLength > 600) do
        snakeCircles.dequeue()
        queueLength -= 1

    //------- drawing -------//

    // Thick and thin line widths
    val thick = new BasicStroke(3)
    val thin  = new BasicStroke(1)

    // Nice font for the christmas message
    val christmasFont  = Font("Comic Sans MS", Font.BoldItalic, 60)

    override def makePic(): BufferedImage =
      val pic = emptyImage
      val g = pic.graphics

      // If there was a background we could paint it here
      // The parameters of drawImage are picture, x, y and an ImageObserver which we in this case leave null.
      background foreach (image => g.drawImage(image, 0, 0, null))

      // Draw all the circles.

      // For more ideas on drawing see:
      //   https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
      //   https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics2D.html
      //   https://docs.oracle.com/javase/tutorial/2d/basic2d/index.html
      def drawCircle(x: Int, y: Int) =
        g.setColor(WHITE)
        g.setStroke(thin)
        g.fillOval(x - 20, y - 20, 40, 40)

        g.setStroke(thick)
        g.setColor(BLACK)
        g.drawOval(x - 20, y - 20, 40, 40)

      for c <- snakeCircles do
        drawCircle(c.x, c.y)

      for c <- mouseCircles do
        drawCircle(c.x, c.y)

      g.setFont(christmasFont)

      // Notice the fake "shadow" under the text
      g.setColor(BLACK)
      g.drawString("Hyvää joulua!", 24, 48)

      g.setColor(GREEN)
      g.drawString("Hyvää joulua!", 22, 46)

      pic

    // Effects can also receive information on mouse movements.
    // When a mouse goes to ne coordinates this method gets called.

    // We use it to draw still more circles at the mouse location
    override def mouseAt(x: Int, y: Int) =
      mouseCircles enqueue new Circle(x,y)
      if (mouseCircles.length > 300) then
        mouseCircles.dequeue()


    // This effect will never end
    def next = false
    def newInstance = new Snakes
}

