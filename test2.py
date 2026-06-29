import requests

BASE_URL = "http://localhost:8080"
ENDPOINT = "/v1/orders/sync"
response = requests.post(BASE_URL + ENDPOINT, data="{}", headers={"Content-Type": "application/json"})
print("Status Code:", response.status_code)
