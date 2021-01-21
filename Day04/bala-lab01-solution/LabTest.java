package com.intuit;

 

import static org.junit.jupiter.api.Assertions.assertEquals;

 

import java.time.Duration;

 

import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;

 

import akka.actor.testkit.typed.javadsl.ActorTestKit;

import akka.actor.testkit.typed.javadsl.BehaviorTestKit;

import akka.actor.testkit.typed.javadsl.TestProbe;

import akka.actor.typed.ActorRef;

 

class LabTest {

 

       @Test

       void testFolderMessage() {

              BehaviorTestKit<String> testKit 

                     = BehaviorTestKit.create(Lab.folderBehavior);

              String message = "/Users/bdutt/dev/akka/jan-18-25-2021/Day04/src/main/resources";

              testKit.run(message);

              assertEquals(testKit.getAllLogEntries().size(), 2);

              assertEquals(testKit.getAllLogEntries().get(0).message(), 

                           "Reading folder /Users/bdutt/dev/akka/jan-18-25-2021/Day04/src/main/resources");

       }

      

       @Test

       void testFileMessage() {

              BehaviorTestKit<String> testKit 

                     = BehaviorTestKit.create(Lab.fileBehavior);

              String message = "/Users/bdutt/dev/akka/jan-18-25-2021/Day04/src/main/resources/application.conf";

              testKit.run(message);

              assertEquals(testKit.getAllLogEntries().size(), 1);

              assertEquals(testKit.getAllLogEntries().get(0).message(), 

                           "Reading file /Users/bdutt/dev/akka/jan-18-25-2021/Day04/src/main/resources/application.conf");

       }

      

       @Test

       void testDisplayMessage() {

              BehaviorTestKit<String> testKit 

                     = BehaviorTestKit.create(Lab.displayBehavior);

              String message = "test";

              testKit.run(message);

              assertEquals(testKit.getAllLogEntries().size(), 2);

              assertEquals(testKit.getAllLogEntries().get(1).message(), 

                           "test");

       }

      

 

       private static ActorTestKit actorTestKit;

      

       @BeforeAll

       public static void setup() {

              Lab.setup();

              actorTestKit = ActorTestKit.create(); 

       }

      

       @AfterAll

       public static void tearDown() {

              actorTestKit.shutdownTestKit();

       }

      

       @Test

       void testFolderActor() {

              ActorRef<String> folderBehavior = actorTestKit.spawn(Lab.folderBehavior);

              TestProbe<String> probe = actorTestKit.createTestProbe();

             

              String message = "/Users/bdutt/dev/akka/jan-18-25-2021/Day04/src/main/resources";

              folderBehavior.tell(message);

             

              probe.expectMessage(Duration.ofSeconds(6), "hi");

             

       }

}