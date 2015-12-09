package ie.gmit.sw.servlets;

import java.io.*;
import java.util.concurrent.*;

import javax.servlet.*;
import javax.servlet.http.*;

import ie.gmit.sw.server.queue.*;
import ie.gmit.sw.server.threads.Worker;

public class CrackerHandler extends HttpServlet {
	private static final long serialVersionUID = -3488966062128116375L;
	private String remoteHost = null;
	private static long jobNumber = 0;
	private static OutQueue outQueue = new OutQueueImpl();
	private static InQueue inQueue = new InQueueImpl();
	private static int threadCount = 10;
	private static ExecutorService pool;
	
	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
		remoteHost = ctx.getInitParameter("RMI_SERVER"); //Reads the value from the <context-param> in web.xml
		pool = Executors.newFixedThreadPool(threadCount);
		for (int i =0; i<threadCount; i++){
       		pool.submit(new Worker(outQueue, inQueue, remoteHost));
   		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		
		int maxKeyLength = Integer.parseInt(req.getParameter("frmMaxKeyLength"));
		String cypherText = req.getParameter("frmCypherText");
		String taskNumber = req.getParameter("frmStatus");


		out.print("<html><head><title>Distributed Systems Assignment</title>");		
		out.print("</head>");		
		out.print("<body>");
		if(taskNumber == null){
			out.print("<h1>Processing request for Job#: T" + jobNumber + "</h1>");
		}else{
			out.print("<h1>Processing request for Job#: " + taskNumber + "</h1>");
		}
		out.print("<div id=\"r\"></div>");
		
		
		out.print("RMI Server is located at " + remoteHost);
		out.print("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
		out.print("<p>CypherText: " + cypherText + "</p>");
		
		
		if (taskNumber == null){
			taskNumber = new String("T" + jobNumber);
			jobNumber++;
			
			QueueMessage newJob = new QueueMessage(taskNumber, maxKeyLength, cypherText);
			
		
			//Add job to in-queue
			inQueue.add(newJob);
			
			out.print("<form name=\"frmCracker\">");
			out.print("<input name=\"frmMaxKeyLength\" type=\"hidden\" value=\"" + maxKeyLength + "\">");
			out.print("<input name=\"frmCypherText\" type=\"hidden\" value=\"" + cypherText + "\">");
			out.print("<input name=\"frmStatus\" type=\"hidden\" value=\"" + taskNumber + "\">");
			out.print("</form>");								
			out.print("</body>");	
			out.print("</html>");	
			
			out.print("<script>");
			out.print("var wait=setTimeout(\"document.frmCracker.submit();\", 10000);");
			out.print("</script>");
		}else{
			//Check out-queue for finished job
			if(outQueue.containsKey(taskNumber)){
				//print out result
				out.print("<p>PlainText: " + outQueue.get(taskNumber).getCypherText() + "</p>");
				out.print("</body>");	
				out.print("</html>");	
				outQueue.remove(taskNumber);
			}else{
				out.print("<form name=\"frmCracker\">");
				out.print("<input name=\"frmMaxKeyLength\" type=\"hidden\" value=\"" + maxKeyLength + "\">");
				out.print("<input name=\"frmCypherText\" type=\"hidden\" value=\"" + cypherText + "\">");
				out.print("<input name=\"frmStatus\" type=\"hidden\" value=\"" + taskNumber + "\">");
				out.print("</form>");								
				
				
				out.print("<script>");
				out.print("var wait=setTimeout(\"document.frmCracker.submit();\", 10000);");
				out.print("</script>");
			}
		}
				
		/*-----------------------------------------------------------------------     
		 *  Next Steps: just in case you removed the above....
		 *-----------------------------------------------------------------------
		 * 1) Generate a big random number to use a a job number, or just increment a static long variable declared at a class level, e.g. jobNumber
		 * 2) Create some type of a "message request" object from the maxKeyLength, cypherText and jobNumber.
		 * 3) Add the "message request" object to a LinkedList or BlockingQueue (the IN-queue)
		 * 4) Return the jobNumber to the client web browser with a wait interval using <meta http-equiv="refresh" content="10">. The content="10" will wait for 10s.
		 * 4) Have some process check the LinkedList or BlockingQueue for "message requests" 
		 * 5) Poll a "message request" from the front of the queue and make an RMI call to the Vigenere Cypher Service
		 * 6) Get the result and add to a Map (the OUT-queue) using the jobNumber and the key and the result as a value
		 * 7) Return the cyphertext to the client next time a request for the jobNumber is received and delete the key / value pair from the Map.
		 */
		
		//You can use this method to implement the functionality of an RMI client
		
		//
		
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
 	}
}
