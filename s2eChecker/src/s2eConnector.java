import java.io.*;
import java.net.*;

/*
* We have a serial-to-Ethernet (s2e) device acting as a server/client.
* The s2e IP is: 10.0.109.225 with ports 9999 (general), 10001 (serial port 1), 10002 (port 2)
* GPSTime is a program which reads in the data from this device. But GPSTime might not work.
* This code is a substitute for GPSTime to confirm functionality of the s2e device.
* That's the theory, anyway.
*/

/* TO DO:
* - Exit Code 130.... ( = SIGTERM)
* - Threading....
* - Could keep & this to load into db...
* */

public class s2eConnector {

    private Socket socket;
    private BufferedReader bufferedReader;


    // Constructor:
    public s2eConnector(Socket socket) {

        try {
            // set-up & connect the socket
            this.socket = socket;
            if (socket.isConnected()) {
                System.out.println("Connected to Server :)");
            }
            // load the reader (note a byte stream might be more appropriate)
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioe) {
            System.out.println("FAILED TO CONSTRUCT READER! \n" + ioe.getMessage());
        }
    }

    // Simple conversion of counter data string to match GPSTime value:
    public Double preProcessString2Double(String counter_data) {

        // Counter outputs: 'xx.xxx, y, u' where X is an int, y is 2 or 7 (why?), I think u is mu is micro seconds
        // TIC value is double 'xx.xxxy'

        String[] ss = counter_data.split("[, ]");
        return Double.parseDouble(ss[0].concat(ss[1]));
    }

    // Main connector method:
    public void start() {

        try {

            DBHandler dbHandler = new DBHandler();
            Double ticVal;

            while (socket.isConnected()) {
                try {
                    String data = bufferedReader.readLine();
                    if (data != null) {
                        ticVal = preProcessString2Double(data.trim());
                        dbHandler.saveValue(ticVal);
                        System.out.println(ticVal);
                    }
                } catch (IOException e) {
                    System.out.println("ERROR IN READ! \n" + e.getMessage());
                }
            }
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                    System.out.println("CLOSED READER.");
                }
                if (socket != null) {
                    socket.close();
                    System.out.println("CLOSED SOCKET.");
                }
            } catch (IOException e) {
                System.out.println("ERROR CLOSING! \n" + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Waiting for connection...");

        String serverAddress =  "10.0.109.225";      //
        int portNumber = 10001;                      // the tcp port for serial port 1

        try {
            Socket socket = new Socket(serverAddress, portNumber);
            s2eConnector connector = new s2eConnector(socket);
            connector.start();
        } catch (IOException e) {
            System.out.println("FAILED TO INITIALISE & START! \n" + e.getMessage());
        }
    }
}