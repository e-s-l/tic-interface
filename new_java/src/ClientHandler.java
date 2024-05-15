import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    // instances executed by seperate threads

    // static array list of instace of this class, ie each of the clients, so we can broadcast...
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    // static means belongs to class not instances of the class
    private Socket socket; //connection between client and server
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientIP;

    // constructor:
    public ClientHandler(Socket socket) {
        // properties:
        try {
            // this = the clientHandler Object
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));     // note: stream = bytes, writer = characters
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientIP = socket.getRemoteSocketAddress().toString();
            //
            clientHandlers.add(this);
            //
           broadcastMessage("Server: Client at" + this.clientIP + " has connected.");

        } catch (IOException ioe) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    // need to override run method
    @Override
    public void run() {
        // this is what is run on each new thread.
        // listening to messages are a blocking operation, hence the seperate threads

        String msgFromClient;

        while(socket.isConnected()) {
            try {
                msgFromClient = bufferedReader.readLine();
                if (msgFromClient != null) {
                    System.out.println(msgFromClient);
                } else {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
            } catch (IOException ioe) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientIP.equals(clientIP)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush(); // fill out the buffer to send
                }
            } catch (IOException ioe) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("Server: Client at" + this.clientIP + " has disconnected.");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
