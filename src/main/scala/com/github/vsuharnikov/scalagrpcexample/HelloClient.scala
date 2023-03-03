package com.github.vsuharnikov.scalagrpcexample

import com.github.vsuharnikov.scalagrpcexample.hello.{HelloWorldGrpc, SayHelloRequest, SayHelloResponse}
import io.grpc._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Promise}

object HelloClient {
  def main(args: Array[String]): Unit = {
    val port = args.headOption.fold(10000)(_.toInt)

    val channel = io.grpc.ManagedChannelBuilder
      .forAddress("127.0.0.1", port)
      .usePlaintext()
      .build()

    val request = SayHelloRequest()
      .withName("Alice")
      .withMessage("Hello, server!")

    val response = sendAndTrackHeaders(channel, request)
    // val response = sendSimple(channel, request)
    println(s"Got response: ${response.message}")
  }

  private def sendAndTrackHeaders(channel: Channel, request: SayHelloRequest): SayHelloResponse = {
    val sayHelloPromise = Promise[SayHelloResponse]()

    val call = channel.newCall(HelloWorldGrpc.METHOD_SAY_HELLO, CallOptions.DEFAULT)
    call.start(
      new ClientCall.Listener[SayHelloResponse] {
        override def onHeaders(headers: Metadata): Unit = {
          println(s"onHeaders: $headers")

          val headerName  = "X-MyHeader"
          val headerValue = Option(headers.get(Metadata.Key.of(headerName, Metadata.ASCII_STRING_MARSHALLER)))
          println(s"onHeaders: $headerName=$headerValue")
        }

        override def onMessage(message: SayHelloResponse): Unit = sayHelloPromise.success(message)
      },
      new Metadata()
    )
    call.sendMessage(request)
    call.halfClose()
    call.request(1)

    Await.result(sayHelloPromise.future, 1.second)
  }

  private def sendSimple(channel: Channel, request: SayHelloRequest): SayHelloResponse = {
    val asyncStub = HelloWorldGrpc.stub(channel)
    Await.result(asyncStub.sayHello(request), 1.second)
  }
}
