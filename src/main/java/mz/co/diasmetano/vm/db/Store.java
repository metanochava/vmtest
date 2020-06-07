package mz.co.diasmetano.vm.db;

import java.util.ArrayList;
import java.util.List;

import mz.co.diasmetano.vm.model.Number;

public class Store {
	
	public static List<Number> numbers = new ArrayList<Number>();

	public static List<Number> getNumbers() {
		return numbers;
	}


	
	public Store(Number number) {
		System.out.println("\n Storing number "+number.getNumber()+"\n");
		Store.numbers.add(number);
	}
	
	
}
