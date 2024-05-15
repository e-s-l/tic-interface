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
        try {
            BufferedReader buffFileReader = new BufferedReader(new FileReader(serialFileName));
            String data = buffFileReader.readLine();
            try {
                while(data != null) {
                        System.out.println("FROM SERIAL PORT: " + data);
                }
            } catch (IOException ioe) {
                System.out.println(":(");
            } finally {
                return data;
            }


        } catch (FileNotFoundException fnfe) {
            System.out.println("FILE NOT FOUND");
            fnfe.printStackTrace();
        }
    }

    public void readNSend() {
        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    while(true) {
                        String data = readSerialFile(); // should give port name here...
                        if (data != null) {
                            sendData(data);
                        } else {
                            Thread.sleep(500);
                        }
                    }
                } catch (InterruptedException e) {
                        System.out.println(":(");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    public void sendData(String data) {

        try {
            bufferedWriter.newLine();
            bufferedWriter.flush();
            String msg = data;

            while(socket.isConnected()) {
                if (data != null) {
                    System.out.println("Sending " + data);
                    bufferedWriter.write(msg);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                else {
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("IOE THROWN BY SENDDATA");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        // this is again a blocking operation, so need new thread...

        new Thread(new Runnable() {
            @Override
            public void run(){
                String msg;

                while(socket.isConnected()){
                    try {
                        msg = bufferedReader.readLine();
                        System.out.println("FROM SERVER: " + msg);
                    } catch (IOException ioe) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
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
    
     public static void main(String[] args) throws IOException {
        // to instantiate & run:

        String serverAddress =  "127.0.0.1"; // local host // "10.0.107.147"; // windows laptop // "10.0.109.133"; // Ny Mitra
        int portNumber = 1234;

        Socket socket = new Socket(serverAddress, portNumber);
        Client client = new Client(socket);
        //
        client.listenForMessage();
        client.readNSend();
        //client.readSerialFile();
        //client.sendData();
    }  

}
