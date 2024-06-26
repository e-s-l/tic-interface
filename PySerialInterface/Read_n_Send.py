#!/usr/bin/env python3

########################
# READ FROM SERIAL PORT
# SEND AS UDP DATAGRAM
########################

import serial
import sys
import socket

from UDPServer_SQLDB_config import *

class SerialReader:

    # class instantiation:
    def __init__(self, port, speed, timeout):
        self.port = port
        self.speed = speed  # speed = baud rate
        self.timeout = timeout
        self.serial = None

    # METHODS:
    def open(self):
        try:
            self.serial = serial.Serial(self.port, self.speed, timeout=self.timeout)
            print(f"Successfully opened Serial Port {self.port}")
        except serial.SerialException as se:
            print(f"Failed to open Port {self.port}")
            print(se)
            sys.exit(1)  # remember non-zero exit code is bad....

    def read(self):
        #  print("reading")
        if self.serial:
            print("is open")
            data = self.serial.readline().decode().strip()          #LINE
           # data = self.serial.read(100).decode().strip()               #read till end of buffer
            print(f"Received from serial port {self.port}: {data}")
            return data
        else:
            print(f"Port {self.port} isn't open!")
            return None

    def write(self, data):
        if self.serial:
            data = data.encode()
            self.serial.write(data)
            print(f"Wrote {data} to Port {self.port}.")
        else:
            print("Cannot write to closed port.")

    def close(self):
        if self.serial:
            self.serial.close()
            print(f"Closed Port: {self.port}")
        else:
            print("Can't closed something already closed.")


####################

class UDPClient:

    def __init__(self, address, port):
        self.server_address = address
        self.server_port = port
        self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        print(f"UDP set-up to send to Server at {server_address} on port {self.server_port}")

    def send_message(self, message):
        self.client_socket.sendto(message.encode(), (self.server_address, self.server_port))
        print("Sent: %s" % message)

    def close(self):
        self.client_socket.close()


####################

if __name__ == "__main__":

    ##############

    serial_port = "/dev/ttyV1"
    baud_rate = 9600
    time_out = 2                #non-blocking mode
    serialPort = SerialReader(serial_port, baud_rate, time_out)
    serialPort.open()

    ##############

    server_address = UDP_server_address

    server_port = 1234
    client = UDPClient(server_address, server_port)

    ##############

    try:
        # print("trying!")
        while 1:
            #  print("loopy")
            line = serialPort.read()
            if line:
                print(line)
                client.send_message(line)
    except KeyboardInterrupt:
        serialPort.close()
    finally:
        client.close()
