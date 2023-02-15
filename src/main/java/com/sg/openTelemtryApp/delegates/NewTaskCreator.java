package com.sg.openTelemtryApp.delegates;

import com.sg.openTelemtryApp.delegates.bpmn.*;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Scope;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;

public class NewTaskCreator {

    public static final int MAX_SLEEP_BETWEEN_TASKS = 20000;

/*    ManagedChannel jaegerChannel = ManagedChannelBuilder.forAddress("localhost", 3336)
            .usePlaintext()
            .build();*/

    JaegerGrpcSpanExporter jaegerExporter =  JaegerGrpcSpanExporter.builder()
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

    OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
            .setTracerProvider(tracerProvider)
            .setMeterProvider(sdkMeterProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .build();

    List<AbstractDelegate> taskList = List.of(new FirstTestDelegate(openTelemetry), new SecondTestDelegate(openTelemetry), new ThirdTestDelegate(openTelemetry), new FourthTestDelegate(openTelemetry), new FifthTestDelegate(openTelemetry), new SixthTestDelegate(openTelemetry),
            new SeventhTestDelegate(openTelemetry), new EighthTestDelegate(openTelemetry), new NinthTestDelegate(openTelemetry), new TenthTestDelegate(openTelemetry), new EleventhTestDelegate(openTelemetry), new TwelfthTestDelegate(openTelemetry));

    public NewTaskCreator() {
        System.out.println("New Task started....");
    }

    public void runRandomNumberOfTasks() throws InterruptedException {
        int nunberOfMethods = (int) (Math.random() * 10);
        System.out.println(String.format("%d of delegates will be started", nunberOfMethods));
        for (int i = 0; i < nunberOfMethods; i++) {
            AbstractDelegate task = taskList.get(i);
            String taskName = taskList.get(i).getClass().toString();
            task.run();
            Span span = task.getTracer().spanBuilder(taskName+"Span").startSpan();
            LocalDateTime start = java.time.LocalDateTime.now();
            span.setAttribute("start_time", start.toString());
            try (Scope ss = span.makeCurrent()) {
                span.setAttribute("method", "sleep");
                Thread.sleep((int) (Math.random() * MAX_SLEEP_BETWEEN_TASKS));
                LocalDateTime end = java.time.LocalDateTime.now();
                span.setAttribute("end_time", end.toString());
                span.setAttribute("time_wasted", Duration.between(start, end).getSeconds() + "s");
            }finally {
                span.end();
                task.close();
            }
        }
        finish();
    }

    private static void finish() {
        System.out.println(".... FINISHED");

    }
}
