* Create three actors __FolderActor__, __FileActor__ and __DisplayActor__
* Implement these actors in __Functional style__ atleast one of them :)
* Pass a __folder/directory path__ to folder actor
* It reads all the files and passes each __filepath__ to the __FileActor__
* __FileActor__ reads the contents of the file and passes the contents back to __FolderActor__
* FolderActor passes the contents to __DisplayActor__ which displays the contents on the console

* Write a couple of tests using __BehaviorTestKit__ and __ActorTestKit__ capturing interaction of between any two actors