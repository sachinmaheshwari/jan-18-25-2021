package com.intuit;

import java.util.stream.IntStream;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.GroupRouter;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Routers;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;

public class GroupRouterExample {

	public static void main(String[] args) {
		ActorSystem<Object> parent = ActorSystem.create(Parent2.create(), "parent2");
		IntStream.range(1, 10).forEach(it -> parent.tell("hello"));
		
	}

}
class Parent2 extends AbstractBehavior<Object> {

	ServiceKey<Object> key = ServiceKey.create(Object.class, "some-key");
	GroupRouter<Object> pool;
	ActorRef<Object> router;
	public Parent2(ActorContext<Object> context) {
		super(context);
		pool = Routers.group(key);
		ActorRef<Object> child1 = context.spawn(Child2.create(), "child1");
		ActorRef<Object> child2 = context.spawn(Child2.create(), "child2");
		context.getSystem().receptionist().tell(Receptionist.register(key, child1));
		context.getSystem().receptionist().tell(Receptionist.register(key, child2));
		router = context.spawn(pool, "pool");
	}
	
	@Override
	public Receive<Object> createReceive() {
		return newReceiveBuilder()
				.onAnyMessage((msg) -> {
					router.tell(msg);
					return this;
				})
				.build();
	}
	
	public static Behavior<Object> create() {
		return Behaviors.setup(Parent2::new);
	}
	
}

class Child2 extends AbstractBehavior<Object> {

	public Child2(ActorContext<Object> context) {
		super(context);
	}

	@Override
	public Receive<Object> createReceive() {
		return newReceiveBuilder()
				.onAnyMessage((msg) -> {
					System.out.println("Received by " + getContext().getSelf().path());
					return this;
				})
				.build();
	}
	
	public static Behavior<Object> create() {
		return Behaviors.setup(Child2::new);
	}
	
}