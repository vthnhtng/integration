import requests

BASE_URL = "http://localhost:8080"
ENDPOINT = "/v1/orders/sync"
response = requests.post(BASE_URL + ENDPOINT, data="abc", headers={"Content-Type": "application/json", "X-Timestamp": "1719660000000", "X-Signature": "abc"})
print("Status Code:", response.status_code)
print("Response text:", response.text)
