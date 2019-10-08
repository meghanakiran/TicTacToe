


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class tictactoeserver {
	Socket socket = null;
	
	
	
	
	public static int row, col;
	public static Scanner scan = new Scanner(System.in);
	public static char[][] board = new char[3][3];
	public static char turn = 'x';
	public static int flagplayer1 = 0;
	public static int flagplayer2 = 0;
	public static int played = 0;
	public static int notplayed = 0;
	//public static int full =0;
	public static int playersJoined =0;
	public tictactoeserver() {
		System.out.println("Starting");
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(1234);
			System.out.println("Waiting for Players to Connect ");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		while(playersJoined<2) {
			
			try {
				socket = serverSocket.accept();
				playersJoined++;
				
				ConnThread ct = new ConnThread(socket, playersJoined);
				ct.start();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	
	}

	public static void main(String[] args) {
		new tictactoeserver();
	}

	public static void play(PrintWriter printWriter, BufferedReader bufferedReader) throws IOException {
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				board[i][j] = '_';
				
			}
		}
		PrintBoard(printWriter);
		boolean playing = true;
		while (playing) {
			if (flagplayer1 == 0 && flagplayer2 == 0 && notplayed == 0) {
				System.out.println("player 1");
				printWriter.println("player 1");
				printWriter.flush();
				flagplayer1 = 1;
				
			} else if (flagplayer1 == 0 && flagplayer2 == 1 && notplayed == 1) {
				System.out.println("player 2");
				printWriter.println("player 2");
				printWriter.flush();
				flagplayer1 = 1;
			} else if (flagplayer1 == 1 && flagplayer2 == 0 && notplayed == 1 && played == 0) {
				System.out.println("player 1");
				printWriter.println("player 1");
				printWriter.flush();
				flagplayer1 = 1;
			} else {
				System.out.println("player 2");
				printWriter.println("player 2");
				printWriter.flush();
				flagplayer2 = 1;
				// played=0;
			}
			System.out.println("enter row");
			printWriter.println("enter row");
			printWriter.flush();
			String rowStr = bufferedReader.readLine();
			row = Integer.parseInt(rowStr);
			System.out.println("Row entered "+row);
			System.out.println("enter column");
			printWriter.println("enter column");
			printWriter.flush();
			String colStr = bufferedReader.readLine();
			col = Integer.parseInt(colStr);
			System.out.println("Column entered "+col);
			checkentry();

			if (Gameover(row, col)) {
				playing = false;
				System.out.println("Game over player  " + turn + " WINS!");
				printWriter.println("Game over player  " + turn + " WINS!");
				printWriter.flush();

			}
			//full =0;
			if (checkboard()) {
				playing = false;
				System.out.println("Game over players::   its a TIE !! ");
				printWriter.println("Game over players::   its a TIE !! ");
				printWriter.flush();
			}
			PrintBoard(printWriter);
			if (turn == 'x' && flagplayer1 == 1 && flagplayer2 == 0 && played == 1)
				turn = 'o';
			else if (turn == 'o' && flagplayer1 == 1 && flagplayer2 == 1 && notplayed == 1 && played == 1)
				turn = 'o';
			else if (turn == 'o' && flagplayer1 == 1 && flagplayer2 == 1 && played == 2)
				turn = 'x';
			else if (turn == 'x' && flagplayer1 == 1 && flagplayer2 == 0 && notplayed == 1)
				turn = 'x';
			else
				turn = 'x';

			if (flagplayer1 == 1 && flagplayer2 == 1 && played == 2) {
				flagplayer1 = 0;
				flagplayer2 = 0;
				played = 0;
				notplayed = 0;
				turn = 'x';
			}

		}
	}

	public static void checkentry() {
		if (board[row][col] == '_') {
			board[row][col] = turn;
			played = played + 1;
		} else {
			System.out.println("already inserted in row and col  : WRONG MOVE");
			notplayed = 1;
			System.out.println("Enter again");
		}
	}

	public static void PrintBoard(PrintWriter printWriter) {
		for (int i = 0; i < 3; i++) {
			System.out.println();
			printWriter.println();
			printWriter.flush();
			for (int j = 0; j < 3; j++) {
				if (j == 0){
					System.out.print("|");
					printWriter.print("|");
					printWriter.flush();
				}
				System.out.print(board[i][j] + "|");
				printWriter.print(board[i][j] + "|");
				printWriter.flush();
			}
		}
		System.out.println();
		printWriter.println();
		printWriter.flush();

	}

	public static boolean Gameover(int rmove, int cmove) {
		if (board[0][cmove] == board[1][cmove] && board[0][cmove] == board[2][cmove])
			return true;
		if (board[rmove][0] == board[rmove][1] && board[rmove][0] == board[rmove][2])
			return true;
		if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[1][1] != '_')
			return true;
		if (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[1][1] != '_')
			return true;

		return false;
	}
	
	public static boolean checkboard()
	{   
		int full=0;
		for (int i = 0; i < 3; i++) {
			//System.out.println();
			for (int j = 0; j < 3; j++) {
				if(board[i][j]!='_') {
					full=full+1;
				}
			}
		}
			
			if(full==9)
				return true;
			else 
				return false;
	
	}
}


class ConnThread extends Thread {
	Socket socket = null;
	 String username = "";
	 InputStream inputStream = null;
	 OutputStream outputStream = null;
	 int playerNumber =0;
	 
	static HashMap <Integer, ConnThread> users = new HashMap<>();
	ConnThread(Socket socket, int playersJoined){
		this.playerNumber = playersJoined;
		this.socket = socket;
	}
	
	public void run(){
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			PrintWriter printWriter = new PrintWriter(outputStream);
			
			String username = bufferedReader.readLine();
			
			System.out.println(username+" Joined");
			
			printWriter.println("Welcome Player "+username);
			printWriter.flush();
			
			users.put(this.playerNumber, this);
			int j=1;
			/*while(j==1 && playerNumber==2){*/
				/*ConnThread conn = users.get(1);
				System.out.println(users);*/
			makeAMove(printWriter, bufferedReader);
				
				
			/*}*/
			
		} catch (Exception e) {
			
		}
		
		
		
		
		
		
	}

	private synchronized void makeAMove(PrintWriter printWriter, BufferedReader bufferedReader) throws IOException {
		/*System.out.println("Player "+this.playerNumber+" is making a move");
		printWriter.println("Player "+this.playerNumber+" make a move");
		printWriter.flush();
		
		
		String move = bufferedReader.readLine();
		System.out.println("Move made "+ move);
		*/
		
		tictactoeserver.play(printWriter, bufferedReader);
		
		
		
		
		
	}
	
}
