package com.sg.openTelemtryApp.delegates.bpmn;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.Transactional;

@Transactional
@Interceptor
public class MetricsTrackingInterceptor {

  @AroundInvoke
  public Object aroundInvoke(InvocationContext invocationContext) throws Exception {
    System.out.println("aroundInvoke() started");
    return invocationContext.proceed();

  }

}
