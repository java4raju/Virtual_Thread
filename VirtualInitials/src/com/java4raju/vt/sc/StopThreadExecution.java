package com.java4raju.vt.sc;

import java.time.Duration;

public class StopThreadExecution {

	public static void main(String[] args) throws InterruptedException {

		Thread t = new Thread(new Task());
		t.start();
		Thread.sleep(Duration.ofSeconds(11));
		t.interrupt();

	}

}

class Task implements Runnable {

	@Override
	public void run() {

		try {
			Thread.sleep(Duration.ofSeconds(10));
			System.out.println("--Exucuting Task--");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
