package com.sg.openTelemtryApp.delegates.bpmn;

import com.sg.openTelemtryApp.delegates.AbstractDelegate;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

public class TenthTestDelegate extends AbstractDelegate {
  private OpenTelemetry openTelemetry;
  public Tracer tracer;

  public TenthTestDelegate(OpenTelemetry openTelemetry) {
    this.openTelemetry = openTelemetry;
    this.tracer = openTelemetry.getTracer(this.getClass().getName(), DELEGATE_VERSION);
  }

  @Override
  public void run() {
    super.run();
  }

  @Override
  public Tracer getTracer() {
    return tracer;
  }
}
