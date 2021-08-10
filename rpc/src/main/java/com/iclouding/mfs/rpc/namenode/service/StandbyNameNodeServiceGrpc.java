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
public class StandbyNameNodeServiceGrpc {

  private StandbyNameNodeServiceGrpc() {}

  public static final String SERVICE_NAME = "com.iclouding.mfs.rpc.namenode.StandbyNameNodeService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest,
      com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse> METHOD_FETCH_EDIT_LOGS =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "com.iclouding.mfs.rpc.namenode.StandbyNameNodeService", "fetchEditLogs"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse.getDefaultInstance()));

  public static StandbyNameNodeServiceStub newStub(io.grpc.Channel channel) {
    return new StandbyNameNodeServiceStub(channel);
  }

  public static StandbyNameNodeServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new StandbyNameNodeServiceBlockingStub(channel);
  }

  public static StandbyNameNodeServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new StandbyNameNodeServiceFutureStub(channel);
  }

  public static interface StandbyNameNodeService {

    public void fetchEditLogs(com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest request,
        io.grpc.stub.StreamObserver<com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse> responseObserver);
  }

  public static interface StandbyNameNodeServiceBlockingClient {

    public com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse fetchEditLogs(com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest request);
  }

  public static interface StandbyNameNodeServiceFutureClient {

    public com.google.common.util.concurrent.ListenableFuture<com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse> fetchEditLogs(
        com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest request);
  }

  public static class StandbyNameNodeServiceStub extends io.grpc.stub.AbstractStub<StandbyNameNodeServiceStub>
      implements StandbyNameNodeService {
    private StandbyNameNodeServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StandbyNameNodeServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StandbyNameNodeServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StandbyNameNodeServiceStub(channel, callOptions);
    }

    @java.lang.Override
    public void fetchEditLogs(com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest request,
        io.grpc.stub.StreamObserver<com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_FETCH_EDIT_LOGS, getCallOptions()), request, responseObserver);
    }
  }

  public static class StandbyNameNodeServiceBlockingStub extends io.grpc.stub.AbstractStub<StandbyNameNodeServiceBlockingStub>
      implements StandbyNameNodeServiceBlockingClient {
    private StandbyNameNodeServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StandbyNameNodeServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StandbyNameNodeServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StandbyNameNodeServiceBlockingStub(channel, callOptions);
    }

    @java.lang.Override
    public com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse fetchEditLogs(com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_FETCH_EDIT_LOGS, getCallOptions(), request);
    }
  }

  public static class StandbyNameNodeServiceFutureStub extends io.grpc.stub.AbstractStub<StandbyNameNodeServiceFutureStub>
      implements StandbyNameNodeServiceFutureClient {
    private StandbyNameNodeServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StandbyNameNodeServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StandbyNameNodeServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StandbyNameNodeServiceFutureStub(channel, callOptions);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse> fetchEditLogs(
        com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_FETCH_EDIT_LOGS, getCallOptions()), request);
    }
  }

  private static final int METHODID_FETCH_EDIT_LOGS = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StandbyNameNodeService serviceImpl;
    private final int methodId;

    public MethodHandlers(StandbyNameNodeService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_FETCH_EDIT_LOGS:
          serviceImpl.fetchEditLogs((com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest) request,
              (io.grpc.stub.StreamObserver<com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse>) responseObserver);
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
      final StandbyNameNodeService serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder(SERVICE_NAME)
        .addMethod(
          METHOD_FETCH_EDIT_LOGS,
          asyncUnaryCall(
            new MethodHandlers<
              com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest,
              com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse>(
                serviceImpl, METHODID_FETCH_EDIT_LOGS)))
        .build();
  }
}
