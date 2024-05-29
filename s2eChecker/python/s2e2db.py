########################
# RECEIVE TIC VALUE THRO' TCP
# SAVE TO DATABASE
########################

# We have a serial-to-Ethernet (s2e) device acting as a server/client.
# The s2e IP is: 10.0.109.225 with ports 9999 (general), 10001 (serial port 1), 10002 (port 2)
# GPSTime is a program which reads in the data from this device. But GPSTime might not work.
# This code is a substitute for GPSTime to confirm functionality of the s2e device.
# That's the theory, anyway.

########################

# for TCP/IP socket connection
import socket

# configuration
sql_db = False
if sql_db:

    from SQLDBHandler import *

    #################
    # SQL DB Config #
    #################

    database = "testdb"  #
    db_address = "127.0.0.1"  # since hosted locally
    username = "root"
    password = "password"

    ###

else:
    # Influx db:
    from InfluxDBHandler import *

    #################
    # INFLUX DB Config #
    #################

    database = "monitoring"  #
    db_address = "10.0.109.133"
    username = "admin"
    password = "admin"

#####################
# S2E Server Config #
#####################

tcp_address = '10.0.109.225'        #
tcp_port = 10001                    # for COM port 1


# Connect to the S2E device & get the data
class S2EServer:

    # class instantiation:
    def __init__(self, address, port):

        try:
            self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            server_address = (address, port)
            self.server_socket.connect(server_address)  # if client
            print("Connected :)")

        except socket.error as err:
            print(" :( Socket error :( \n % s", err)

    def receive(self):

        try:
            file = self.server_socket.makefile('r')
            line = file.readline()

            if line:
                return line
            else:
                return None

        except socket.error as err:
            print(" :( Socket error :( \n % s", err)
            exit(1)

    def close(self):
        self.server_socket.close()


########################
def preprocess_tic_data(counter_data):

    tl1 = counter_data.strip().split(',')
    tl2 = tl1[1].split()
    tic_value = tl1[0] + tl2[0]
    return tic_value


def close_all_connections(server, db):

    db.close_connection()
    server.close()
    print("Closed connections to Server & Database.")


########################

if __name__ == "__main__":

    ##############
    # Create s2e server object
    print("Attempting to connect to s2e device at: ", tcp_address, tcp_port)
    s2e_server = S2EServer(tcp_address, tcp_port)

    if sql_db:
        # Create SQL database object
        db = SQLDatabase(db=database, host=db_address, user=username, pw=password)
    else:
        # Create Influx database
        db = InfluxDatabase(db=database, host=db_address, user=username, pw=password)

    # infinite loop
    try:
        while 1:
            data = s2e_server.receive()
            if data:
                data = preprocess_tic_data(data)
                print(data)
                db.upload(data)
    except KeyboardInterrupt:
        print("SIGTERM")
    finally:
        close_all_connections(s2e_server, db)

####################
