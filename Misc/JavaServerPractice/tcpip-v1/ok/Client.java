import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.charset.StandardCharsets;


public class Client {

	//initialisations:
    private Socket clientSocket;
    private DataOutputStream out; 
	private DataInputStream in;

	//method to start the client connection to server
    public void startConnection(String ip, int port) {
        //
		try {		
			// create conncetion (socket) to server:
            clientSocket = new Socket(ip, port);
			System.out.println(" Connected to Server :)");
			
			// set up in and out puts
			out = new DataOutputStream(clientSocket.getOutputStream());	
			//to receive in messgaes from server
			in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
			
        } catch (IOException e) {
			System.out.println(":( In startConnection :(    " + e.getMessage());
			System.exit(1); // Exit the program if connection is refused
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

	//method to close the client conncetion to server
    public void stopConnection() {
        try {
          if (out != null) {
                out.close();
            }
		if (clientSocket != null) {
			clientSocket.close();
			System.out.println("Disconnected from Server :)");
		}
        } catch (IOException e) {
			System.out.println(":(  in stopConnection :(  " + e.getMessage());
        }
    }
	
	
	public static void main(String[] args) {
		
		Client client = new Client();
		
		try {
			//start connection to the server:
			client.startConnection("127.0.0.1",6666);	//this should be user input
		
			//get user input (debuggin): string to be sent
			Scanner userInput = new Scanner(System.in);
			System.out.println("Input your message to the server:");
			//
			//  private DataInputStream in;
			//	in = new DataInputStream(System.in);	
			
			while (true) { 	
				//messages out
				String msg = userInput.nextLine();
				client.sendData(msg);
				
				if (".".equals(msg)) {
					break;
				}
				
				//messages in
				String msgIn = client.readMessage();
				System.out.println(msgIn);
				
				 
			}
		} catch (Exception e) {
			System.out.println(":( in main :(   " + e.getMessage());
        } finally {
			client.stopConnection();	
		}
    }

}