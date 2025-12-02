import pika
import json
import time
import random
import psycopg2

# -------------------------------
# PostgreSQL connection
# -------------------------------
PG_HOST = "localhost"
PG_PORT = 5434
PG_DB   = "device"
PG_USER = "postgres"
PG_PASS = "SQLPostgres32"

def load_device_ids():
    try:
        conn = psycopg2.connect(
            host=PG_HOST,
            port=PG_PORT,
            database=PG_DB,
            user=PG_USER,
            password=PG_PASS
        )
        cursor = conn.cursor()

        # ❗ Ajustează dacă tabela ta se numește diferit
        cursor.execute("SELECT device_id FROM device;")

        devices = [row[0] for row in cursor.fetchall()]

        cursor.close()
        conn.close()

        print(f"[INFO] Loaded {len(devices)} device from DB.")
        return devices

    except Exception as e:
        print("[ERROR] Could not connect to PostgreSQL:", str(e))
        return []


# -------------------------------
# RabbitMQ connection
# -------------------------------
RABBIT_URL = "amqps://jmjawvis:q3zYxWxrYNWNmdI3Z8QDJgGXNn-IJKEJ@collie.lmq.cloudamqp.com/jmjawvis"

parameters = pika.URLParameters(RABBIT_URL)
connection = pika.BlockingConnection(parameters)
channel = connection.channel()

EXCHANGE_NAME = "device-exchange"
ROUTING_KEY = "device.measurement"


# -------------------------------
# Build measurement message
# -------------------------------
def build_measurement(device_id):
    return {
        "device_id": device_id,
        "measurement_value": round(random.uniform(10, 500), 2),
        "timestamp": int(time.time() * 1000)
    }


# -------------------------------
# MAIN LOOP
# -------------------------------
device_ids = load_device_ids()

if not device_ids:
    print("[ERROR] No devices found in DB. Exiting...")
    exit(1)

print("[START] Device Simulator running...")
print("[INFO] Sending new measurements every 10 seconds.")

while True:
    for device_id in device_ids:
        message = build_measurement(device_id)
        body = json.dumps(message)

        channel.basic_publish(
            exchange=EXCHANGE_NAME,
            routing_key=ROUTING_KEY,
            body=body.encode("utf-8")
        )

        print(f"[SENT] {body}")

    time.sleep(10)
