package com.nec.strudel.workload.exec.batch;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.json.JsonObject;

import org.apache.log4j.Logger;

import com.nec.strudel.Closeable;
import com.nec.strudel.workload.exec.Report;
import com.nec.strudel.workload.exec.WorkExec;
import com.nec.strudel.workload.state.WorkState;
import com.nec.strudel.workload.util.TimeValue;

public class BatchExec extends WorkExec {
	private static final Logger LOGGER = Logger.getLogger(BatchExec.class);
	private final WorkState state;
	private final WorkThread[] workThreads;
	private final ExecutorService exec;
	private volatile boolean failed = false;

	public BatchExec(WorkState state, WorkThread[] workThreads,
			Closeable[] closeables) {
		super(workThreads.length, closeables);
		this.state = state;
		this.workThreads = workThreads;
		this.exec = Executors.newFixedThreadPool(workThreads.length);
	}
	public WorkThread[] getWorkThreads() {
		return workThreads;
	}

	@Override
	public String getState() {
		boolean allDone = true;
		for (WorkThread t : workThreads) {
			if (t.isDone()) {
				if (!t.isSuccessful()) {
					fail();
				}
			} else {
				allDone = false;
			}
		}
		if (this.failed) {
			state.fail();
		} else if (allDone) {
			state.done();
		}
		return state.getState();
	}
	@Override
	public Report getReport() {
		Report[] reports = new Report[workThreads.length];
		for (int i = 0; i < workThreads.length; i++) {
			reports[i] = workThreads[i].getReport();
		}
		return Report.aggregate(reports);
	}

	@Override
	public void start(TimeValue slack) {
		if (state.start()) {
			int slackMS = (int) slack.toMillis();
			if (slackMS > 0) {
				Random rand = new Random();
				for (WorkThread w : workThreads) {
					int wait = rand.nextInt(slackMS);
					exec.execute(new StartSlackRunner(
							TimeValue.milliseconds(wait), w));
				}
			} else {
				for (WorkThread w : workThreads) {
					exec.execute(new StartSlackRunner(w));
				}
			}
			/**
			 * no further tasks accepted:
			 */
			exec.shutdown();
		}
	}
	@Override
	public void operate(String name, JsonObject data) {
		state.operate(name, data);
	}
	@Override
	public synchronized void stop() {
		if (state.stop()) {
			for (WorkThread w : workThreads) {
				w.stop();
			}
		}
	}
	@Override
	public synchronized boolean terminate(long timeout, TimeUnit unit)
			throws InterruptedException {
		if (state.isRunning()) {
			stop();
		}
		if (state.terminate()) {
			return exec.awaitTermination(timeout, unit);
		} else {
			return true;
		}
	}
	protected void fail() {
		failed = true;
	}
	
	public static WorkExec create(WorkState state, WorkThread[] workThreads,
			Closeable... closeables) {
		return new BatchExec(state, workThreads, closeables);
	}
	/**
	 * for unit tests
	 */
	public static WorkThread[] findWorkThreads(WorkExec w) {
		if (w instanceof BatchExec) {
			return ((BatchExec) w).getWorkThreads();
		}
		return null;
	}

	protected class StartSlackRunner implements Runnable {
		private final Runnable task;
		private final long waitMS;
		StartSlackRunner(TimeValue wait, Runnable task) {
			this.waitMS = wait.toMillis();
			this.task = task;
		}
		StartSlackRunner(Runnable task) {
			this.waitMS = 0;
			this.task = task;
		}
		@Override
		public void run() {
			if (waitMS > 0) {
				try {
					Thread.sleep(waitMS);
				} catch (InterruptedException e) {
					return;
				}
			}
			try {
				task.run();
			} catch (Exception e) {
				LOGGER.error("work thread failed", e);
				fail();
			}
		}
	}
}