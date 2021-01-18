package com.intuit.lab01;

import java.util.Scanner;

import akka.actor.typed.ActorSystem;

// Fire and forget pattern
public class Main {

	public static void main(String[] args) {
		ActorSystem<WeatherCommand> system = ActorSystem.create(WeatherBehavior.create(), "reporter");
		System.out.println("Please enter the city name or history or q to quit");
		Scanner scanner = new Scanner(System.in);
		boolean keepGoing = true;
		while(keepGoing) {
			String data = scanner.next();
			if ("q".equalsIgnoreCase(data.trim())) {
				keepGoing = false;
				system.tell(() -> "quit" ); // Fire and forget interaction pattern
			} 
			else if("history".equalsIgnoreCase(data.trim()) ) {
				system.tell(new HistoryCommand());		
			}
			else {
				system.tell(new CityCommand(data));
			}
		}

	}

}
