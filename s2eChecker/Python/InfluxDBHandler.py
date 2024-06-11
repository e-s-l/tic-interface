# for influx db
from datetime import datetime, timezone
from influxdb import InfluxDBClient
#######################


class InfluxDatabase:

    def __init__(self, db, host, user, pw):

        self.client = None
        self.database = db
        self.host = host
        self.host_port = 8086
        self.user = user
        self.pw = pw

        self.connect()

    def upload(self, data):
        # push data to the database

        # Create influxDB data to send
        measurement = 'Rabben_TIC'
        tags = {}

        # Define fields and timestamp
        fields = {
            'tic_counter': float(data),
        }
        time = datetime.now(timezone.utc)

        # Create data dictionary
        data = [{
            'measurement': measurement,
            'tags': tags,
            'time': time,
            'fields': fields
        }]

        # Write data
        self.client.write_points(data)

    def connect(self):
        # make connection to server and select the db
        self.client = InfluxDBClient(self.host, self.host_port, self.user, self.pw, self.database)
        self.client.switch_database(self.database)

    def close_connection(self):
        # close the connection to the DB
        self.client.close()

# well it's a lot simpler than the mysql version...

