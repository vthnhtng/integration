import json
import time
import hmac
import hashlib
import requests

# ===========================
# Configuration
# ===========================
BASE_URL = "http://localhost:8080"
ENDPOINT = "/v1/orders/sync"

SECRET = "secret"

# ===========================
# Request Body
# ===========================
payload = {
    "eventType": "ORDER_CREATED",
    "eventTimestamp": "2026-06-17T09:00:00Z",
    "order": {
        "source": "MAGENTO",
        "orderNumber": "#10006",
        "currency": "USD",
        "totalAmount": 2400,
        "items": [
            {
                "name": "iPhone 16",
                "sku": "SKU001",
                "quantity": 2,
                "unitPrice": 1200
            },
            {
                "name": "iPhone 16",
                "sku": "SKU001",
                "quantity": 2,
                "unitPrice": 1200
            }
        ]
    }
}

# Serialize JSON once.
# IMPORTANT: Sign exactly what will be sent.
payload_str = json.dumps(
    payload,
    separators=(",", ":"),
    ensure_ascii=False
)

# ===========================
# Generate Timestamp
# ===========================
timestamp = str(int(time.time() * 1000))

# ===========================
# Generate Signature
# Java:
# data = timestamp.toEpochMilli() + "." + payload
# ===========================
data_to_sign = f"{timestamp}.{payload_str}"

signature = hmac.new(
    SECRET.encode("utf-8"),
    data_to_sign.encode("utf-8"),
    hashlib.sha256
).hexdigest()

# ===========================
# Headers
# ===========================
headers = {
    "Content-Type": "application/json",
    "X-Timestamp": timestamp,
    "X-Signature": signature,
}

print("Timestamp :", timestamp)
print("Signature :", signature)

# ===========================
# Send Request
# ===========================
response = requests.post(
    BASE_URL + ENDPOINT,
    headers=headers,
    data=payload_str
)

print("Status Code:", response.status_code)
print("Response:")
print(response.text)