import java.io.*;
import java.net.Socket;
import java.util.*;

//
// READ AND SEND
//
// CLIENT CLASS
//
// FUNCTIONALITY:
// Read in Data from live File... (open)
// Pre-process Data as necessary... (clean)
// Create Socket connection to Server... (connect)
// Listen and Send to Socket...             (chat)
// Note: listen is for control/command information back from server.
//

// NEED TO CATCH INTERRUPT CTRL C AND CLOSE CONNECTION PROPERLY

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private FileReader fileReader;
    private String data;
    private Thread lfmThread;
    private Thread rnsThread;

    // constructer
    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioe) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public String readSerialFile() {

        String serialFileName = "/dev/ttyV1"; // "/dev/ttyUSB1";
        String data = null;

        try {
            BufferedReader buffFileReader = new BufferedReader(new FileReader(serialFileName));
            data = buffFileReader.readLine();
        } catch (FileNotFoundException fnfe) {
            System.out.println("FILE NOT FOUND: " + fnfe.getMessage());
            fnfe.printStackTrace();
            System.exit(1);
        } finally {
            return data;
        }
    }

    public void sendData(String data) {

        if(socket.isConnected()){
            try {
                bufferedWriter.newLine();
                bufferedWriter.flush();
                String msg = data;

                if (data != null) {
                    System.out.println("Sending " + data);
                    bufferedWriter.write(msg);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            } catch (IOException e) {
                System.out.println("IOE THROWN BY SENDDATA");
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

    public void end() {
         try {
            closeEverything(socket, bufferedReader, bufferedWriter);
            //
            lfmThread.interrupt();
            lfmThread.join();
            rnsThread.interrupt();
            rnsThread.join();
        } catch (InterruptedException ie) {
                System.out.println("Thread interrupt!");
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
                        end();
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
                    String data = readSerialFile(); // should give port name here...
                    if (data != null) {
                        sendData(data);
                    } else {
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    System.out.println(":(");
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }
    }

    
     public static void main(String[] args) throws IOException {
        // to instantiate & run:

        String serverAddress =  "127.0.0.1"; // local host // "10.0.107.147"; // windows laptop // "10.0.109.133"; // Ny Mitra
        int portNumber = 1234;

        Socket socket = new Socket(serverAddress, portNumber);
        Client client = new Client(socket);

        client.start();

    }  

}
