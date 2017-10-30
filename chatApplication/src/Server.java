import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {

	private static ServerSocket serverSocket = null;
	private static ArrayList<ClientHandler> clientHandlers = null;
	
	public Server(int portNumber)
	{
		try 
		{
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Server was created on port: " + portNumber);
			clientHandlers = new ArrayList<ClientHandler>();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	
	public void run()
	{
		while(true)
		{
			try 
			{
				Socket clientSocket = serverSocket.accept();
				clientHandlers.add(new ClientHandler(clientSocket, clientHandlers));
				clientHandlers.get(clientHandlers.size()-1).start();
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
		}
	}
}
