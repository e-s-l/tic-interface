import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

//class:
public class Server {

    //object/properties:
    private ServerSocket serverSocket;

    //constructor:
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    //method:
    public void startServer() {

        try {
            while (!serverSocket.isClosed()){
            
                Socket socket = serverSocket.accept();  //note this is a blocking method that returns a socket object
                System.out.println("A new client has connected.");
                ClientHandler clientHandler = new ClientHandler(socket);
                
                //thread object
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException ioe) {}
    

    }

    public void closeServerSocket(){
        try {
            if (serverSocket != null) { //to avoid null pointers
                serverSocket.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //to instantiate & run:

        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();

    }  
}
