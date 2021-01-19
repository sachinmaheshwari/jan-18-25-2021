package com.intuit.lab01;

public class PortfolioFetchCommand extends PortfolioCommand {}

class PortfolioCompleteCheckCommand extends PortfolioCommand {}

class PortfolioItemFetchedCommand extends PortfolioCommand {
	private String symbol;
	private double price;
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
