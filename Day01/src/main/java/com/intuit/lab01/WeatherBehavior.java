package com.intuit.lab01;

import java.util.HashMap;
import java.util.Map;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Behaviors;

public class WeatherBehavior extends AbstractBehavior<WeatherCommand> {

	private Map<String, Double> cityTemperatureMap = new HashMap<String, Double>();
	
	public WeatherBehavior(ActorContext<WeatherCommand> context) {
		super(context);
	}
	
	public static Behavior<WeatherCommand> create() {
		return Behaviors.setup(WeatherBehavior::new);
	}

	@Override
	public Receive<WeatherCommand> createReceive() {
		return newReceiveBuilder()
				.onMessage(HistoryCommand.class, msg -> {
					cityTemperatureMap.forEach((k, v) -> {
						System.out.println(k + ": " + v);
					});
					return this;
				})
				.onMessage(CityCommand.class, city -> {
					String cityName = city.getName();
					if (cityName.equalsIgnoreCase("chennai") || cityName.equalsIgnoreCase("pune")) {
						double temperature = Math.random() * 50;
						cityTemperatureMap.put(cityName, temperature);
						System.out.println("Temperature of " + city.getName() + " is " + temperature);
					}
					else {
						System.out.println("Temperature of " + city.getName() + " is not available");
					}
					return this;
				})
				.onAnyMessage(cmd -> {
					if ("quit".equalsIgnoreCase(cmd.getData())) {
						System.out.println("Time out. Bye Bye");
						return Behaviors.stopped();
					}
					return this;
				})
				.build();
	}
	
}
