# for SQL database
import mysql.connector  # driver for python2mysql
from mysql.connector import errorcode
from datetime import datetime, timezone  # for time stamp on db upload
#######################

DB_table = "real_tic_data"
table_cols = ['time_stamp', 'tic_value']

#######################


class SQLDatabase:

    def __init__(self, db, host, user, pw):
        self.connection = None
        self.database = db
        self.host = host
        self.user = user
        self.pw = pw

        self.connect()

    def connect(self):
        # make connection to sql server and select the db
        try:
            cnx = mysql.connector.connect(
                host=self.host,
                user=self.user,
                password=self.pw,
                database=self.database
            )
            self.connection = cnx
            print("Connected to DB: \n", cnx)                          # debug: show result of connection
        except mysql.connector.Error as err:
            if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
                print("Access Denied")
            elif err.errno == errorcode.ER_BAD_DB_ERROR:
                print("Database does not exist")
            else:
                print("Other: %s" % err)

    def upload(self, data):
        # push data to the database
        try:
            if not self.connection.is_connected():
                print("Connection is not established.")
                return
            csr = self.connection.cursor()          # cursors process query return row by row

            # check if the table exists
            table = DB_table
            csr.execute("SHOW TABLES LIKE %s", (table,))
            table_exists = csr.fetchone()
            if not table_exists:
                # Create the table if it doesn't exist
                create_table_query = """
                CREATE TABLE {} (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    {} DATETIME,
                    {} VARCHAR(255)
                )
                """.format(table, table_cols[0], table_cols[1])
                csr.execute(create_table_query)
                print("Created table {}".format(table))

            # check if the table has the required columns
            columns_to_check = table_cols
            for column in columns_to_check:
                csr.execute("SHOW COLUMNS FROM {} LIKE %s".format(table), (column,))
                column_exists = csr.fetchone()
                if not column_exists:
                    # Add the column if it doesn't exist
                    if column == table_cols[0]:
                        alter_table_query = "ALTER TABLE {} ADD COLUMN {} DATETIME".format(table, column)
                        csr.execute(alter_table_query)
                    else:
                        alter_table_query = "ALTER TABLE {} ADD COLUMN {} VARCHAR(255)".format(table, column)
                        csr.execute(alter_table_query)
                    print("Added column '{}' to table {}".format(column, table))
            ###

            time_stamp = datetime.now(timezone.utc).astimezone().isoformat()
            tic_value = data

            ###
            query = "INSERT INTO %s (%s, %s) " % (table, table_cols[0], table_cols[1]) + " VALUES (%s, %s)"
            csr.execute(query, (time_stamp, tic_value))
            self.connection.commit()        # make sure data is committed to db.
            csr.close()                     # kill the cursor

        except mysql.connector.Error as err:
            print("Upload Err: \n % s" % err)

    def close_connection(self):
        # close the connection to the DB
        self.connection.close()

