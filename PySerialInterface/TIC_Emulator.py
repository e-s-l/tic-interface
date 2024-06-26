#!/usr/bin/env python3

########################
### TIC Emulator     ###
########################

# Send random double between 0 an 30
# with time stamp

import serial
import sys
import random
import time

#########################

#sudo socat -d -d -v pty,rawer,echo=0,link=/dev/ttyV0,rawer,b9600 pty,rawer,echo=0,link=/dev/ttyV1,rawer,b9600

#########################


class SerialReader:

    # class instantiation:
    def __init__(self, port, speed):
        self.port = port
        self.speed = speed  # speed = baud rate
        self.serial = None

    # METHODS:
    def open(self):
        try:
            self.serial = serial.Serial(self.port, self.speed)
            time.sleep(1)
            print("Successfully opened Port %s" % self.port)
        except serial.SerialException as se:
            print("Failed to open Port %s" % self.port)
            print(se)
            sys.exit(1)  # remember non-zero exit code is bad....

    def read(self):
        if self.serial:
            #return self.serial.readline().decode().strip()
             return self.serial.read(100).decode().strip()
        else:
            print("Port %s isn't open!" % self.port)
            return None

    def write(self, data):
        if self.serial:
            if type(data) == str:
                data = data.encode()
            elif type(data) == float:
                print("data is float")
                data = bytes(str(data),'utf-8')      # this doesn't work...
            self.serial.write(data)
            self.serial.flush()
            #self.serial.reset_input_buffer()
            print(f"Wrote {data} to Port {self.port}.")
        else:
            print("Cannot write to closed port.")

    def close(self):
        if self.serial:
            self.serial.close()
            print("Closed Port: %s" % self.port)
        else:
            print("Can't closed something already closed.")


####################


if __name__ == "__main__":

    port_name = "/dev/ttyV0"
    baud_rate = 9600
    serialPort = SerialReader(port_name, baud_rate)
    serialPort.open()

    ##############

    try:

        random.seed(15)

        while 1:
            data = str(random.uniform(0,30))
            #data = "help"
            serialPort.write(data+"\n")
            #serialPort.write(data)
            time.sleep(0.200)       #sleep takes seconds

    except KeyboardInterrupt:
        serialPort.close()
