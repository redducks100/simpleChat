import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
	  private String clientName = null;
	  private BufferedReader inputStream = null;
	  private PrintStream outputStream = null;
	  private Socket clientSocket = null;
	  private final ArrayList<ClientHandler> threads;

	  public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> threads) {
	    this.clientSocket = clientSocket;
	    this.threads = threads;
	  }

	  public void run() {
	    ArrayList<ClientHandler> threads = this.threads;

	    try {
	      inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	      outputStream = new PrintStream(clientSocket.getOutputStream());
	      String name = null;
	      while (true) {
	        outputStream.println("Enter your name: ");
	        name = inputStream.readLine().trim();
	        if (name.indexOf('@') == -1) {
	          break;
	        } else {
	          outputStream.println("The name should not contain '@' character.");
	        }
	      }

	      outputStream.println("Welcome " + name + " to the chat room.\nTo leave enter /exit in a new line.");
	      synchronized (this) 
	      {
	        	for(ClientHandler handler : threads)
	        	{
	        		if(handler == this)
	        		{
	        		    clientName = "@" + name;
	       	            break;
	        		}
	        	}
	      		for(ClientHandler handler : threads)
		      	{
	      			if (handler != this) {
	      				handler.outputStream.println("SERVER: A new user " + name + " entered the chat room!");
	      			}
		      	}
	      }
	      
	      while (true) {
	        String line = inputStream.readLine();
	        if (line.startsWith("/exit")) {
	          break;
	        }
	        if (line.startsWith("@")) {
	          String[] words = line.split("\\s", 2);
	          if (words.length > 1 && words[1] != null) {
	            words[1] = words[1].trim();
	            if (!words[1].isEmpty()) {
	              synchronized (this) {
	                for (ClientHandler handler : threads) {
	                  if (handler != this && handler.clientName != null && handler.clientName.equals(words[0])) {
	                    handler.outputStream.println(clientName + ": " + words[1]);
	                    this.outputStream.println("The message was received.");
	                    break;
	                  }
	                }
	              }
	            }
	          }
	        } else {
	          synchronized (this) {
	        	 for (ClientHandler handler : threads) {
	              if (handler.clientName != null) {
	                handler.outputStream.println(clientName + ": " + line);
	              }
	            }
	          }
	        }
	      }
	      synchronized (this) {
	    	 for (ClientHandler handler : threads) {
	          if (handler != this && handler.clientName != null) {
	            handler.outputStream.println("SERVER: The user " + name + " left the chat room!");
	          }
	        }
	      }
	     this.outputStream.println("@SERVER: /exit");

	      synchronized (this) {
	        threads.remove(this);
	      }
	      
	      inputStream.close();
	      outputStream.close();
	      clientSocket.close();
	    } 
	    catch (IOException e) 
	    {
	    	
	    }
	  }
}
