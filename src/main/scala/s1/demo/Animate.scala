package s1.demo

import scala.swing.*
import scala.swing.Swing.*
import scala.collection.mutable.Buffer
import s1.gui.*
import s1.demo.effects.*

/**
 * Animate extends SimpleSwingApplication which makes
 * Animate a Swing application 
 *
 * In buffer "allEffects" you can initiate new instances of
 * all the effects from the package s1.demo.effects
 *
 * In buffer "demo" please initiate in order the effects used in your
 * demo ( aka combination of effects ). Effects are changed when the
 * current effect returns true from its "next" method.
 * 
 * Music can be added by adding the path of the .wav file wrapped in Option.
 * If music is not required for the demo, you can simply give the demoMusic value None.
 * Music is only added for the demo as a whole, not to individual effects.
 * NOTE: Unfortunately, the current version doesn't support MP3 files. wav is recommended.
 *
 * It is also possible to change the delay for effects
 * -> change the speed how quickly things change. Feel free to
 * change this variable and play with it.
 *
 * The variable top declares the top variable needed for SimpleSwingApplication.
 * In order to build your own effects and demos you don't have to touch this variable.
 */

object Animate extends SimpleSwingApplication {

  val allEffects: Buffer[Effect] = Buffer(SnowFlake(), BouncingBall(), Dissolve(), RasterBar(), Snakes(), RotatingCube())
  val demo: Buffer[Effect] = Buffer(SnowFlake())
  val demoMusic: Option[String] = Some("sound/sandmusic.wav")
  val delay = 25

  val top: MainFrame = DemoGUI
}
