#
#

import mysql.connector  # driver for python2mysql

# make connection and connect to db
mydb = mysql.connector.connect(
    host = "localhost",
    user = "admin",
    password = "password",
    database = "testDB"  # this will have to exist already
)

print(mydb)

###
# then

mycursor = mydb.cursr()  # cursors process query return row by row
mycursor.exeute("SHOW TABLES")
for tb in mycursor:
    print(tb)