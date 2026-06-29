import requests

BASE_URL = "http://localhost:8080"
ENDPOINT = "/error"
response = requests.post(BASE_URL + ENDPOINT)
print("Status Code:", response.status_code)
