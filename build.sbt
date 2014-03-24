name := "vine"

version := "1.0-SNAPSHOT"


scalaVersion := "2.10.2" 

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
   filters,
   "org.specs2" %% "specs2" % "2.3.1" % "test",
   "junit" % "junit" % "4.11",
   "org.scala-sbt" % "test-interface" % "1.0",
  "org.scalaz" %% "scalaz-core" % "7.0.4",
 "ws.securesocial" %% "securesocial" % "2.1.3",
  "postgresql" % "postgresql" % "9.1-901.jdbc4"
)     

scalacOptions in Test ++= Seq("-Yrangepos")

scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.7", "-unchecked","-Ywarn-adapted-args", "-Xlint")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers ++= Seq(
  "typesafe" at "http://repo.typesafe.com/typesafe/repo",
  Resolver.url("typesafe-community", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns),
  Resolver.url("typesafe-community-snapshots", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns),
  Resolver.url("sbt-plugin-releases", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
)


play.Project.playScalaSettings
