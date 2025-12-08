import pika
import json
import time
import random
import psycopg2
import os

# -------------------------------
# Load config.json
# -------------------------------
def load_config():
    if not os.path.exists("config.json"):
        print("[WARN] config.json not found. Using DB device list.")
        return None

    try:
        with open("config.json", "r") as f:
            data = json.load(f)
            device_id = data.get("device_id", "").strip()

            if device_id:
                print(f"[CONFIG] Loaded device_id from config: {device_id}")
                return device_id
            else:
                print("[CONFIG] No device_id set in config.json → using DB devices.")
                return None

    except Exception as e:
        print("[ERROR] Could not read config.json:", e)
        return None


# -------------------------------
# PostgreSQL connection
# -------------------------------
PG_HOST = "localhost"
PG_PORT = 5434
PG_DB   = "device"
PG_USER = "postgres"
PG_PASS = "SQLPostgres32"

def load_device_ids_from_db():
    try:
        conn = psycopg2.connect(
            host=PG_HOST,
            port=PG_PORT,
            database=PG_DB,
            user=PG_USER,
            password=PG_PASS
        )
        cursor = conn.cursor()

        cursor.execute("SELECT device_id FROM device;")

        devices = [row[0] for row in cursor.fetchall()]

        cursor.close()
        conn.close()

        print(f"[INFO] Loaded {len(devices)} devices from DB.")
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
# Build measurement
# -------------------------------
def build_measurement(device_id):
    return {
        "device_id": device_id,
        "measurement_value": round(random.uniform(10, 500), 2),
        "timestamp": int(time.time() * 1000)
    }


# -------------------------------
# MAIN LOGIC
# -------------------------------
config_device = load_config()

if config_device:
    device_ids = [config_device]   # folosim doar device-ul din config
else:
    device_ids = load_device_ids_from_db()

if not device_ids:
    print("[ERROR] No devices available. Exiting.")
    exit(1)

print("\n[START] Device Simulator running...")
print("[INFO] Sending new measurements every 10 seconds.\n")

while True:
    for device_id in device_ids:
        measurement = build_measurement(device_id)
        body = json.dumps(measurement)

        channel.basic_publish(
            exchange=EXCHANGE_NAME,
            routing_key=ROUTING_KEY,
            body=body.encode("utf-8")
        )

        print(f"[SENT] {body}")

    time.sleep(10)
