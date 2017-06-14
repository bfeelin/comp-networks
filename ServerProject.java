
import java.io.*;
import java.net.*;
import java.util.*;


public class ServerProject 
{
	public static int sentThreadCount;
	public static boolean running = false;
	public static int id;
	public static ServerSocket serverSocket;


    public static void main(String[] args) throws IOException
    {
        int portNumber = 23456;
        serverSocket = new ServerSocket(portNumber);
        System.out.println("Server socket created...");
        start();
    }

    public static void start() throws IOException
    {
        System.out.println("Waiting for client...");
        id = 1;
        sentThreadCount = 0;
        while (true)
        {
            Socket clientSocket = serverSocket.accept();
            ThreadManager connection = new ThreadManager(clientSocket, id++);
            new Thread(connection).start();
        }
    }
}


class ThreadManager implements Runnable
{
	private Socket clientSocket;
	private int id = -1;

	public ThreadManager(Socket socket, int i)
	{
		this.clientSocket = socket;
		this.id = i;
	}

	public void run()
	{
        try
        {
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            
            String command;
            while ((command = in.readLine()) != null)
            {
                if (command.equals("Stop"))
                {
                    System.out.println("All threads processed...");
                    ServerProject.start();
                }
                else
                {
                    System.out.println("Thread: " + id + " received...");
                    String serverOutput = runCommand(command);
                    System.out.println("Thread: " + id + " Sending response to socket output...");
                    out.println(serverOutput);
	        	}           
            }
            out.flush();
            clientSocket.close();
        }
        catch (IOException e)
        {
            System.out.println("Exception caught when trying to connect to client");
            System.out.println(e.getMessage());
        }
        finally
        {
            try
            {
                if (clientSocket != null)
                    clientSocket.close();
            }
            catch (IOException e){}
        }
    }

    /**
     * Runs client command on the command prompt
     * Reads response from system
     * 
     * @param String command from the client
     * @return String response from system
     */
    public String runCommand(String c) throws IOException
    {
        String response = "";
	
        try 
        {
            Process p = Runtime.getRuntime().exec(c);
            
            BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
           
            String temp;
            while ((temp = output.readLine()) != null) 
                response = response + temp;
            while (stdError.readLine() != null) 
                response = stdError.readLine();
        }
        catch (IOException e) 
        {
            System.out.println("Exception");
            e.printStackTrace();
            System.exit(-1);
        }

        return response;
    }
}
