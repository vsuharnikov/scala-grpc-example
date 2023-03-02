import scalapb.compiler.Version.scalapbVersion
import scalapb.compiler.Version.grpcJavaVersion

name         := "scala-grpc-example"
version      := "0.1"
scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "scalapb-runtime"      % scalapbVersion % "protobuf",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion,
  "io.grpc"               % "grpc-netty"           % grpcJavaVersion
)

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value
)

// Ctrl-C
run / fork         := true
run / connectInput := true
