import java.util.Scanner;

public class Main {
	
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		int choice = -1;
		Server server = null;
		Client client = null;
		
		boolean created = false;
		
		while(!created)
		{
			System.out.println("1 - Start a server");
			System.out.println("2 - Join a server");
			choice = Integer.parseInt(scanner.nextLine());
			
			switch(choice)
			{
				case 1:
					System.out.println("Choose a port: ");
					int port = Integer.parseInt(scanner.nextLine());
					server = new Server(port);
					created = true;
					break;
				case 2:
					System.out.println("Host adress: ");
					String hostAdress = scanner.nextLine();
					System.out.println("Server port: ");
					int hostPort = Integer.parseInt(scanner.nextLine());
					client = new Client(hostAdress, hostPort);
					created = true;
					break;
				default:
					break;
			}
		}
		
		if(server != null)
		{
			server.run();
		}
		if(client != null)
		{
			client.start();
		}
	}
}
