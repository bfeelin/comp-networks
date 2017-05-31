import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	static ArrayList<Thread> threads = new ArrayList<Thread>();
	static double totalRespTime = 0;
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		run();
	}
	
	public static void run() throws IOException, InterruptedException
	{
		try
		{
			    @SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket(9991);
			    System.out.println("Server socket created");
			    Socket clientSocket = serverSocket.accept();
			    PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);							//to write to Client
			    String inputLine = null;
			    int noClients;

	            	 BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));	//to read from Client
		            while ((inputLine = input.readLine()) != null) 									//while there are more commands
		            {	    																		//get command
		            	noClients = Integer.parseInt(input.readLine());								//get number of clients
		            	
				    	for(int i = 0; i < noClients; i++) 											//initialize thread objects and add to array
		            	{
		                    Thread newThread = new Thread(clientSocket);
		                    threads.add(newThread);      
		            	}
		            	for(int i = 0; i < noClients; i++)											
		            	{
		            		threads.get(i).start(inputLine); 										//run command
		            		totalRespTime += threads.get(i).getRespTime();							//add this response time to the total
		            	}
		            	output.println("Average response time running '" + inputLine + "' for " + noClients + " clients: " + totalRespTime/noClients + "ms");
		            	output.println("end");
		            	threads.clear();										//clear old threads
		            	totalRespTime = 0;
			    }
		}
		catch (IOException e) 
		{
            System.out.println("Exception caught when trying to listen on port or listening for a connection");
            System.out.println(e.getMessage());
        }
	}
}
