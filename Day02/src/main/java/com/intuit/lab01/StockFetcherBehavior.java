package com.intuit.lab01;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class StockFetcherBehavior extends AbstractBehavior<StockFetcherCommand> {

	public StockFetcherBehavior(ActorContext<StockFetcherCommand> context) {
		super(context);
	}

	public static Behavior<StockFetcherCommand> create() {
		return Behaviors.setup(StockFetcherBehavior::new);
	}

	@Override
	public Receive<StockFetcherCommand> createReceive() {
		return newReceiveBuilder()
				.onMessage(StockFetcherCommand.class, this::onFetch)
				.build();
	}
	
	private Behavior<StockFetcherCommand> onFetch(StockFetcherCommand cmd) {
		// talk to the stock service
		double price = StockServer.getPrice(cmd.getSymbol());
		PortfolioItemFetchedCommand portCmd = new PortfolioItemFetchedCommand();
		portCmd.setPrice(price);
		portCmd.setSymbol(cmd.getSymbol());
		cmd.getPortfolioAgent().tell(portCmd);
		return Behaviors.stopped();
	}

}
