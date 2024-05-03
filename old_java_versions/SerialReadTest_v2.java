import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

public class SerialReadTest {

	public static void main(String[] args) {
		
		/////////////////////////////////////////////////////
		//just for fun: find and list available serial ports:
			SerialPort[] ports = SerialPort.getCommPorts();		
			////
			if (ports == null) {
				  System.out.println("no serial ports.");  
			} else {
				for (SerialPort port: ports) {
					System.out.println("port = " + port + ", w name: " +  port.getSystemPortName());	//debug
				}
			}

			/*//////////////////////
			* OPEN PORT
			//////////////////////*/

			
			SerialPort comPort = ports[3];
			//open the port
			if (comPort.openPort()) {	//this method opens the port and returns true if successful
				System.out.println("port " + comPort.getSystemPortName() + " opened");
			} else {
				System.out.println("Failed to open port!");
				return;
			}
			
			/*//////////////////////
			* READ PORT
			//////////////////////*/

			comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		/*	
			try {
				   while (true)
				   {
					   int readByte = comPort.getInputStream().read();
					   System.out.println(readByte);
					   //
					   byte[] readBuffer = new byte[comPort.bytesAvailable()];
					   //byte[] readBuffer = new byte[1024];
					   int numRead = comPort.readBytes(readBuffer, readBuffer.length);
					   System.out.println("Read " + numRead + " bytes.");
					   String result = new String(readBuffer); //, StandardCharsets.UTF_8
					   System.out.println(result);
				   }
				} catch (Exception e) { e.printStackTrace(); }
			*/
			try
			{
			 //  for (int j = 0; j < 10; ++j) {
				while (true) {
				   System.out.print((char)comPort.getInputStream().read());
				//   System.out.print(":)");   
				}
			 
			} catch (Exception e) { e.printStackTrace(); }

	//		Scanner scannerInput = new Scanner(System.in);
			
	//		try {
	//			System.out.println(":)");
		//		int ep = 0;
				
				
	//			while(true) {
	//				int readByte = COMin.getInputStream().read();
	//				System.out.println(readByte);
		//			ep++;
					
		//			if (scannerInput.next() != null) {
			//			throw new IOException();
				//	}
			//	}
				
		//	} catch (IOException ioe ) {
		//		System.out.println(":( IOE");  
		//	}
			
			/*//////////////////////
			* CLOSE PORT
			//////////////////////*/
		//	finally {
		//		scannerInput.close();
				try {
					System.out.println("Closing input stream.");
					comPort.getInputStream().close();	
				} catch (IOException ioe ) {
					System.out.println(":(");  
				}
				//
				if (comPort.closePort()) {	//this method closes the port and returns true if successful
					System.out.println("port "  + comPort.getSystemPortName() + " closed");
				} else {
					System.out.println("Failed to close port!");
				}
		//	}
			
					
	}

}
