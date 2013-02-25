package ru.tomtrix.ttf

import ExtendedShell._
import org.eclipse.swt.SWT
import patterns.Disposable._
import org.eclipse.swt.widgets._
import org.eclipse.swt.events._
import patterns.{ActorsManager, Undo, Repository}
import org.apache.log4j.Logger
import SWTWrappers._
import ExtendedSearch._
import org.eclipse.swt.graphics.{Color, GC, Image}

object Starter extends App with Undo {
  using(ActorsManager) { g =>
    var s = "abc"
    doCommand({s = s.toUpperCase}, {s = s.toLowerCase})
    println(s)
    undo()
    println(s)
    redo()
    println(s)
    undo()
    undo()
    undo()
    undo()
    println(s)
    redo()
    println(s)

    Logger.getLogger(getClass).warn("fuck me, baby!")
    using(new Repository("trix.sqlite")(SQLITE)) { t =>
      t.getTable("Select * from Children where age < ?", Seq(14)) foreach println
      t.getTuple("Select * from Children where age < ?", Seq(14)) foreach println
      t.getAttribute[String]("Select name from Children where age < ?", Seq(14)) foreach (t => println(t))
      println("count = %d" format t.getValue[Int]("Select count(*) from Children where age < ?", Seq(14)).getOrElse(-1))
    }
    val display = new Display
    showSplashScreenFunc("images.jpg", errorText = "Loading") { t =>
      Thread.sleep(500)
    }
    val shell = new Shell(display)
    shell.setBounds(100, 100, 600, 300)
    val gt = new Text(shell, SWT.BORDER)
    gt.setLocation(30, 30)
    gt.setSize(130, 22)

    /*val f = new Text(shell, SWT.SEARCH | SWT.ICON_SEARCH)
    f.setBounds(20, 30, 120, 125)
    val img = new Image(Display.getDefault, 120, 125)
    val gc = new GC(img)
    f.addModifyListener(new ModifyListener {
      def modifyText(e: ModifyEvent) {
        gc.fillRectangle(0, 0, 120, 125)
        gc.drawLine(f.getCaretLocation.x, 0, f.getCaretLocation.x, 50)
        f.setBackgroundImage(null)
        f.setBackgroundImage(img)
      }
    })*/

    val lbl = new Button(shell, SWT.PUSH)
    lbl.setText("Hello world")
    lbl.addSelectionListener(new SelectionAdapter  {
      override def widgetSelected(e: SelectionEvent) {
        val sh = new Shell(display)
        sh.setBounds(200, 100, 200, 100)
        val lb = new Label(sh, SWT.CENTER)
        lb.setText("Fuck me")
        lb.pack()
        sh.open(shell)
      }
    })
    lbl.setBounds(100, 200, 60, 23)

    gt.setSearch(scala.List("remove", "removeAll", "move", "moveAll", "rewrite", "rewriteAll"))
    shell.open()
    while (!shell.isDisposed)
      if (!display.readAndDispatch())
        display.sleep
    display.dispose()
  }
}
