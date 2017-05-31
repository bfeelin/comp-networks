import java.util.*;
import java.net.*;
import java.io.*;
public class Client 
{
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		if(args[0] == null)
		{
			System.out.println("Error: IP address of server not specified.");
			System.exit(0);
		}
		else run(args[0]);
	}
	public static void run(String ip) throws UnknownHostException, IOException
	{	
				try 
				{
						System.out.println("Establishing connection.....");
					    @SuppressWarnings("resource")
						Socket socket = new Socket(ip, 9991);
					    System.out.println("Client socket connected to server " + ip);
					    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);	    				//to write to Server
					    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));	//to read from Server
					    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));				//to read input
					   
					    String noClients;
					    String userInput;
					    String results = null;
					    displayMenu();
					    while ((userInput = stdIn.readLine()) != null) 					//read input from the user
					    {
					    	userInput = getCmd(userInput);							//get appropriate command, checking for errors
					    	if(userInput.equals("error"))							//if input is not an integer between 1 and 7
					    		continue;											//restart while loop
					    	
					    	noClients = getNoClients();								//prompt for number of clients
					        output.println(userInput);								//send input to the server	
					        output.println(noClients);								//send noClients to server
					        while(!(results = input.readLine()).equals("end"))		//while there are more results
					        	System.out.println(results);						//print
					       
					        displayMenu();
					    }
				}
				catch (IOException e) 
				{
		            System.out.println("Exception caught when trying to listen on port or listening for a connection");
		            System.out.println(e.getMessage());
		        }
	}
	
	public static String getNoClients()
	{
		String noClients;
		Scanner scan = new Scanner(System.in);
		System.out.println("Please enter the number of clients (max 100): ");
		noClients = scan.nextLine();
		
		while(!(noClients.matches("\\d\\d?") || noClients.equals("100")))							//while invalid input
		{
			System.out.println("Invalid input. Please enter an integer between 1 and 100.");
			noClients = scan.nextLine();
		}
		return noClients;
	}
	
	public static void displayMenu()
	{
		System.out.println("Select an option by number");
		System.out.println("1. Date and Time");
		System.out.println("2. Uptime");
		System.out.println("3. Memory use");
		System.out.println("4. Netstat");
		System.out.println("5. Current users");
		System.out.println("6. Running processes");
		System.out.println("7. Quit");
	}
	
	public static String getCmd(String input)		//return appropriate linux command based on the number input by the user
	{
		if(!input.matches("[1-7]"))							//reject anything not 1-7 and display menu again
		{
			System.out.println("Invalid input. Only input integers between 1 and 7.");
			System.out.println();
			displayMenu();
			return "error";
		}
		int input1;
		input1 = Integer.parseInt(input);

		if(input1 == 7)					//exit
		{
			System.out.println("Exiting");
			System.exit(0);
		}
		   if(input1 == 1)
				return "date";
			else if(input1 == 2)
				return "uptime";
			else if(input1 == 3)
				return "free";
			else if(input1 == 4)
				return "netstat";
			else if(input1 == 5)
				return "who";
			else if(input1 == 6)
				return "ps -e";
			else return "error";
	}
	
	
	
		
	

}
