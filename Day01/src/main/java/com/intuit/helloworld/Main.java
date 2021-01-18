package com.intuit.helloworld;

import java.time.LocalDateTime;

import akka.actor.typed.ActorSystem;

public class Main {

	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			ActorSystem<String> clockActor 
				= ActorSystem.create(Clock.create(), "my-clock");
			clockActor.tell("now");
//			clockActor.tell("now");
//			clockActor.tell("now");
//			clockActor.tell("now");			
		}


	}
	private void oldStyleClock() {
		Thread clock = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("It's " + LocalDateTime.now());
			}
		});
		clock.start();
	}

}
