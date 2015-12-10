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
	private static int threadCount = 50;
	private static ExecutorService pool;
	
	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
		remoteHost = ctx.getInitParameter("RMI_SERVER"); //Reads the value from the <context-param> in web.xml
		pool = Executors.newFixedThreadPool(threadCount);
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
			//inQueue.add(newJob);
			
			// I have deprecated the InQueue since the ExecutorService already takes care of this. It has a functional queue length of Integer.MaxValue
			pool.submit(new Worker(newJob, outQueue, remoteHost));
			
			out.print("<img src=\"images/loading.gif\">");
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
				out.print("<button onclick=\"location.href = '/Cracker';\" id=\"myButton\" class=\"float-left submit-button\" >Home</button>");
				out.print("</body>");	
				out.print("</html>");	
				outQueue.remove(taskNumber);
			}else{
				
				out.print("<img src=\"images/loading.gif\">");
				
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
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
 	}
}
