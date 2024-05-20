import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

//
// RECEIVE & SAVE
//

// class:
public class Server {

    // object/properties:
    private ServerSocket serverSocket;

    // constructor:
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    // methods:
    public void startServer() {

        try {
            System.out.println("Waiting for connection...");
            while (!serverSocket.isClosed()){
            
                Socket socket = serverSocket.accept();          // note this is a blocking method that returns a socket object
                System.out.println("Client has connected.");
                ClientHandler clientHandler = new ClientHandler(socket);
                
                //thread object
                Thread clientHandlerThread = new Thread(clientHandler);
                clientHandlerThread.start();
            }
        } catch (IOException ioe) {
            System.out.println(":(");
        }
    }

    public void closeServerSocket(){        // where do you call this?
        try {
            if (serverSocket != null) {     // to avoid null pointers
                serverSocket.close();
                System.out.println("Closed socket.");
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
