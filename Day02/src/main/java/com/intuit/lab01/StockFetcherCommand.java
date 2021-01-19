package com.intuit.lab01;

import akka.actor.typed.ActorRef;

public class StockFetcherCommand {

	private ActorRef<PortfolioCommand> portfolioAgent;
	private String symbol;
	public ActorRef<PortfolioCommand> getPortfolioAgent() {
		return portfolioAgent;
	}
	public void setPortfolioAgent(ActorRef<PortfolioCommand> portfolioAgent) {
		this.portfolioAgent = portfolioAgent;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	

}
