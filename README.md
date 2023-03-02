# ScalaPB demo

Run the server: `sbt "runMain com.github.vsuharnikov.scalagrpcexample.HelloServer"`
Run a client: `sbt "runMain com.github.vsuharnikov.scalagrpcexample.HelloClient"` 

## grpcurl demo

```
$ grpcurl -plaintext 127.0.0.1:10000 list
com.github.vsuharnikov.scalagrpcexample.HelloWorld
grpc.reflection.v1alpha.ServerReflection

$ grpcurl -plaintext 127.0.0.1:10000 describe com.github.vsuharnikov.scalagrpcexample.HelloWorld
com.github.vsuharnikov.scalagrpcexample.HelloWorld is a service:
service HelloWorld {
  rpc SayHello ( .com.github.vsuharnikov.scalagrpcexample.SayHelloRequest ) returns ( .com.github.vsuharnikov.scalagrpcexample.SayHelloResponse );
}

$ grpcurl -plaintext 127.0.0.1:10000 describe com.github.vsuharnikov.scalagrpcexample.SayHelloRequest
com.github.vsuharnikov.scalagrpcexample.SayHelloRequest is a message:
message SayHelloRequest {
  string name = 1 [(.scalapb.field) = { scala_name:"name" }];
  string message = 2 [(.scalapb.field) = { scala_name:"message" }];
}

$ grpcurl -plaintext -d '{"name": "Bob", "message": "Greetings!"}' 127.0.0.1:10000 com.github.vsuharnikov.scalagrpcexample.HelloWorld/SayHello
{
  "message": "Hello, Bob!"
}
```
