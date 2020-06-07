package mz.co.diasmetano.vm.thread;

import mz.co.diasmetano.vm.service.NumberService;

public class TaskControlThreadTimeWaint implements Runnable {
	

	public TaskControlThreadTimeWaint() {


	}

	public void run() {
		
		try {

			while (true) {
				for (int i = 0; i < NumberService.futures().size(); i++) {
					
					if (
							NumberService.tasks().get(i).getWaiting()> NumberService.tasks().get(i).x_max_wait() 
							&& NumberService.tasks().get(i).stat().equals("pending") 
							&& (NumberService.tasks().get(i).x_max_wait() != 0
							&& NumberService.tasks().get(i).x_max_wait() >= 31)
						) {
						NumberService.futures().get(i).cancel(true);
						System.out.println(NumberService.tasks().get(i).getNumber()+ " CANCELED! \nAfter "+ NumberService.tasks().get(i).x_max_wait() +" waiting.");
						NumberService.tasks().get(i).setStat("canceled");
					}
				}
			
				Thread.sleep(1000);
			}

		
		}

		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	

}