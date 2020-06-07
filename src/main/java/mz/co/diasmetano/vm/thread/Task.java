package mz.co.diasmetano.vm.thread; 

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import mz.co.diasmetano.vm.db.Store;
import mz.co.diasmetano.vm.model.Number;

public class Task implements Runnable {
	
	private String requestId;
	private String number;
	private long created_at;
	private long start_process_at;
	private String stat = "pending";
	private String submition_time;
	private long x_max_wait = 0;
	private int min_time_process;

	public Task(String requestId, String number, long created_at, long x_max_wait, int min_time_process) {

		this.requestId = requestId;
		this.number = number;
		this.created_at = created_at;
		this.x_max_wait = x_max_wait;

		Date d = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
		submition_time = ft.format(d).toString();

		System.out.println(" ---------------------------------- submited " + number + " at " + submition_time+ " -------------------------------------");

	}

	public void run() {
		this.stat = "nuning";
		Number num = new Number();
		Random gerador = new Random();
		this.start_process_at = new Date().getTime();
		try {
			SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
			Date d = new Date();
			String start_process = ft.format(d).toString();
			System.out.println("-----------------------------------------------------------------------> " + number
					+ " start at " + start_process);
			long initialize_process = new Date().getTime();

			num.setRequestId(requestId);

			if (min_time_process < 30) {
				min_time_process = 30;
			}
			
				Thread.sleep(1000 * (gerador.nextInt(3) + min_time_process));
			
			

			long finalize_process = new Date().getTime();

			String end_process = ft.format(d).toString();
			long process_time = ((finalize_process - initialize_process) / 1000);
			String sms =number + " complete |created at " + created_at + " |  wait time  " + waitTime()
					+ " | total time process " + process_time + " | ending process " + end_process;
			num.setProcessTime(process_time + " secunds "+sms);
			num.setNumber(Integer.parseInt(number));
			new Store(num);
			this.stat = "done";
		}

		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public long waitTime() {
		long result = ((this.start_process_at - created_at) / 1000);
		if(result<0) {
			result=0;
		}
		return result;
	}

	public long getWaiting() {
		return ((new Date().getTime() - created_at) / 1000);
	}

	public String getRequestID() {
		return requestId;
	}

	public long x_max_wait() {
		return x_max_wait;
	}

	public String getNumber() {
		return number;
	}

	public long Created_at() {
		return created_at;
	}

	public String stat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String Submition_time() {
		return submition_time;
	}
	
	public long getX_max_wait() {
		return x_max_wait;
	}


}