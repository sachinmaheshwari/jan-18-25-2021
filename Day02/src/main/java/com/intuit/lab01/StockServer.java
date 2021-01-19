package com.intuit.lab01;

public class StockServer {

	public static double getPrice(String symbol) {
		//Time consuming operation
		try {
			Thread.sleep((int)Math.random() * 10);
		}
		catch(Exception ex) {}
		double price = Math.random() * 1000;
		return price;
	}

}