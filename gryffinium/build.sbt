name := """Gryffinium"""
organization := "com.quentinforestier"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.13.8"

libraryDependencies += guice

libraryDependencies ++= Seq(
  jdbc,
  "org.postgresql" % "postgresql" % "42.3.3",
)

libraryDependencies += "org.mindrot" % "jbcrypt" % "0.4"



libraryDependencies += javaForms