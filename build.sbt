name := "readerT-DI"
version := "0.1"

scalaVersion := "2.13.1"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.1.1"
libraryDependencies += "org.typelevel" %% "cats-effect" % "2.1.2"
libraryDependencies += "co.fs2" %% "fs2-core" % "2.2.1"
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
