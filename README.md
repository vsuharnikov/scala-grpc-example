# ScalaPB demo

Run the server on 10000 port: `sbt "runMain com.github.vsuharnikov.scalagrpcexample.HelloServer"`

## Optional - Nginx

Optionally we can run Nginx proxy on 10001 port to add response headers. We can get them later.

To run Nginx:
- `nix-shell -p nginx --run ./nginx-run.sh`
- or just `./nginx-run.sh` if you have an installed Nginx.

## Testing

### With Scala client

- asking a default (10000) port `sbt "runMain com.github.vsuharnikov.scalagrpcexample.HelloClient"`
- asking 10001 port `sbt "runMain com.github.vsuharnikov.scalagrpcexample.HelloClient 10001"`

### With grpcurl client

#### A list of available services

```
$ grpcurl -plaintext 127.0.0.1:10000 list
com.github.vsuharnikov.scalagrpcexample.HelloWorld
grpc.reflection.v1alpha.ServerReflection
```

#### Describe a service

```
$ grpcurl -plaintext 127.0.0.1:10000 describe com.github.vsuharnikov.scalagrpcexample.HelloWorld
com.github.vsuharnikov.scalagrpcexample.HelloWorld is a service:
service HelloWorld {
  rpc SayHello ( .com.github.vsuharnikov.scalagrpcexample.SayHelloRequest ) returns ( .com.github.vsuharnikov.scalagrpcexample.SayHelloResponse );
}
```

#### Describe a message

```
$ grpcurl -plaintext 127.0.0.1:10000 describe com.github.vsuharnikov.scalagrpcexample.SayHelloRequest
com.github.vsuharnikov.scalagrpcexample.SayHelloRequest is a message:
message SayHelloRequest {
  string name = 1 [(.scalapb.field) = { scala_name:"name" }];
  string message = 2 [(.scalapb.field) = { scala_name:"message" }];
}
```

#### Send an unary request

```
$ grpcurl -plaintext -d '{"name": "Bob", "message": "Greetings!"}' 127.0.0.1:10000 com.github.vsuharnikov.scalagrpcexample.HelloWorld/SayHello
{
  "message": "Hello, Bob!"
}
```

#### Debug 10001 port

```
$ grpcurl -v -plaintext -d '{"name": "Bob", "message": "Greetings!"}' 127.0.0.1:10001 com.github.vsuharnikov.scalagrpcexample.HelloWorld/SayHello

Resolved method descriptor:
rpc SayHello ( .com.github.vsuharnikov.scalagrpcexample.SayHelloRequest ) returns ( .com.github.vsuharnikov.scalagrpcexample.SayHelloResponse );

Request metadata to send:
(empty)

Response headers received:
content-type: application/grpc
date: Fri, 03 Mar 2023 07:53:14 GMT
grpc-accept-encoding: gzip
server: nginx/1.22.1
x-myheader: My value

Response contents:
{
  "message": "Hello, Bob!"
}

Response trailers received:
(empty)
Sent 1 request and received 1 response
```
