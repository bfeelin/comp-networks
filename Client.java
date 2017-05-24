import java.util.*;
import java.net.*;
import java.io.*;
public class Client 
{
	static Boolean exit = false;
	static ArrayList<Long> respTimes = new ArrayList<Long>();
	public static void main(String[] args) throws UnknownHostException, IOException
	{
			run();
	}
	public static void run() throws UnknownHostException, IOException
	{

				try 
				{
						System.out.println("Establishing connection.....");
					    @SuppressWarnings("resource")
						Socket socket = new Socket("192.168.100.109", 9998);
					    System.out.println("Client socket created.");
					    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);	    
					    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
					    
					    String userInput;
					    displayMenu();
					    while ((userInput = stdIn.readLine()) != null) 
					    {
					    	userInput = getCmd(userInput);
					    	if(userInput.equals("error"))
					    		break;
					    	long startTime = System.currentTimeMillis();
					        output.println(userInput);
					        System.out.println("echo: " + input.readLine());
					        long endTime = System.currentTimeMillis();
					        respTimes.add(endTime - startTime);
					    }
				}
				catch (IOException e) 
				{
		            System.out.println("Exception caught when trying to listen on port or listening for a connection");
		            System.out.println(e.getMessage());
		        }
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
