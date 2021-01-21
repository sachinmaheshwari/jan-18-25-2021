import akka.actor.typed.ActorRef;

import akka.actor.typed.ActorSystem;

import akka.actor.typed.Behavior;

import akka.actor.typed.javadsl.Behaviors;

 

public class Lab {

      

       static Behavior<String> displayBehavior;

       static Behavior<String> fileBehavior;

       static Behavior<String> folderBehavior;

      

 

       public static void main(String[] args) {

              setup();

              ActorSystem<String> actor = ActorSystem.create(folderBehavior, "folder");

              actor.tell("/Users/bdutt/dev/akka/jan-18-25-2021/Day04/src/main/resources");

       }

      

       static void setup() {

             

              displayBehavior = Behaviors.setup(context -> {

                     return Behaviors

                                  .receive(String.class)

                                  .onAnyMessage( str -> {

                                         context.getLog().info("Display contents");

                                         context.getLog().info(str);

                                         return Behaviors.same();

                                  })

                                  .build();

              });

             

              fileBehavior = Behaviors.setup(context -> {

                     return Behaviors

                                  .receive(String.class)

                                  .onAnyMessage(file  -> {

                                         context.getLog().info("Reading file "+file);

                                         List<String> contents = Files.readAllLines(Paths.get(file), Charset.defaultCharset());

                                         ActorRef<String> displayActor = context.spawn(displayBehavior, "display"+Math.random());

                                         displayActor.tell(contents.toString());

                                         return Behaviors.same();

                                  })

                                  .onMessageEquals("bye", () -> {

                                         context.getLog().info("Bye Bye");

                                         return Behaviors.stopped();

                                  })

                                  .build();

              });

             

             

              folderBehavior = Behaviors.setup(context -> {

                     return Behaviors

                                  .receive(String.class)

                                  .onAnyMessage( folder  -> {

                                         context.getLog().info("Reading folder "+folder);

                                         ActorRef<String> fileActor = context.spawn(fileBehavior, "file");

                                         for(String path: new File(folder).list()) {

                                                context.getLog().info("File "+path);

                                                if((new File(folder+"/"+path).isFile())){

                                                       fileActor.tell(folder+"/"+path);

                                                }

                                         }

                                         return Behaviors.same();

                                  })

                                  .onMessageEquals("bye", () -> {

                                         context.getLog().info("Bye Bye");

                                         return Behaviors.stopped();

                                  })

                                  .build();

              });

 

       }

 

}