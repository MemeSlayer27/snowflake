package s1.gui

import java.awt.Color
import scala.collection.mutable.Buffer
import scala.swing.{Font, Menu, MenuBar, MenuItem}

/** STUDENTS DON'T HAVE TO TOUCH THIS CLASS OR UNDERSTAND IT
 *
 * DemoMenu creates the menu for the GUI. 
 *
 * @param GUI the DemoGUI where the menu is going to be located
 */

object DemoMenu:
  val GUI = DemoGUI

  // menuFont is used in all menuItems
  val menuFont = Font("Ariel", Font.Plain, 25)

  // these are menuItems which names you are able to change
  val changeEffectMenu = createMenu("Change effect")
  val restartEffect = createMenuItem("Restart effect")
  val startDemoMenu = createMenuItem("Start demo")
  val menuBar = createMenuBar

  // creates a single menuItem, basically applies the font to it
  def createMenuItem(name: String): MenuItem =
    val menuItem = MenuItem(name)
    menuItem.font = menuFont
    menuItem

  // same for menu
  def createMenu(name: String): Menu =
    val menu = Menu(name)
    menu.font = menuFont
    menu

  // creates the menuBar itself and adds the effects to the list of effects
  def createMenuBar =
    val menuBar = MenuBar()
    restartEffect.minimumSize = changeEffectMenu.preferredSize
    restartEffect.maximumSize = changeEffectMenu.preferredSize
    val backgroundColors = Buffer(Color.LIGHT_GRAY, Color.DARK_GRAY, Color.BLACK)

    menuBar.contents += changeEffectMenu
    menuBar.contents += restartEffect

    changeEffectMenu.contents += startDemoMenu
    GUI.app.listenTo(startDemoMenu)
    GUI.app.listenTo(restartEffect)

    for eff <- GUI.effects do
      val item = createMenuItem(eff.name)
      GUI.app.listenTo(item)
      changeEffectMenu.contents += item

    menuBar
