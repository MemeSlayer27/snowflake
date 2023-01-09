package s1.demo.effects

import javax.imageio.ImageIO
import java.io.File
import s1.image.ImageExtensions._
import scala.util.Random
import s1.demo.Effect

/**
 * This example demonstrates manipulating single pixels
 */
class Dissolve extends Effect(750, 750, "Dissolve") {
  // This variable is used to find out when to end this effect
  var clock = 0

  // Here we load the image we are going to manipulate
  var current = {
    // Start with an empty image
    val start = emptyImage

    // ...and draw the Aalto logo in the middle of it
    val aaltoLogo = ImageIO.read(new File("pictures/Aalto_logo.png"))
    start.graphics.drawImage(aaltoLogo, 225, 225, null)

    // Return the modified image
    start
  }

  val random = new Random

  // A completely random color used later in the algorithm
  val transparent = s1.image.Color(0,0,0,0)

  // Clamp used to keep (randomly generated) pixel coordinates inside the image area
  def clamp(v: Int, maxValue: Int) = math.max(0, math.min(v, maxValue))

  /**
   * This method creates (or in this case only returns) a BufferedImage
   * of the effect
   */
  def makePic() = current

  /**
   * This method updates the state of the effect - In this particular algorithm it
   * actually manipulates the image.
   */
  def tick() =
    // The current bitmap image can be copied if we do not wish to edit the original
    // (In this example editing the original would have been fine - this is to explain things)
    val nextImage = current.copy

    for (i <- 1 to 300) {
      // pick a random coordinate
      val x1 = random.nextInt(width)
      val y1 = random.nextInt(height)

      val n = nextImage(x1, y1)

      // ...and another one nearby
      val x2 = clamp(x1 - 10 + random.nextInt(20), width-1)
      val y2 = clamp(y1 - 10 + random.nextInt(20), height-1)

      // We can check if pixels are opaque
      if (nextImage(x1,y1).opaque && !nextImage(x2, y2).opaque) {

        // We can rean and write the value of a pixel
        nextImage(x2, y2).color = nextImage(x1,y1).color

        // We can set the color of any pixel to a predefined color
        nextImage(x1, y1).color = transparent
      }
    }
    // This helper variable is used to check when to change effects
    clock += 1

    current = nextImage
  end tick

  // The effect ends when we have changed the model 300 times
  def next = clock > 300
  
  def newInstance = new Dissolve
}