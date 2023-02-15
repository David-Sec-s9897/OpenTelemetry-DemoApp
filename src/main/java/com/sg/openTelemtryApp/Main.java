/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package com.sg.openTelemtryApp;

import com.sg.openTelemtryApp.delegates.NewTaskCreator;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A typical simple backing bean, that is backed to <code>helloworld.jsp</code>
 * 
 */
public class Main {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static final int SLEEP_BEFORE_THREADS = 60000; //new Task will start every 60 seconds

    public static void main(String[] args) {
        runThreads(SLEEP_BEFORE_THREADS);
    }

    private static void runThreads(int repeatInterval){
        Timer timer = new Timer();
        timer.schedule( new TimerTask()
        {
            public void run() {
                try {
                    new NewTaskCreator().runRandomNumberOfTasks();
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "InterruptedException {0}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }, 0, repeatInterval);
    }
}