import java.io.*;
import java.net.*;
import java.util.*;


// READ AND SEND
// CLIENT CLASS

/*
 FUNCTIONALITY:
 Read in Data from live File... (open)
 Pre-process Data as necessary... (clean)
 Create Socket connection to Server... (connect)
 Listen and Send to Socket...             (chat)
 Note: listen is for control/command information back from server.
*/

public class Client {

    // instantiate:
    // object properties:
    private Socket socket;
    private String serialSource;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    //
    private Thread lfmThread;
    private Thread rnsThread;


    // constructer
    public Client(Socket socket, String serialFile) {
        try {
            this.serialSource = serialFile;     // the path to the serial file to be read from
            this.socket = socket;               // create the tcp socket connection
            if(socket.isConnected()){
                System.out.println("Connected to Server.");
            }
            // set-up the in and out put throu' this socket.
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException  ioe) {
            System.out.println("FAILED TO INITIALISE CLIENT: \n" + ioe.getMessage());
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public String readSerialFile() {

        FileReader fileReader;
        // String data;
        String data = null;         // initialise the empty data

        try {
            BufferedReader buffFileReader = new BufferedReader(new FileReader(this.serialSource));
            data = buffFileReader.readLine();
        } catch (FileNotFoundException fnfe) {
            System.out.println("FILE NOT FOUND: \n" + fnfe.getMessage());
            System.exit(1);
        } finally {
            return data;        // always return data: returns null if failed to open file
        }
    }

    public void sendData(String data) {

        if(socket.isConnected()){
            try {
                // clear the lines:
                bufferedWriter.newLine();
                bufferedWriter.flush();
                // if null data nothing happens...
                if (data != null) {
                    System.out.println("Sending " + data);  // for debug...
                    bufferedWriter.write(data);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            } catch (IOException ioe) {
                System.out.println("IOE THROWN BY SENDDATA \n" + ioe.getMessage());
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        System.out.println("Client closing everything.");
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

    public void start() {

        //
        lfmThread = new Thread(new Runnable() {
            @Override
            public void run() {
                listenForMessage();
            }
        });

        rnsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                readNSend();
            }
        });

        lfmThread.start();
        rnsThread.start();
    }

    public void finish() {
         try {
            closeEverything(socket, bufferedReader, bufferedWriter);
            //
            lfmThread.interrupt();
            rnsThread.interrupt();
            lfmThread.join();
            rnsThread.join();
        } catch (InterruptedException ie) {
            System.out.println("Thread interrupt! \n" + ie.getMessage());
        }
    }

    public void listenForMessage() {
        while (!Thread.currentThread().isInterrupted()) {
            String msg;
            while(socket.isConnected()){
                try {
                    msg = bufferedReader.readLine();
                    System.out.println("FROM SERVER: " + msg);
                    if (msg == null) {
                        finish();
                        System.exit(1);
                    }
                } catch (IOException ioe) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }
    }

    public void readNSend() {
        while (!Thread.currentThread().isInterrupted()) {
            while (socket.isConnected()) {
                try {
                    String data = readSerialFile();
                    if (data != null) {
                        sendData(data);
                    } else {                            // if null data then wait...
                        Thread.sleep(500);
                    }
                } catch (InterruptedException ie) {
                    System.out.println("Thread interrupt! \n" + ie.getMessage());
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }
    }

    
     public static void main(String[] args) throws IOException {
        // to instantiate & run

        // CONFIGURATION VARIABLES:
        String serverAddress =  "127.0.0.1";    // local host // "10.0.107.147"; // windows laptop // "10.0.109.133"; // Ny Mitra
        int portNumber = 1234;                  // the tcp listening port
        String serialFileName = "/dev/ttyV1";   // actual: "/dev/ttyUSB1";
        //

        try {
            Socket socket = new Socket(serverAddress, portNumber);
            Client client = new Client(socket, serialFileName);
            client.start();
        } catch (ConnectException ce) {
           System.out.println("FAILED TO INITIALISE MAIN: \n" + ce.getMessage());
        }

    }  

}
