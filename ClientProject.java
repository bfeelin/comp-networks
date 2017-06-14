
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientProject
{
    public static int portNumber = 23456;
    public static String hostName; 
    public static long totalTime;
    public static int threadsReceived;
	static ArrayList<Thread> threads = new ArrayList<Thread>();
	static ArrayList<Double> respTimes = new ArrayList<Double>();
	static StringBuilder res = new StringBuilder();

    public static void main(String[] args) throws IOException 
    {
        if (args.length == 0)
        {
			System.out.println("Error: IP address of server not specified.");
			System.exit(0);
        }
         
        hostName = args[0];
        
        begin();
    }//end main

    public static void begin() throws IOException
    {
        Scanner input = new Scanner(System.in);
        String userInput;
        String numClients;
        int noClients;
        
        displayMenu();
        userInput = input.nextLine();
        userInput = getCmd(userInput);
        while (userInput.equals("error")) 					//read input from the user
	    {
        	 userInput = input.nextLine();
             userInput = getCmd(userInput);
	    }	
        numClients = getNoClients();								//prompt for number of clients
        noClients = Integer.parseInt(numClients);
        
        connect(userInput, noClients);
    }
    
    private static void connect(String c, int numberOfClients) throws IOException
    { 
        totalTime = 0;
        String command = c;
        for (int i = 0; i < numberOfClients; i++)
        {
            Socket clientSocket = new Socket(hostName, portNumber);
            threads.add(new Thread(new ThreadManager(clientSocket, command, i, numberOfClients)));
        }
        for (int i = 1; i <= numberOfClients; i++)
            threads.get(i).start();
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
	
    private static void exit()
    {
        System.out.println("Exiting...");
        System.exit(0);
    }//end exit()
}//end main


/**
 * Connects the socket to the server using a thread
 */
class ThreadManager implements Runnable
{
    private Socket clientSocket;
    private String cmd;
    private int i;
    private int noClients;
    private long startTime, endTime;
    
    /**
     *Constructor stores arguments and starts new thread
     *
     * @param s The client socket
     * @param c Command to run at terminal
     * @param i Current object/thread number
     * @param noc Number of clients to be created
     */
    public ThreadManager (Socket s, String c, int i, int noClients)
    {
        this.clientSocket = s;
        this.cmd = c;
        this.i = i;
        this.noClients = noClients;
    }//end constructor

    public void run()
    {
        String serverResponse = "";
        String temp = "";

        try 
        {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Thread " + i + " sent");
            startTime = System.currentTimeMillis();
            out.println(cmd);
            serverResponse = in.readLine();
            endTime = System.currentTimeMillis();
	    
            long responseTime = endTime - startTime;
	   
            if (ClientProject.threadsReceived == 0)       
                System.out.println("\nServer Response:\n" + serverResponse + "\n");
            System.out.println("Thread " + i + " received");
            ClientProject.totalTime = ClientProject.totalTime + responseTime;
            ClientProject.threadsReceived++;
            if (ClientProject.threadsReceived == noClients)
            {
                System.out.println("Mean Response Time: " + ClientProject.totalTime/noClients + " ms\n");
                out.println("Stop");
                ClientProject.threadsReceived = 0;
                ClientProject.begin();
            }
            out.flush();
            clientSocket.close();
        }

        catch  (IOException e) 
        {
            System.err.println("Problem with IO connection");
            System.exit(1);
        } 
    }
}
