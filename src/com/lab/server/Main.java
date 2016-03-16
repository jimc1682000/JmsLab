package com.lab.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class Main {
	private static ExecutorService executor = Executors.newFixedThreadPool(10);

	public static void main(String[] args) throws Exception {
		thread(new Producer(), false);
		thread(new Producer(), false);
		consumerFuture();
		// thread(new Consumer(), false);
		Thread.sleep(1000);
		// thread(new Consumer(), false);
		consumerFuture();
		thread(new Producer(), false);
		// thread(new Consumer(), false);
		consumerFuture();
		thread(new Producer(), false);
		Thread.sleep(1000);
		// thread(new Consumer(), false);
		consumerFuture();
		thread(new Producer(), false);
		// thread(new Consumer(), false);
		consumerFuture();
		// thread(new Consumer(), false);
		consumerFuture();
		thread(new Producer(), false);
		thread(new Producer(), false);
		Thread.sleep(1000);
		thread(new Producer(), false);
		// thread(new Consumer(), false);
		consumerFuture();
		// thread(new Consumer(), false);
		consumerFuture();
		thread(new Producer(), false);
		// thread(new Consumer(), false);
		consumerFuture();
		thread(new Producer(), false);
		// thread(new Consumer(), false);
		consumerFuture();
		thread(new Producer(), false);
		// thread(new Consumer(), false);
		consumerFuture();
		// thread(new Consumer(), false);
		consumerFuture();
		thread(new Producer(), false);
		executor.shutdown();
	}

	public static void thread(Runnable runnable, boolean daemon) {
		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}

	public static void consumerFuture() {
		Consumer consumer = new Consumer();
		CompletableFuture.supplyAsync((Supplier<String>) () -> {
			try {
				return consumer.start();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}, executor).whenComplete((results, ex) -> {
			if (ex == null) {
				System.out.println(results);
			} else {
				ex.printStackTrace();
			}
			try {
				consumer.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
