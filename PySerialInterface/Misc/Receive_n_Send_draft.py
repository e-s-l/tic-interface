########################
# RECEIVE UDP DATAGRAM
# SAVE TO DATABASE
########################

import socket

import mysql.connector  # driver for python2mysql
from mysql.connector import errorcode

from datetime import datetime, timezone  # for time stamp on db upload

####################


class UDPServer:

    # class instantiation:
    def __init__(self, address, port):
        self.address = address
        self.port = port
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.server_socket.bind((self.address, self.port))

    def receive(self):
        message, client_address = self.server_socket.recvfrom(1024)  # number of bytes, will have to change
        print(f"Received from {client_address}: {message.decode()}")
        return message

    def close(self):
        self.server_socket.close()


####################


class SQLDatabase:

    def __init__(self, db, host, user, pw):
        self.connection = None
        self.database = db
        self.host = host
        self.user = user
        self.pw = pw

        self.connect()

    def connect(self):
        try:
            # make connection to sql server and select the db
            cnx = mysql.connector.connect(
                host=self.host,
                user=self.user,
                password=self.pw,
                database=self.database  # this will have to exist already
            )
            self.connection = cnx
            print(cnx)
        except mysql.connector.Error as err:
            if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
                print("Access Denied")
            elif err.errno == errorcode.ER_BAD_DB_ERROR:
                print("Database does not exist")
            else:
                print("Other: %s" % err)

    def upload(self, data):

        try:
            if not self.connection.is_connected():
                # self.connection is None or
                print("Connection is not established.")
                return

            print("trying")

            csr = self.connection.cursor()  # cursors process query return row by row
            print("created cursor")  # there exists option to buffer cursors

            table = "fake_tic_data"

            # Check if the table exists
            csr.execute("SHOW TABLES LIKE %s", (table,))
            table_exists = csr.fetchone()
            if not table_exists:
                # Create the table if it doesn't exist
                create_table_query = """
                CREATE TABLE {} (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    time_stamp DATETIME,
                    tic_value VARCHAR(255)
                )
                """.format(table)
                csr.execute(create_table_query)
                print("Created table {}".format(table))

            # Check if the table has the required columns
            columns_to_check = ['time_stamp', 'tic_value']
            for column in columns_to_check:
                csr.execute("SHOW COLUMNS FROM {} LIKE %s".format(table), (column,))
                column_exists = csr.fetchone()
                if not column_exists:
                    # Add the column if it doesn't exist
                    alter_table_query = "ALTER TABLE {} ADD COLUMN {} DATETIME".format(table, column)
                    csr.execute(alter_table_query)
                    print("Added column '{}' to table {}".format(column, table))
            ###

            time_stamp = datetime.now(timezone.utc).astimezone().isoformat()
            tic_value = data

            ###
            # query = "INSERT INTO {} (time_stamp, tic_value) VALUES (%s, %s)".format(table)
            query = "INSERT INTO %s (time_stamp, tic_value) " % table + " VALUES (%s, %s)"

            csr.execute(query, (time_stamp, tic_value))
            print("wrote query")

            self.connection.commit()  # make sure data is committed to db.
            csr.close()

        except mysql.connector.Error as err:
            print("Upload Err: % s" % err)

    def close_connection(self):

        self.connection.close()
        print("committed and closed")

####################


if __name__ == "__main__":

    ##############
    server_address = "10.0.107.147"     # "0.0.0.0" # = all available network interfaces
    server_port = 1234                  # what's the default UDP port again?
    # Get udp server object
    server = UDPServer(server_address, server_port)

    # Ge SQL database object
    sql_db = SQLDatabase(db="testdb", host="127.0.0.1", user="root", pw="password")

    # infinite loop
    try:
        while 1:
            msg = server.receive()
            sql_db.upload(msg)
    except KeyboardInterrupt:
        server.close()
    finally:
        sql_db.close_connection()

####################
