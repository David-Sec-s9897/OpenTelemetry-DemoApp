package com.sg.openTelemtryApp.delegates.bpmn;

import com.sg.openTelemtryApp.delegates.AbstractDelegate;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class TwelfthTestDelegate extends AbstractDelegate {

  @Override
  public void run() {
    super.run();
    Span parentSpan = super.getParentSpan();

    Span span = getTracer().spanBuilder(getClass().getName() + "::run error Span").setParent(Context.current().with(parentSpan)).startSpan();
    span.setAttribute("foo", "bar");
    int a = 1;
    int b  =0;
    try {
      int res  = a / b;
      System.out.println(res);
    }
    catch (ArithmeticException e){
      System.out.println(e.getMessage());
      span.setStatus(StatusCode.ERROR);
      span.recordException(e, Attributes.of(AttributeKey.longArrayKey("params to divide function"), List.of(Long.valueOf(a),Long.valueOf(b))));
    }
    finally {
      span.end();
    }

  }
}
