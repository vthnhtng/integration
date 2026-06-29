import hmac, hashlib
import requests
import json

payload_dict = {
    "orderId": "ORDER-001",
    "platform": "SHOPIFY",
    "customer": {
        "name": "John Doe",
        "email": "john@example.com"
    },
    "items": [
        {
            "sku": "SKU-001",
            "quantity": 2,
            "price": 19.99
        }
    ]
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
