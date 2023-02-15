package com.sg.openTelemtryApp.telemetry;

import io.opentelemetry.api.OpenTelemetry;

public class OpentElemetrySingletonFactory {

  private static OpenTelemetry openTelemetryInstance;

  public static OpenTelemetry getInstance(){
    if (openTelemetryInstance == null){
      openTelemetryInstance = new com.sg.openTelemtryApp.telemetry.OpenTelemetry().getInstance();
    }
    return openTelemetryInstance;
  }


}
