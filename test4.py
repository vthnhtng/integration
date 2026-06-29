import hmac, hashlib
import requests

timestamp = "1719660000000"
payload = "{}"
data = timestamp + "." + payload
signature = hmac.new(b"secret", data.encode("utf-8"), hashlib.sha256).hexdigest()

BASE_URL = "http://localhost:8080"
ENDPOINT = "/v1/orders/sync"
response = requests.post(BASE_URL + ENDPOINT, data=payload, headers={"Content-Type": "application/json", "X-Timestamp": timestamp, "X-Signature": signature})
print("Status Code:", response.status_code)
