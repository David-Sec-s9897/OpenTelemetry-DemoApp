package com.sg.openTelemtryApp.delegates;

import com.sg.openTelemtryApp.delegates.bpmn.EighthTestDelegate;
import com.sg.openTelemtryApp.delegates.bpmn.EleventhTestDelegate;
import com.sg.openTelemtryApp.delegates.bpmn.FifthTestDelegate;
import com.sg.openTelemtryApp.delegates.bpmn.FirstTestDelegate;
import com.sg.openTelemtryApp.delegates.bpmn.FourthTestDelegate;
import com.sg.openTelemtryApp.delegates.bpmn.NinthTestDelegate;
import com.sg.openTelemtryApp.delegates.bpmn.SecondTestDelegate;
import com.sg.openTelemtryApp.delegates.bpmn.SeventhTestDelegate;
import com.sg.openTelemtryApp.delegates.bpmn.SixthTestDelegate;
import com.sg.openTelemtryApp.delegates.bpmn.TenthTestDelegate;
import com.sg.openTelemtryApp.delegates.bpmn.ThirdTestDelegate;
import com.sg.openTelemtryApp.delegates.bpmn.TwelfthTestDelegate;
import java.util.List;

public class NewTaskCreator {

  public static final int MAX_SLEEP_BETWEEN_TASKS = 20000;

  List<AbstractDelegate> taskList = List.of(new FirstTestDelegate(), new SecondTestDelegate(), new ThirdTestDelegate(), new FourthTestDelegate(), new FifthTestDelegate(), new SixthTestDelegate(),
      new SeventhTestDelegate(), new EighthTestDelegate(), new NinthTestDelegate(), new TenthTestDelegate(), new EleventhTestDelegate(), new TwelfthTestDelegate());

  public NewTaskCreator() {
    System.out.println("New Task started....");
  }

  public void runRandomNumberOfTasks() throws InterruptedException {
    int nunberOfMethods = (int) (Math.random() * 10);
    System.out.println(String.format("%d of delegates will be started", nunberOfMethods));
    for (int i = 0; i < nunberOfMethods; i++) {
      AbstractDelegate task = taskList.get(i);
      task.run();
      Thread.sleep((int) (Math.random() * MAX_SLEEP_BETWEEN_TASKS));
      task.close();
    }
    finish();
  }

  private static void finish() {
    System.out.println(".... FINISHED");

  }
}
