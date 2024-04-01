/*
 * header description...
 */

//imports here.

//for jSeriealComm first need to download and include jar file, then...
import com.fazecast.jSerialComm.SerialPort;

//
// import java.io.IOException;
import java.io.*;

public class TicCounterInterface {

	//declarations here
	static boolean daBug;	//debug flag
	
	//main
	public static void main (String [] args) {
			
		//initialisations here.
		daBug = false;

		
		/*/////////////////////////////////////////////////////////////////////
		 * OPEN & CONFIGURE SERIAL PORT CONNECTIONS
		 * looks like this will require an external package such as jSerialComm
		 * jSerialComm is light-weight & operating-system autonomous, but will it do what we need?
		/////////////////////////////////////////////////////////////////////*/
		
		//just for fun
		// find and list available serial ports:
		SerialPort[] ports = SerialPort.getCommPorts();		
		////
		if (ports == null) {
			  System.out.println("no serial ports.");  
		} else {
			for (SerialPort port: ports) {
				System.out.println(port);	//debug
				System.out.println(port.getSystemPortName());
			}
		}
			  
		////
		if (daBug==true) {
			  System.out.println(":)");  
		}
		////
		  
		//create serial port object corresponding to known port
		
		// set port parameters
		// open port connection
		
		 
		
		//Set or load connection parameters such as baud rate?
		
		//Once set-up & established, use as data input stream. 
		
		/////////////////////////////////////////////////////
		
		/*//////////
		 * THREAD 1:
		//////////*/
		
		/*////////////////////
		 * EXCEPTION HANDLING
		 * TRY open thread 2 ??
		 * CATCH 
		 * if exceptions such as program errors, port time-outs...
		 * then...
		 * Do we want exceptions here to abort/block the data monitoring & db pushing thread? (Also should these be two different threads?)
		 * FINAL close serial and db connections, after thread b ?
		///////////////////*/
		
	
	
	/////////////////////////////////////////////////////
	
			
			/*//////////
			 * THREAD 2:
			//////////*/
			
			
			/*///////////////////////////////////////////
			 * ENTER INFINITE LOOP MONITORING CONNECTIONS
			 *  while(true) the break if need be
			 //////////////////////////////////////////*/
			
			//monitor the stream
		
		
		/////////////////////////////////////////////////////
		
		/*//////////
		 * THREAD: ???
		//////////*/
			
			//this will happen at a lower frequency than the stream, ie this will sample the stream. 
		
				/*////////////////////////////
				 * FORMAT THE DATA AS REQUIRED
				 * convert time-stamp string
				///////////////////////////*/
			
				/*//////////////////////
				 * PUSH DATA TO DATABASE
				 * query the mysql db
				//////////////////////*/
		
		/////////////////////////////////////////////////////
		
		/*//////////
		 * THREAD: ???
		//////////*/
		
		/*
		 * Listening to ethernet socket to send out the raw data stream to any other programmes as required.
		 */
		
		
		
		
		/////////////////////////////////////////////////////
			
		/*/////
		 * FIN
		/////*/
			
			
		}


}
