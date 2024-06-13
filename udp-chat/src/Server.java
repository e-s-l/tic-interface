import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {

    private DatagramSocket datagramSocket;
    private byte[] buffer = new byte[256];

    public Server(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }
    //////////////////////////////////////////////
    public void receiveThenSend() {
        while(true) {
            try {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket); //blocking method
                InetAddress inetAddress = datagramPacket.getAddress();  //get ip address
                int port = datagramPacket.getPort();    //get port number
                String messageFromClient = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println("Message from Client: " + messageFromClient);
                datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
                datagramSocket.send(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    /////////////////////////////////////////////////
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(1111);
        Server server = new Server(datagramSocket);
        server.receiveThenSend();

    }
    /////////////////////////////////////////////////
}
