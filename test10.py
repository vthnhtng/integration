

import hmac, hashlib
import requests
import json
import time

payload_dict = {
    "eventType": "ORDER_CREATED",
    "eventTimestamp": "2023-01-01T00:00:00Z",
    "order": {
        "source": "SHOPIFY",
        "orderNumber": "ORD-123",
        "currency": "USD",
        "totalAmount": 100.00,
        "items": [
            {
                "name": "Widget",
                "sku": "SKU-001",
                "quantity": 2,
                "unitPrice": 50.00
            }
        ]
    }
}
payload = json.dumps(payload_dict, separators=(",", ":"), ensure_ascii=False)

timestamp = "1719660000000"
data = timestamp + "." + payload
signature = hmac.new(b"secret", data.encode("utf-8"), hashlib.sha256).hexdigest()

BASE_URL = "http://localhost:8080"
ENDPOINT = "/v1/orders/sync"
response = requests.post(BASE_URL + ENDPOINT, data=payload, headers={"Content-Type": "application/json", "X-Timestamp": timestamp, "X-Signature": signature})
print("Status Code:", response.status_code)
print("Response text:", response.text)
