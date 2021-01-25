package com.intuit;

import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.server.Route;
import static akka.http.javadsl.server.Directives.*;

import java.util.concurrent.CompletionStage;

import akka.NotUsed;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;


public class WebApp {

	public static void main(String[] args) {
		//define the routes
		//define and start the http server
		
		//define the actor system
		
		Behavior<NotUsed> root = Behaviors.setup(context -> {
			WebRoutes routes = new WebRoutes();
            startHttpServer(routes.getRoutes(), context.getSystem());
            return Behaviors.empty();
		});
		ActorSystem.create(root, "hello-server");
	}
	
	
	static void startHttpServer(Route route, final ActorSystem<?> system) {
        CompletionStage<ServerBinding> futureBinding =
            Http.get(system).newServerAt("localhost", 8000).bind(route);

        futureBinding.whenComplete((binding, exception) -> {
            if (binding != null) {
            	System.out.println("Server started in 8000");
            } else {
            	System.out.println("Stopping server");
                system.terminate();
            }
        });
    }

}



class WebRoutes {
	public Route getRoutes() {
		return concat(
			path("hello", () -> 
				get(() -> complete("Hello akka server"))	
			)	
		);
	}
}
