package com.github.vsuharnikov.scalagrpcexample

import com.github.vsuharnikov.scalagrpcexample.hello.{HelloWorldGrpc, SayHelloRequest, SayHelloResponse}
import io.grpc.netty.NettyServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService

import scala.concurrent._

object HelloServer extends App {
  class HelloService extends HelloWorldGrpc.HelloWorld {
    override def sayHello(request: SayHelloRequest): Future[SayHelloResponse] = {
      println(s"Got sayHello \"${request.message}\" from ${request.name}")
      Future.successful(SayHelloResponse(message = s"Hello, ${request.name}!"))
    }
  }

  val server = NettyServerBuilder
    .forPort(10000)
    .addService(HelloWorldGrpc.bindService(new HelloService, ExecutionContext.global))
    .addService(ProtoReflectionService.newInstance()) // Optional
    .build
    .start

  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run(): Unit = server.shutdown()
  })

  println("Listening 127.0.0.1:10000...")
  server.awaitTermination()
}
