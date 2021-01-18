* High level abstraction of threading model 
* Akka is a simple library
* Director who wants to direct a movie
* She delegates certain tasks to different actors
* Every actor behaves according to the script
* Director records/gathers all the behaviors of the actors
* Constructs the movie

* Calculating a large prime number
* BigInteger nextProbablePrime()

### Actor

* Actor is a simple object
* Each actor runs in its own thread
* Creating an actor = creating a thread
* Just like how a thread has a task, actor has a __behavior__
* Every actor receives messages that are queued
* Behavior acts upon the messages
* Processes only one message at a time

* __Akka application is all about defining your own actors and making them interact with each other by passing messages__
* Messages are immutable