package com.sg.openTelemtryApp.delegates;

import com.sg.openTelemtryApp.telemetry.OpentElemetrySingletonFactory;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class AbstractDelegate {

  public static final String DELEGATE_VERSION = "911.0.0";

  private static Span parentSpan;
  private static LocalDateTime start = java.time.LocalDateTime.now();



  public void run() {
    System.out.println(this.getClass().getSimpleName() + " started");
    parentSpan = getTracer().spanBuilder(this.getClass().getSimpleName() + "Span").startSpan();
    parentSpan.setAttribute("start_time", start.toString());
    try (Scope ss = parentSpan.makeCurrent()) {
      parentSpan.setAttribute("method", "run");
      parentSpan.setAttribute("traceId", parentSpan.getSpanContext().getTraceId());
    }
  }

  public void close() {
    LocalDateTime end = java.time.LocalDateTime.now();
    parentSpan.setAttribute("end_time", end.toString());
    parentSpan.setAttribute("time_wasted", Duration.between(start, end).getSeconds() + "s");
    parentSpan.end();

    System.out.println(this.getClass().getSimpleName() + " finished");
  }

  public Tracer getTracer(){
      System.out.println("getTracer()" + this.getClass().getSimpleName());
      return OpentElemetrySingletonFactory.getInstance().getTracer(this.getClass().getSimpleName(), DELEGATE_VERSION);
    }

  protected Span getParentSpan(){
    return parentSpan;
  }

}
