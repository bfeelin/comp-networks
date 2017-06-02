import java.util.*;
import java.net.*;
import java.text.DecimalFormat;
import java.io.*;
public class Client 
{
	static ArrayList<Thread> threads = new ArrayList<Thread>();
	static ArrayList<Double> respTimes = new ArrayList<Double>();
	static StringBuilder res = new StringBuilder();;
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException
	{
		if(args[0] == null)
		{
			System.out.println("Error: IP address of server not specified.");
			System.exit(0);
		}
		else run(args[0]);
	}
	public static void run(String ip) throws UnknownHostException, IOException, InterruptedException
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
					   
					    String numClients;
					    int noClients;
					    String userInput;
					    String results;
					    displayMenu();
					    while ((userInput = stdIn.readLine()) != null) 					//read input from the user
					    {
					    	userInput = getCmd(userInput);							//get appropriate command, checking for errors
					    	if(userInput.equals("error"))							//if input is not an integer between 1 and 7
					    		continue;											//restart while loop
					    	
					    	numClients = getNoClients();								//prompt for number of clients
					        noClients = Integer.parseInt(numClients);
					    	
					        for(int i = 0; i < noClients; i++) 						//initialize thread objects and add to array
			            	{
			                    Thread newThread = new Thread(input, output, userInput);
			                    threads.add(newThread);      
			            	}
					        threads.get(noClients-1).last = true;					//set completion flag
			            	for(int i = 0; i < noClients; i++)						//set start time and run each thread											
			            	{
			            		threads.get(i).setStartTime(System.nanoTime());
			            		threads.get(i).run();
			            	}          	
			            	int k = 0;
			            	while((results = input.readLine()) != null)		//while there are more results
						    {
			            		if(results.equals("last"))				
			            			break;
			            		if(results.equals("end"))				//if last byte is in
			            			threads.get(k++).setEndTime(System.nanoTime());
			            		else res.append(results+"\n");							//else store results                  		
			            	}

			            	System.out.println(res.toString());
		            		DecimalFormat df = new DecimalFormat("#.##");
		            		double totalRespTime = 0;
			            	for(int j = 1; j < threads.size() + 1; j++)					//collect response times
			            	{
			            		long sT = threads.get(j-1).getStartTime();
			            		long eT = threads.get(j-1).getEndTime();
			            		double responseTime = (eT - sT)/1000000.0;
			            		double rT = Double.valueOf(df.format(responseTime));
			            		System.out.println("Response time for thread " + j + ": " +rT);
			            		totalRespTime += rT;
			            	}
			            	System.out.println("Average response time running " + userInput + " for " + noClients + " clients: " + totalRespTime/threads.size() + "ms");
			            	res = new StringBuilder();
			            	threads.clear();
			            	respTimes.clear();							//clear threads and data
			            	totalRespTime = 0;
					        displayMenu();
					    }
				}
				catch (IOException e) 
				{
		            System.out.println("Exception caught when trying to listen on port or listening for a connection");
		            System.out.println(e.getMessage());
		        }
	}
	
	public static String getNoClients()										//prompt for number of clients
	{
		String noClients;
		Scanner scan = new Scanner(System.in);
		System.out.println("Please enter the number of clients (max 100): ");
		noClients = scan.nextLine();
		
		while(!(noClients.matches("\\d\\d?") || noClients.equals("100")) || noClients.matches("0(0+)?"))							//while invalid input
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
