name := "spark-scala-application"
version := "0.0.0-SNAPSHOT"
organization := "thw.vancann"

scalaVersion := "2.11.11"
val sparkVersion = "2.2.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core"                  % sparkVersion % Provided,
  "org.apache.spark" %% "spark-sql"                   % sparkVersion % Provided,
  "com.github.scopt" %% "scopt"                       % "3.3.0"      % Compile,
  "org.scalatest"    %% "scalatest"                   % "2.2.1"      % "test, it"
)

// test run settings
//parallelExecution in Test := false
test in assembly := {}

// Enable integration tests
Defaults.itSettings
lazy val root = project.in(file(".")).configs(IntegrationTest)

// Measure time for each test
testOptions in Test += Tests.Argument("-oD")
testOptions in IntegrationTest += Tests.Argument("-oD")

// Scoverage settings
coverageExcludedPackages := "<empty>;.*storage.*"
coverageMinimum := 70
coverageFailOnMinimum := true
