package com.intuit.lab01;

import akka.actor.typed.ActorSystem;

public class Lab01Main {

	public static void main(String[] args) {
		ActorSystem<PortfolioCommand> portfolioActor = ActorSystem.create(PortfolioBehavior.create(), "portfolio");
		portfolioActor.tell(new PortfolioFetchCommand());
	}

}
