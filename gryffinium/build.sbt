name := """Gryffinium"""
organization := "com.quentinforestier"

maintainer := "quentin.forestier@heig-vd.ch"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.13.11"

// Runtime Dependencies
libraryDependencies ++= Seq(
  // https://github.com/playframework/playframework/releases/2.8.15
  "com.google.inject" % "guice" % "7.0.0",
  "com.google.inject.extensions" % "guice-assistedinject" % "7.0.0",
  "net.jodah" % "typetools" % "0.6.3",

  jdbc,
  javaForms,
  "org.postgresql" % "postgresql" % "42.6.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.15.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.15.2",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % "2.15.2",
  "org.mindrot" % "jbcrypt" % "0.4"
)

// Test Dependencies
libraryDependencies += "org.mockito" % "mockito-core" % "5.4.0" % "test"

// Dependencies Check Directives
dependencyUpdatesFailBuild := true
