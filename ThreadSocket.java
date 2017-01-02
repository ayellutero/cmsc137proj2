/*
References:
1.	http://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html

2.	http://www.jcgonzalez.com/java-socket-mini-server-http-example
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadSocket extends Thread{
	private Socket inSocket; // requests

	ThreadSocket(Socket inSocket){
		this.inSocket = inSocket;
		this.start();
	}

	public void run(){
		try{
			InputStream inStream = inSocket.getInputStream();
			PrintWriter out = new PrintWriter(inSocket.getOutputStream(), true);
			
			// Code on line 27 is equal to
			// BufferedReader in = new BufferedReader(new InputStreamReader(inStream.getInputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
			String line;

			line = in.readLine(); // Read HTTP request file
			String reqMethod = line; // First line (which is the request method)

			String getData = "";

			System.out.println("HTTP-HEADER: " + line);
			line = "";

			// looks for post data
			int postDataI = -1;
			while ((line = in.readLine()) != null && (line.length() != 0)){
				System.out.println("HTTP-HEADER: " + line);
				if(line.indexOf("Content-length: ") > -1){ // Read until EOF
					postDataI = new Integer(
						line.substring(
							line.indexOf("Content-length: ") + 16,
							line.length())).intValue();
				}
			}

			String postData = "";
			// Read post data
			if (postDataI > 0){
				char[] charArray = new char[postDataI];
				in.read(charArray, 0, postDataI);
				postData = new String(charArray);
			}

			// Print response headers
			out.println("HTTP/1.0 200 OK");
            out.println("Content-Type: text/html; charset=utf-8");
            out.println("Server: Miniwebserver");
            // this blank line signals the end of the headers
            out.println("");

            // Send request file and save to web server program
            try{
            	PrintStream ps = new PrintStream(new FileOutputStream("HTTPRequestFile.html"));

            	out.println("<h3>CMSC 137 Project: Mini-Webserver</h3>");
            	out.println("HTTP Request method: " + reqMethod);
            	out.println("<br>HTTP Header is printed on the console.");
            	out.println("<br>GetData Key-Value Pair: " + getData);
            	out.println("<br>PostData Key-Value Pair: " + postData);
            	out.println("<form name=\"input\" action=\"form_submited\" method=\"post\">");
            	out.println("Username: <input type=\"text\" name=\"user\"><input type=\"submit\" value=\"Submit\"></form>");

            	ps.println("<h3>CMSC 137 Project: Mini-Webserver</h3>");
            	ps.println("HTTP Request method: " + reqMethod);
            	ps.println("<br>HTTP Header is printed on the console.");
            	ps.println("<br>GetData Key-Value Pair: " + getData);
            	ps.println("<br>PostData Key-Value Pair: " + postData);
            	ps.println("<form name=\"input\" action=\"form_submited\" method=\"post\">");
            	ps.println("Username: <input type=\"text\" name=\"user\"><input type=\"submit\" value=\"Submit\"></form>");
            	
            	out.close();
            	inSocket.close();
            	ps.close();
            }catch(FileNotFoundException e){
            	e.printStackTrace();
            }


		}catch(Exception e){
			e.printStackTrace();
		}
	}

}