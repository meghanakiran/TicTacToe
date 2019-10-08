// @author Meghana Kiran


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class tictactoeclient {
	
	public tictactoeclient(){
		
		try {
			Socket socket = new Socket("127.0.0.1",1234);
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Scanner inputKeyboard = new Scanner(System.in);
			
			System.out.println("welcome to tictactoe");
			System.out.println("Please enter your username:");
			String username = inputKeyboard.nextLine();
			
			printWriter.println(username);
			printWriter.flush();
			
			
			ListenThread lt = new ListenThread(username, socket);
			lt.start();
			
			boolean end = false;
			while (!end) {
				System.out.println("Please enter your move : ");
				String x = inputKeyboard.nextLine();
				printWriter.println(x);
				printWriter.flush();
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new tictactoeclient();
	}
}

class ListenThread extends Thread{
	String username="";
	Socket socket = null;
	
	ListenThread (String username, Socket socket){
		
		super("Listen Thread");
		this.username = username;
		this.socket = socket;
	}
	public void run() {
		try {
			PrintWriter outputWriter = new PrintWriter(socket.getOutputStream());
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			boolean end = false;
			while (!end) { //wait for commands
				String message = inputReader.readLine();
				if (message != null)
					System.out.println(message);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
