# Vignere Cracker Web App
![ArchtiectureSectionSiagram](http://i.imgur.com/i6crGkt.png "Web App")

Java Tomcat webapp which connects to an RMI vigenere cypher breaker object

Java Docs are enclose inthe /doc folder and this web app will be distributed as a .war file for use with tomcat.

The main features of this project are:

Contents:
---------
1. index.jsp
2. CrackerHandler.java
3. ie.gmit.server.threads package
4. ie.gmit.server.queues package
5. ie.gmit.sw.breaker package
6. Deployment

1 - index.jsp
---
This jsp page is the main entry point for the web app. It serves up the html from the tomcat server for the home page of the app which allows users to enter their encrypted text and max key length.

I have modified it to allow the user to add whether they want to use RMI connection or CORBA

2 - CrackerHandler.java
---
This is a HTTPServlet which handles the form submission from the index.jsp

The flow of control basically goes through one if statment split: if the user has a Job Number already then it checks the out queue (tree map) for that job otherwise it indicates that it is a new job so it starts the decryption process on the remote host and assigns the requestor a Job Number to wait on.

3 - ie.gmit.server.threads
---
This package contains the worker classes that connect to the remote host to start the decryption and return the result to the outqueue.

At current there is the RMIWorker and the CORBAWorker, both of which implement the Runnable interface, there are also some utility classes: A WorkerFactory and a WorkerType enum which facilitate the creation of new Worker threads.

4 - ie.gmit.server.queues
---
This package contains the queues which manage the user requests both waiting for processing and waiting for collection from by the user.

This contains interfaces for an InQueue and and OutQueue, as wel as an implementation for each based on a LInkedList and a TreeMap respectively. This package also contains the QueueMessage class which contains the details of the user request and also is used as the return structure which allows the program to use the same object for the in queue and the out queue.

There is also a AbandonedMessageCollector.java class here also which deals with messages where the client may never come to collect them, so instead of being removed from out queue they remain on the queue forever and take up server memory, this class implements the Runnable interface and is composed onto the OutQueueImpl and runs every 30 seconds to sweep through the outqueue to remove abandoned messages

5 - ie.gmit.breaker
---
This package contains the interfaces and client side helper classes for the remote objects i.e. the VignereBreaker extends remote interface and the CORBAVigenereBreaker interface with its helper classes etc.

6 - Deployment
---
To deploy this web app you should simply drop the distributed .war file into the web apps directory of your tomcat folder, however it will not do anything without connecting to the remote object back end host. To edit the location of this host, in the web.xml file change the RemoteHost value to whatever IP address your remote objects are listening on. 
