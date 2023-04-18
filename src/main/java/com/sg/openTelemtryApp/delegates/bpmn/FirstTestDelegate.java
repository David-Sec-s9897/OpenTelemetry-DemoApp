package com.sg.openTelemtryApp.delegates.bpmn;

import com.sg.openTelemtryApp.delegates.AbstractDelegate;
import com.sg.openTelemtryApp.interceptor.HelloAdder;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceState;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.TracerProvider;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.annotation.Priority;
import javax.interceptor.Interceptor;
import javax.interceptor.Interceptors;

@HelloAdder
@Interceptor
@Priority(500)
public class FirstTestDelegate extends AbstractDelegate {

  public final Tracer tracer = getTracer();
  private static Span parentSpan;

  public FirstTestDelegate() {
  }

  @Override
  public void run() {
    super.run();
    parentSpan = super.getParentSpan();
    Span span = tracer.spanBuilder(getClass().getName() + "::run Span").setParent(Context.current().with(parentSpan)).startSpan();
    span.end();
    firstMethod();
    secondMethod();
    Span spanEnd = tracer.spanBuilder(getClass().getName() + "::run close Span").setParent(Context.current().with(parentSpan)).setSpanKind(SpanKind.SERVER).startSpan();
    spanEnd.end(LocalDateTime.now().toInstant(ZoneOffset.UTC));
    super.close();
  }

  @Interceptors(MetricsTrackingInterceptor.class)
  private void firstMethod() {
    Span span = tracer.spanBuilder(getClass().getName() + "::firstMethod Span").setParent(Context.current().with(parentSpan)).startSpan();
    subMethodOfFirstMethod(span);
    LocalDateTime start = java.time.LocalDateTime.now();
    span.setAttribute("start_time", start.toString());
    try (Scope ss = span.makeCurrent()) {
      LocalDateTime end = java.time.LocalDateTime.now();
      span.setAttribute("end_time", end.toString());
      span.setAttribute("traceId", span.getSpanContext().getTraceId());
      span.setAttribute("time_wasted", Duration.between(start, end).getSeconds() + "s");
      span.end();
    }
  }

  private void subMethodOfFirstMethod(Span span){
    tracer.spanBuilder(getClass().getName() + "::subMethodOfFirstMethod Span").setParent(Context.current().with(Span.wrap(SpanContext.create(span.getSpanContext().getTraceId(), span.getSpanContext().getSpanId(),
        TraceFlags.getSampled(), TraceState.getDefault())))).startSpan().end();
  }

  private void secondMethod(){
      Span span = tracer.spanBuilder(getClass().getName()+"::secondMethod Span").setParent(Context.current().with(parentSpan)).startSpan();
      LocalDateTime start = java.time.LocalDateTime.now();
      span.setAttribute("start_time", start.toString());
      try (Scope ss = span.makeCurrent()) {
        LocalDateTime end = java.time.LocalDateTime.now();
        span.setAttribute("end_time", end.toString());
        span.setAttribute("traceId", span.getSpanContext().getTraceId());
        span.setAttribute("time_wasted", Duration.between(start, end).getSeconds() + "s");
        span.end();
      }
  }
}
