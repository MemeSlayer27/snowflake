package s1.gui

import s1.demo.{Animate, Effect}
import s1.image.ImageExtensions.*

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.{File, IOException}
import java.util.{Scanner, Timer, TimerTask}
import javax.sound.sampled.*
import javax.swing.ImageIcon
import scala.collection.mutable.{Buffer, Queue}
import scala.swing.event.MouseDragged.apply
import scala.swing.event.{MouseMoved, MousePressed, MouseReleased}


/** STUDENTS DON'T HAVE TO TOUCH THIS CLASS OR UNDERSTAND IT
 *
 * DemoArea hosts one [[Effect]] at a time.
 * The code required to run it is already in place in the
 * [[Animate]] class.
 *
 */

class DemoArea(val effects: Buffer[Effect], val isDemo: Boolean) extends swing.Label:
  demo =>

  var currentEffect = effects.head
  var nextEffects   = effects.tail
  var audioPlayer: Option[SimpleAudioPlayer] = None

  var latest = emptyImage(currentEffect.height, currentEffect.width)
  icon = new ImageIcon(latest)
  var mousePosition:Option[(Int, Int)] = None

  var timer = Timer()

  // create a new task that updates the picture and changes the effect if needed
  val task = new TimerTask() {
      def run() =
        currentEffect.tick()
        latest=currentEffect.makePic()
        mousePosition.foreach((pos) => currentEffect.mouseAt(pos._1, pos._2))

        if currentEffect.next then
          if nextEffects.nonEmpty then
            currentEffect = nextEffects.head
            nextEffects = nextEffects.tail
        demo.repaint()
      end run
    }
  
  override def paintComponent(g: Graphics2D) = g.drawImage(latest, 0, 0, null)

  /**
   *  Simple Audio Player
   *
   *  Plays audio with play and stops playing with quit.
   *  DOESN'T WORK WITH MP3 FILES (.wav is recommended)
   *
   * @param filepath filepath to the audio file
   */
  class SimpleAudioPlayer(filepath: String):
    val audioInputStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile)

    val clip = AudioSystem.getClip
    clip.open(audioInputStream)

    clip.loop(Clip.LOOP_CONTINUOUSLY)

    def play() = clip.start()
    def stop() =
      clip.stop()
      clip.close()

  /**
   * Start the animation process and audio player if needed
   *
   * @param intervalMilliseconds the interval for the task
   */
  def startAnimating(intervalMilliseconds: Int) =
    if isDemo then
      Animate.demoMusic match
        case Some(str) =>
          try {
            val player = new SimpleAudioPlayer(str)
            audioPlayer = Some(player)
            player.play()
          }
          catch {
            case _: Throwable => throw new Exception("Something wrong with audio player")
          }
        case None =>
    timer = Timer()
    timer.schedule(task, 0, intervalMilliseconds)
  end startAnimating

  def stopAnimating() =
    audioPlayer match
      case Some(i) => i.stop()
      case None =>
    latest = emptyImage(currentEffect.height, currentEffect.width)
    task.cancel()
    timer.cancel()
    timer.purge()
    demo.repaint()
  end stopAnimating

  // Add reactions to mouse and listen to clicks and mouse
  reactions += {
    case mouse: MouseMoved => {
      currentEffect.mouseAt(mouse.point.getX.toInt, mouse.point.getY.toInt)
    }
    case mouse: MousePressed => {
      currentEffect.mousePress(mouse.point.getX.toInt, mouse.point.getY.toInt)
    }
    case mouse: MouseReleased => {
      currentEffect.mouseRelease(mouse.point.getX.toInt, mouse.point.getY.toInt)
    }
  }
  listenTo(this.mouse.clicks)
  listenTo(this.mouse.moves)

