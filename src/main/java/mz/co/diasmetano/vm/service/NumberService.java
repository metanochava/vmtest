package mz.co.diasmetano.vm.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import mz.co.diasmetano.vm.anotation.DelayMe;
import mz.co.diasmetano.vm.db.Store;
import mz.co.diasmetano.vm.model.Number;
import mz.co.diasmetano.vm.thread.Task;
import mz.co.diasmetano.vm.thread.TaskControlThreadTimeWaint;

public class NumberService {

	static int MAX_T = 1;
	static ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
	static List<Future<?>> futures = new ArrayList<Future<?>>();
	static List<Task> tasks = new ArrayList<Task>();
	long maxWait = 0;
	int totalPeding = 0;
	long minWait = 0;
	boolean TaskControl = true;
	private int min_time_process = 30;

	public static List<Future<?>> futures() {

		return futures;
	}

	public static List<Task> tasks() {
		return tasks;
	}

	
	public String random_(long time_wait, String x_max_wait) {
		delayMe();
		Random gerador = new Random();
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		long created_at = new Date().getTime();
		String number = (gerador.nextInt(100) + 4) + "";

		Task gn = new Task(id, number, created_at, time_wait, this.min_time_process);

		tasks.add(gn);

		Future<?> f = NumberService.pool.submit(gn);

		NumberService.futures.add(f);

		if (x_max_wait != null)
			return "Operation submited. the system will process number " + gn.getNumber() + " and  X-Max-Wait " + gn.x_max_wait();
		else
			return "Operation submited. the system will process number " + gn.getNumber();

	}
	
	public String random(String x_max_wait) {
		delayMe();
		String res = "";

		long time_wait = 0;
		try {
			time_wait = Integer.parseInt(x_max_wait);
			if (time_wait < 31) {

				return "Operation was ignored  because the X-Max-Wait is less than 31";

			} else {
				res = random_(time_wait, x_max_wait);

			}
		} catch (Exception e) {

			res = random_(time_wait, x_max_wait);
			
		}
		
		if (TaskControl) {
			new Thread(new TaskControlThreadTimeWaint()).start();
			TaskControl = false;
		}

		return res;
	}

	@DelayMe(time=2)
	public List<Number> history() {
		delayMe();
		return Store.getNumbers();
	}

	
	public String cancel(String requestid) {
		delayMe();
		String result = requestid + "\nNot found";
		for (int i = 0; i < NumberService.futures.size(); i++) {

			if (NumberService.tasks.get(i).getRequestID().equals(requestid)) {
				
				if(NumberService.tasks.get(i).stat().equals("canceled")) {
					
					result = "ID:  " + requestid +" is alredy canceled! " ;
					break;
				}else {
					NumberService.futures.get(i).cancel(true);
					NumberService.tasks.get(i).setStat("canceled");
					result = " Canceled ID:  " + requestid;
					break;
				}
				
			}
		}

		return result;
	}

	public String stats() {
		delayMe();
		long time;
		try {
			time = (int) NumberService.tasks.get(0).waitTime();
		} catch (Exception e) {
			return "the system does not have any task submission yet";
		}

		this.maxWait = time;
		this.totalPeding = 0;
		this.minWait = time;

		for (int i = 0; i < NumberService.futures.size(); i++) {
			time = (int) NumberService.tasks.get(i).waitTime();
			
			if (NumberService.futures.get(i).isDone()) {
				if (maxWait < time) {
					maxWait = time;
				}
	
				if (minWait > time) {
					minWait = time;
				}
			}

			if (!NumberService.futures.get(i).isDone() && (NumberService.tasks.get(i).stat()=="pending")) {
				totalPeding++;
			}
		}

		String response = "Max Wait =>" + maxWait + " \n" + "Min Wait =>" + minWait + " \n" + "Total pendig =>"
				+ totalPeding;

		return response;
	}

	
	public List<Task> pending() {
		delayMe();
		List<Task> tasks = new ArrayList<Task>();

		for (int i = 0; i < NumberService.futures.size(); i++) {

			if (!NumberService.futures.get(i).isDone() && (NumberService.tasks.get(i).stat() == "pending")) {
				tasks.add(NumberService.tasks.get(i));
			}
		}
		return tasks;
	}

	@DelayMe(time = 3)
	public String threads(String value) {
		delayMe();
		try {
			int max_pool = Integer.parseInt(value);
			if (max_pool > 10 || max_pool < 1) {

				return "The System ignored your submition because  the value is out of this range [1 ... 10]";
			} else {
				this.pool = Executors.newFixedThreadPool(max_pool);
				return "poll size cheged to " + max_pool;

			}
		} catch (Exception e) {
			return " Is not a int value. \n Pleace insert a int value ";
		}

	}



	public void delayMe() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		StackTraceElement element = stackTrace[2];

		for (Method method : this.getClass().getDeclaredMethods()) {
			if (method.getName().equals(element.getMethodName())) {

				if (method.isAnnotationPresent(DelayMe.class)) {
					DelayMe delayMe = method.getAnnotation(DelayMe.class);
					try {
						Thread.sleep(delayMe.time() * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		}

	}

}
