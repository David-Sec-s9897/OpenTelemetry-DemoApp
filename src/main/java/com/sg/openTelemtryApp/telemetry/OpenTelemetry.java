package com.sg.openTelemtryApp.telemetry;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

public class OpenTelemetry {


  private static OpenTelemetry single_instance = null;

  /*    ManagedChannel jaegerChannel = ManagedChannelBuilder.forAddress("localhost", 3336)
            .usePlaintext()
            .build();*/

  private static JaegerGrpcSpanExporter jaegerExporter =  JaegerGrpcSpanExporter.builder()
      .setEndpoint("http://localhost:14250")
      //.setDeadlineMs(3000)
      .build();

  Resource resource = Resource.getDefault()
      .merge(Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "logical-service-name")));

  IdGenerator idGenerator = IdGenerator.random();

 SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
      .addSpanProcessor(BatchSpanProcessor.builder(jaegerExporter).build())
      .setResource(resource).setIdGenerator(idGenerator)
      .build();

  SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
      .addSpanProcessor(BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().build()).build())
      .setResource(resource)
      .build();

  SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
      .registerMetricReader(PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder().build()).build())
      .setResource(resource)
      .build();

  io.opentelemetry.api.OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
      .setTracerProvider(tracerProvider)
      .setMeterProvider(sdkMeterProvider)
      .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
      .build();


  public io.opentelemetry.api.OpenTelemetry getInstance() {
    return openTelemetry;
  }
}
