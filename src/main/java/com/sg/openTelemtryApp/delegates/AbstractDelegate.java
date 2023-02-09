package com.sg.openTelemtryApp.delegates;

public abstract class AbstractDelegate {

  public void run(){
    System.out.println(this.getClass().getSimpleName() + " started");
  }

  public void close() {
    System.out.println(this.getClass().getSimpleName() + " finished");
  }
}
