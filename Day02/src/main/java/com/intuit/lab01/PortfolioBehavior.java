package com.intuit.lab01;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class PortfolioBehavior extends AbstractBehavior<PortfolioCommand> { 

	private Map<String, Double> symbols = new HashMap<String, Double>();
	
	public PortfolioBehavior(ActorContext<PortfolioCommand> context) {
		super(context);
		this.symbols.put("HP", -1.0);
		this.symbols.put("IBM", -1.0);
		this.symbols.put("INTUIT", -1.0);
		this.symbols.put("APPL", -1.0);
		this.symbols.put("FB", -1.0);
		this.symbols.put("RIL", -1.0);
		this.symbols.put("TCS", -1.0);
		this.symbols.put("INFY", -1.0);
		this.symbols.put("MS", -1.0);
		this.symbols.put("AZ", -1.0);
	}

	public static Behavior<PortfolioCommand> create() {
		return Behaviors.setup(PortfolioBehavior::new);
	}

	@Override
	public Receive<PortfolioCommand> createReceive() {
		return newReceiveBuilder()
				.onMessage(PortfolioItemFetchedCommand.class, this::onPortfolioItemFetched)
				.onMessage(PortfolioFetchCommand.class, this::onFetchPortfolio)
				.onMessage(PortfolioCompleteCheckCommand.class, this::onCheckPortfolioComplete)
				.build();
	}
	
	private Behavior<PortfolioCommand> onPortfolioItemFetched(PortfolioItemFetchedCommand cmd) {
		this.symbols.put(cmd.getSymbol(), cmd.getPrice());
		System.out.println("Fetched " + this.symbols.values().stream().filter(it -> it > 0.0).collect(Collectors.toList()).size() + " symbols");
		getContext().getSelf().tell(new PortfolioCompleteCheckCommand());
		return Behaviors.same(); //return this
	}
	
	private Behavior<PortfolioCommand> onFetchPortfolio(PortfolioFetchCommand portfolioFetchCmd) {
		this.symbols.forEach((k, v) -> {
			ActorRef<StockFetcherCommand> actor = getContext().spawn(StockFetcherBehavior.create(), k);
			StockFetcherCommand cmd = new StockFetcherCommand();
			cmd.setPortfolioAgent(getContext().getSelf());
			cmd.setSymbol(k);
			actor.tell(cmd);
		});
		return Behaviors.same(); //return this
	}
	
	private Behavior<PortfolioCommand> onCheckPortfolioComplete(PortfolioCompleteCheckCommand cmd) {
		if (this.symbols.values().stream().filter(it -> it < 0.0).collect(Collectors.toList()).size() > 0 ) {
			return Behaviors.same();
		}
		else {
			this.symbols.forEach((k, v) -> {
				System.out.println(k + "     " + v);
			});
		}
		return Behaviors.stopped(); //return this
	}
	

}
