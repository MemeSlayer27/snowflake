ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.2"

lazy val root = (project in file("."))
  .settings(
    name := "scala3-demo-swing-ij",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
  )
