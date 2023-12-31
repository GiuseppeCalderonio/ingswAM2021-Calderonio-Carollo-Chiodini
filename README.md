# Prova Finale di Ingegneria del Software - AA 2020-2021

Implementation of [Maestri del Rinascimento](https://www.balenaludens.it/2020/maestri-del-rinascimento/) board game.

The target of this project is to realize a distributed system where there are only one server and
several clients that communicate with this server through sockets. We used the pattern MVC (Model-View-Controller)
to realize the global structure of the project. In particular in our case the view send a message to the controller, then the
controller checks the message and if it is valid we call some model methods to change the state of the game.
When the state changes the controller notifies one or more views and then waits another message from the view.

# Documentation
****
**UML**

We have uploaded some UML diagrams in [deliveries](https://github.com/gabrielecarollo/ingswAM2021-Calderonio-Carollo-Chiodini/tree/main/deliveries)
folder. Here you can see three type of diagrams
* [Initial](https://github.com/gabrielecarollo/ingswAM2021-Calderonio-Carollo-Chiodini/blob/main/deliveries/UML/UmlInitial.pdf) UML class diagram of model; we realized it with StarUML.
* [Final](https://github.com/gabrielecarollo/ingswAM2021-Calderonio-Carollo-Chiodini/tree/main/deliveries/UML/final%20UML) UML class diagram of model; we generated it automatically with IntelliJ
* UML [NetworkDiagrams](https://github.com/gabrielecarollo/ingswAM2021-Calderonio-Carollo-Chiodini/tree/main/deliveries/NetworkDiagrams): these diagrams contain all the messages that are sent between client
and server in all possible situation of the game. We insert for each game action a sequence
  diagram and the Gson format of the message that must be sent through the network.


**JAVADOC**

We don't have uploaded Javadoc because gitHub doesn't rendering them correctly. We have a Javadoc
folder in our local pc and we could show them when you want. We write Javadoc of all classes and methods.

**COVERAGE REPORT**

To check the correct behavior of the game we have realized a lot of tests (about 400 tests) with the help of
Junit tool achieving excellent coverage results:

- 95% of class coverage.
- 77% of method coverage.
- 85% of code lines coverage.

**PLUGINS AND LIBRARY**
- **Maven**: this is a tool to automate the compiling phase of a java project.
- **JavaFx**: this is a graphical library used to create user interface.
- **Scene builder**: an IDE that allows you to avoid the manual creation of an FXML file.
- **Json**: it allows to send data or objects between different components of a software system.
- **JUnit**: unit testing framework.

# FUNCTIONALITY
****
**FUNCTIONALITY DEVELOPED**
- complete rules
- CLI
- GUI
- Socket
- 2 FA(advanced functionality): **local single game** and **multiple game**
    
The first one allow to play a single game without server; the last one allow to play several games at the same time.
Indeed we have created a lobby and the server manages every player to create multiple games.

# COMPILATION AND PACKAGING
****
**JAR**

For this project we have realized only one jar and we did it with the help of Maven Shade Plugin. We have already created
and uploaded this jar file in a specific GitHub [folder](https://github.com/gabrielecarollo/ingswAM2021-Calderonio-Carollo-Chiodini/tree/main/shade) but if you want to generate it independently you can do it with
the command mvn -> Lifecycle -> package.

# EXECUTION
****

We have only one jar file that can be launched in different ways: according to input parameter we can start it in 
server mode, client GUI or CLI mode, local client GUI or CLI mode.

All the initialization parameters are in order:

- **jar mode initialization**: it could be server, client, localClient
- **port number**
- **ip number**: it could be 127.0.0.1 or a generic public ip
- **visualization mode**: GUI or CLI

These are the five possible configurations to launch the jar from cmd or wsl:

**Server mode**

`java -jar AM30.jar server 1234 null randomString`

**Client GUI mode**

`java -jar AM30.jar client 1234 127.0.0.1 -GUI`

**Client CLI mode**

`java -jar AM30.jar client 1234 127.0.0.1 -CLI`

**Local Client GUI mode**

`java -jar AM30.jar localClient 0 null -GUI`

**Local Client CLI mode**

`java -jar AM30.jar localClient 0 null -CLI`

**IMPORTANT NOTES**:

1.In cmd or wsl if you want to launch the jar file you have to move in the folder where the jar is saved;
for example _cd desktop_ if you saved the jar into desktop folder.

2.There are two ways to play in CLI mode. In the first case you have to use a **_wsl terminal_** for client and server.
In the second case you can use a cmd terminal, but first you need to activate the ansi colors for Windows terminal with
this key register:

`reg add hkcu\console /f /v VirtualTerminalLevel /t REG_DWORD /d 1`

You can copy this in cmd or create and open a .bat file with this code line. Now you can launch the jar file from cmd with any
configuration.

3.If you want to play in GUI mode you can't use wsl terminal, you can use windows cmd without any problems.

4.The number port could be different from 1234 but must be the same when you launch the server mode and client mode.

5.The IP number could be 127.0.0.1 if you want to play an online game on a single pc with different terminals. Instead
if you want to play an online game using different pc you could insert your public Ip address(port forwarding) or insert
the Ip of a remote Amazon server. For example in our project we can use both port forwarding, or the ip number
3.19.123.210 that is the ip of an amazon server. 

6.The three parameters 0, randomString and null are ignored, so you could write what you want but it is important that
they are not empty.

To automate the launch of jar we create some .bat file(you can find them [here](https://github.com/gabrielecarollo/ingswAM2021-Calderonio-Carollo-Chiodini/tree/main/batFiles)) 
but they only work if the jar and all bat files are in your desktop folder. 
Sometimes could be necessary change the initialization parameters into the bat file to launch the jar correctly;
for example you have to update the ip or the server port.

# GROUP MEMBERS
****
- Giuseppe Calderonio | giuseppe.calderonio@mail.polimi.it

- Gabriele Carollo | gabriele.carollo@mail.polimi.it

- Stefano Chiodini | stefano.chiodini@mail.polimi.it