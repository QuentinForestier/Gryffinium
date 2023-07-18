
// Keep the line below until https://github.com/playframework/playframework/issues/11522 has been fixed
ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.19")

// Defines scaffolding (found under .g8 folder)
// http://www.foundweekends.org/giter8/scaffolding.html
// sbt "g8Scaffold form"
addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.13.1")

addSbtPlugin("com.typesafe.play" % "sbt-play-ebean" % "6.2.0")

// Dependencies check
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.4")
