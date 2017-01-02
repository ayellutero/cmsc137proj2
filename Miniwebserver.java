/*
Author: Lutero, Levie Abigail D.
CMSC 137 B-1L
2012-29207
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

public class Miniwebserver{
	// the value of this variable can be changed depending on
	// which available port you want to use
	private static final int PORT = 8081; 

	public static void main(String[] args){
		try{
			// Instantiate a server socket with port = 3000
			ServerSocket server = new ServerSocket(PORT);
			System.out.println("Server is running at port " + PORT);
			
			// This block will let the server accept requests until
			// program is stopped/terminated.
			while(true){

				new ThreadSocket(server.accept());
			}
		}catch(Exception e){
			System.err.println("Error: " + e.getMessage());
		}

	}

}