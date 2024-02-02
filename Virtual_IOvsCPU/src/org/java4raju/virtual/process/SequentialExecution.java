package org.java4raju.virtual.process;

import java.time.Duration;
import java.time.Instant;

import org.java4raju.virtual.task.CPUIntensiveTask;
import org.java4raju.virtual.task.IOIntensiveTask;

public class SequentialExecution {
	public Long executeSequentially(int iterations, long sleepTime, boolean isIOIntensive) {
		Instant actionStart = Instant.now();

		// Create and start multiple threads in a loop
		for (int i = 0; i < iterations; i++) {

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(isIOIntensive) {
			IOIntensiveTask ioInt = new IOIntensiveTask();
			ioInt.runIOIntensiveTask("C:\\Users\\t0265602\\Documents\\execution\\poc\\reactiveJava\\Virtual_IOvsCPU\\resources\\File.txt");
			}
			else {
			CPUIntensiveTask cpuInt = new CPUIntensiveTask();
			cpuInt.runCPUIntensiveTask();
			}
		}
		return Duration.between(actionStart, Instant.now()).toMillis();
	}
}
