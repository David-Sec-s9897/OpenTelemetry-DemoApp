package com.sg.openTelemtryApp.delegates;

import io.opentelemetry.api.trace.Tracer;

public abstract class AbstractDelegate {

  public static final String DELEGATE_VERSION = "1.0.0";

  public void run(){
    System.out.println(this.getClass().getSimpleName() + " started");
  }

  public void close() {
    System.out.println(this.getClass().getSimpleName() + " finished");
  }

  public  Tracer getTracer(){return null;}
}
