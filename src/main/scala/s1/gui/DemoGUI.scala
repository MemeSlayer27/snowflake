package s1.gui

import s1.demo.*

import java.awt.Frame
import scala.collection.mutable.Buffer
import scala.swing.*
import scala.swing.Swing.*
import scala.swing.event.ButtonClicked

/** STUDENTS DON'T HAVE TO TOUCH THIS CLASS OR UNDERSTAND IT
 *
 * DemoGUI class is the top variable for the SimpleSwingApplication used
 * When the views change in the application it's the DemoGUI's view that changes
 *
 * @param app the SimpleSwingApplication where this DemoGUI is the top variable
 * @param effects all the effects to be showcased in the application
 * @param demo all the effects in the wanted in the demo
 * @param delay the delay for the effects to be used in milliseconds
 */

object DemoGUI extends MainFrame:

  // Get some useful variables from Animate
  val app: SimpleSwingApplication = Animate
  val effects = Animate.allEffects
  val demo = Animate.demo
  val delay = Animate.delay

  // Give the MainFrame menuBar
  menuBar = DemoMenu.menuBar

  // This is where the current demo being played is stored
  var currentDemo: Option[DemoArea] = None
  currentDemo = resetEffect(demo, true)

  visible = true

  // Reactions for different buttons
  app.reactions += {
    case clickEvent: ButtonClicked =>
      val buttonText = clickEvent.source.text
      // For restarting an effect or a demo
      if (buttonText == DemoMenu.restartEffect.text) then
        currentDemo match
          case Some(d) =>
            val isDemo = d.effects.map(_.name) == demo.map(_.name)
            currentDemo = resetEffect(d.effects, isDemo)
          case None =>
      // For starting the demo
      else if (buttonText == DemoMenu.startDemoMenu.text) then
        currentDemo = resetEffect(demo, true)
      // Play whatever is clicked
      else
        val effect = effects.find(effect => effect.name == buttonText)
        effect match
          case Some(eff) =>
            currentDemo = resetEffect(Buffer(eff), false)
          case None =>

  }

  /** Function for setting a new effect to be viewed in the MainFrame
   *
   * @param effects the new effect or effects
   * @param isDemo  true if the effects buffer indicates the demo
   * @return
   */
  def resetEffect(effects: Buffer[Effect], isDemo: Boolean): Option[DemoArea] =
    // Check if there is a demo playing at the moment, stop it if true
    currentDemo match
      case Some(demo) => demo.stopAnimating()
      case None =>

    // Create a new panel where the effects are located
    val newPanel = FlowPanel()
    val newEffects = Buffer[Effect]()
    for eff <- effects do
      newEffects += eff.newInstance

    // Create new DemoArea
    val newDemo = DemoArea(newEffects, isDemo)

    newPanel.contents += newDemo
    contents = newPanel
    newDemo.startAnimating(delay)

    // Return the new DemoArea wrapped in Option
    Some(newDemo)