package com.github.vsuharnikov.scalagrpcexample

import com.github.vsuharnikov.scalagrpcexample.hello.{HelloWorldGrpc, SayHelloRequest}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object HelloClient extends App {
  val channel = io.grpc.ManagedChannelBuilder
    .forAddress("127.0.0.1", 10000)
    .usePlaintext()
    .build()

  val greeter = SayHelloRequest()
    .withName("Alice")
    .withMessage("Hello, server!")

  val asyncStub = HelloWorldGrpc.stub(channel)
  val response  = Await.result(asyncStub.sayHello(greeter), 1.second)
  println(s"Got response: ${response.message}")
}
