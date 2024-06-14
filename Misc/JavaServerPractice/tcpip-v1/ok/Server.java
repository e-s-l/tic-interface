import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;



//
//based from https://github.com/eugenp/tutorials/blob/master/core-java-modules/core-java-networking/src/main/java/com/baeldung/socket/EchoMultiServer.java
//

public class Server {

    private ServerSocket serverSocket;
 
    public void startServer(int port) {
		//
		try {
			//created patient server
			serverSocket = new ServerSocket(port);
			
			System.out.println("Waiting for connection...");
			while (true) {
                new ClientHandler(serverSocket.accept()).start();		
			}
		}
		catch (IOException e) {
            System.out.println(":(    " + e.getMessage());
        }
    }
	
	private class ClientHandler extends Thread {
		
		private Socket clientSocket;
		private DataInputStream in;
		private DataOutputStream out; 

	//
		public ClientHandler(Socket socket) {
            this.clientSocket = socket;
			//accept a client
			System.out.println("Connected to a client :)");
			try {
				//to receive in messgaes from clients
				in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
				//TO send out messages to clients
				out = new DataOutputStream(clientSocket.getOutputStream());	
			}
			catch (IOException e) {
				System.out.println(":(    " + e.getMessage());
			}
        }

		public void run() {
			try {

				Scanner userInput = new Scanner(System.in);	
				//
				while(true) {
					//messages in
					String msgIn = readMessage();
					System.out.println(msgIn);
					//
					if (".".equals(msgIn)) {
						break;
					}
					//messages out
					String msgOut = userInput.nextLine();
					sendData(msgOut);
				}
			}
			catch (Exception e) {
				System.out.println(":(    " + e.getMessage());
			}
			finally {
				closeSocket();
			}
		}
			
		// send string converted to byte (usng UTF_8) data to server
		public void sendData(String msg) {
			try {
				byte[] msgInBytes = msg.getBytes(StandardCharsets.UTF_8);	//need to set/know the conversion eg UTF8
				//Using a protocol like Type-Length-Value
				char type = 's';
				out.writeChar(type);
				int length = msgInBytes.length;
				out.writeInt(length);
				//the data bytes
				out.write(msgInBytes);
				out.flush();
			} catch (Exception e) {
				System.out.println(":( in sendData :(    " + e.getMessage());
			}
		}
		
		public String readMessage() {
			try {
				char msgType = in.readChar();
				int msgLength = in.readInt();
				//String msg;
				if(msgType=='s') {
					byte[] msgBytes = new byte[msgLength];
					in.readFully(msgBytes);
					String msg = new String(msgBytes, StandardCharsets.UTF_8);
				
					return msg;
				}
				else {
					System.out.println("Only UTF-8 encoded strings are currently supported.");
					return "";
				}
			} catch (IOException e) {
				System.out.println(":(    " + e.getMessage());
				return "";
			}
		}
		
		public void closeSocket() {
			try {
				if (in != null) {
                    in.close();
                }
				if (clientSocket != null) {
					clientSocket.close();
					System.out.println("Disconnected from Client :)");
				}
				serverSocket.close();
			} catch (IOException e) {
				System.out.println(":(    " + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
        Server server = new Server();
        server.startServer(6666);
    }
}