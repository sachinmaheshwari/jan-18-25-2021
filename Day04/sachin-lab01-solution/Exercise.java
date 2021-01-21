package com.intuit.lab04;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class Exercise {

  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create(FolderActor.create(), "folder");
    system.tell("/Users/smaheshwari1/Development/intuit/training/akka-training/src/test/resources");
  }


}

class FolderActor extends AbstractBehavior<Object> {

  private final Behavior<Object> fileActor;





  public FolderActor(ActorContext<Object> context) {
    super(context);
    fileActor = Behaviors.setup(ctx -> {
      return Behaviors.receive(Object.class).onMessage(FilePath.class,  filePath -> {
        Data data = new Data();
        data.fileContent = new String(Files.readAllBytes(filePath.getFilePath()));
        filePath.getParent().tell(data);
        return Behaviors.same();
      }).build();
    });
  }

  @Override
  public Receive<Object> createReceive() {
    return newReceiveBuilder()
        .onMessage(String.class, (folderPath) -> {
          int i = 1;
          DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folderPath));
          directoryStream.forEach(path -> getContext().spawn(fileActor, "fileActor-" + UUID.randomUUID().toString()).tell(
              new FilePath(path, this.getContext().getSelf())));
          return this;
        })
        .onMessage(Data.class, data -> {
          Behavior<String> displayActor =
          Behaviors.setup(ctx -> {
            return Behaviors.receive(String.class).onAnyMessage( file -> {
              ctx.getLog().info(file);
              return Behaviors.same();
            }).build();
          });
          getContext().spawn(displayActor, "display-" + UUID.randomUUID().toString()).tell(data.fileContent);
          return this;
        })
        .build();
  }

  public static Behavior<Object> create() {
    return Behaviors.setup(FolderActor::new);
  }


}

class Data {
  String fileContent;
}

class FilePath {
  private final Path filePath;
  private final ActorRef<Object> parent;

  FilePath(Path filePath, ActorRef<Object> parent) {
    this.filePath = filePath;
    this.parent = parent;
  }

  public Path getFilePath() {
    return filePath;
  }

  public ActorRef<Object> getParent() {
    return parent;
  }
}