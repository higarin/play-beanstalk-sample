name := "PlayBeanstalkSample"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)     

play.Project.playScalaSettings

lazy val distEb = TaskKey[Unit]("dist-eb", "")

distEb := {
  // Constants
  val targetFile = s"./target/${name.value}-${version.value}-eb.zip"
  val procFile   = "Procfile"
  val extensions = ".ebextensions"
  // Make Package
  val result = (packageBin in Universal).value
  val parent  = result.getParent
  val archive = Path.apply(result).base
  // Decompression
  IO.unzip(result, new File(s"$parent"))
  // Copy Files
  IO.copyFile(new File(procFile), new File(s"$parent/$archive/$procFile"))
  IO.copyDirectory(new File(extensions), new File(s"$parent/$archive/$extensions"))
  // Re-Compression
  IO.zip(Path.allSubpaths(new File(s"$parent/$archive")), new File(targetFile))
}
