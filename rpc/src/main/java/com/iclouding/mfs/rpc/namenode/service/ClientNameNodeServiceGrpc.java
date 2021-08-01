package com.iclouding.mfs.rpc.namenode.service;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;

@javax.annotation.Generated("by gRPC proto compiler")
public class ClientNameNodeServiceGrpc {

  private ClientNameNodeServiceGrpc() {}

  public static final String SERVICE_NAME = "com.iclouding.mfs.rpc.namenode.ClientNameNodeService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<com.iclouding.mfs.rpc.namenode.model.MkDirRequest,
      com.iclouding.mfs.rpc.namenode.model.MkDirResponse> METHOD_MKDIR =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "com.iclouding.mfs.rpc.namenode.ClientNameNodeService", "mkdir"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.iclouding.mfs.rpc.namenode.model.MkDirRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.iclouding.mfs.rpc.namenode.model.MkDirResponse.getDefaultInstance()));

  public static ClientNameNodeServiceStub newStub(io.grpc.Channel channel) {
    return new ClientNameNodeServiceStub(channel);
  }

  public static ClientNameNodeServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ClientNameNodeServiceBlockingStub(channel);
  }

  public static ClientNameNodeServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ClientNameNodeServiceFutureStub(channel);
  }

  public static interface ClientNameNodeService {

    public void mkdir(com.iclouding.mfs.rpc.namenode.model.MkDirRequest request,
        io.grpc.stub.StreamObserver<com.iclouding.mfs.rpc.namenode.model.MkDirResponse> responseObserver);
  }

  public static interface ClientNameNodeServiceBlockingClient {

    public com.iclouding.mfs.rpc.namenode.model.MkDirResponse mkdir(com.iclouding.mfs.rpc.namenode.model.MkDirRequest request);
  }

  public static interface ClientNameNodeServiceFutureClient {

    public com.google.common.util.concurrent.ListenableFuture<com.iclouding.mfs.rpc.namenode.model.MkDirResponse> mkdir(
        com.iclouding.mfs.rpc.namenode.model.MkDirRequest request);
  }

  public static class ClientNameNodeServiceStub extends io.grpc.stub.AbstractStub<ClientNameNodeServiceStub>
      implements ClientNameNodeService {
    private ClientNameNodeServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ClientNameNodeServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientNameNodeServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ClientNameNodeServiceStub(channel, callOptions);
    }

    @java.lang.Override
    public void mkdir(com.iclouding.mfs.rpc.namenode.model.MkDirRequest request,
        io.grpc.stub.StreamObserver<com.iclouding.mfs.rpc.namenode.model.MkDirResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MKDIR, getCallOptions()), request, responseObserver);
    }
  }

  public static class ClientNameNodeServiceBlockingStub extends io.grpc.stub.AbstractStub<ClientNameNodeServiceBlockingStub>
      implements ClientNameNodeServiceBlockingClient {
    private ClientNameNodeServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ClientNameNodeServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientNameNodeServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ClientNameNodeServiceBlockingStub(channel, callOptions);
    }

    @java.lang.Override
    public com.iclouding.mfs.rpc.namenode.model.MkDirResponse mkdir(com.iclouding.mfs.rpc.namenode.model.MkDirRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MKDIR, getCallOptions(), request);
    }
  }

  public static class ClientNameNodeServiceFutureStub extends io.grpc.stub.AbstractStub<ClientNameNodeServiceFutureStub>
      implements ClientNameNodeServiceFutureClient {
    private ClientNameNodeServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ClientNameNodeServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientNameNodeServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ClientNameNodeServiceFutureStub(channel, callOptions);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<com.iclouding.mfs.rpc.namenode.model.MkDirResponse> mkdir(
        com.iclouding.mfs.rpc.namenode.model.MkDirRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MKDIR, getCallOptions()), request);
    }
  }

  private static final int METHODID_MKDIR = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ClientNameNodeService serviceImpl;
    private final int methodId;

    public MethodHandlers(ClientNameNodeService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_MKDIR:
          serviceImpl.mkdir((com.iclouding.mfs.rpc.namenode.model.MkDirRequest) request,
              (io.grpc.stub.StreamObserver<com.iclouding.mfs.rpc.namenode.model.MkDirResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final ClientNameNodeService serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder(SERVICE_NAME)
        .addMethod(
          METHOD_MKDIR,
          asyncUnaryCall(
            new MethodHandlers<
              com.iclouding.mfs.rpc.namenode.model.MkDirRequest,
              com.iclouding.mfs.rpc.namenode.model.MkDirResponse>(
                serviceImpl, METHODID_MKDIR)))
        .build();
  }
}