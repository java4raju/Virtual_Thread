package org.java4raju.virtual.process;

import java.time.Duration;
import java.time.Instant;

import org.java4raju.virtual.task.CPUIntensiveTask;
import org.java4raju.virtual.task.IOIntensiveTask;

public class ThreadExecution {

	public Long executeUsingPlatformThread(int iterations, long sleepTime, boolean isIOIntensive) {
		Thread threads[] = new Thread[iterations];

		Instant actionStart = Instant.now();
 
		for (int i = 0; i < iterations; i++) {
			threads[i] = new Thread(() -> {

				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(isIOIntensive) {
				IOIntensiveTask ioIntensive = new IOIntensiveTask();
				ioIntensive.runIOIntensiveTask("C:\\Users\\t0265602\\Documents\\execution\\poc\\reactiveJava\\Virtual_IOvsCPU\\resources\\File.txt");
				}
				else {
				CPUIntensiveTask cpuInt = new CPUIntensiveTask();
				cpuInt.runCPUIntensiveTask();
				}

			});
			threads[i].start(); // Start the thread
		}

		for (Thread thread : threads) {
			try {
				thread.join(); // Wait for thread to complete
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return Duration.between(actionStart, Instant.now()).toMillis();
	}
}
