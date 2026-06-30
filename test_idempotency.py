"""
Idempotency test: send 3 duplicate requests with same order number + platform.
First should succeed (202), subsequent should fail with order-exists (400).
"""
import json
import time
import hmac
import hashlib
import requests

BASE_URL = "http://localhost:8080"
ENDPOINT = "/v1/orders/sync"
SECRET = "secret"


def sign(payload: dict) -> tuple:
    payload_str = json.dumps(payload, separators=(",", ":"), ensure_ascii=False)
    timestamp = str(int(time.time() * 1000))
    data_to_sign = f"{timestamp}.{payload_str}"
    signature = hmac.new(
        SECRET.encode("utf-8"),
        data_to_sign.encode("utf-8"),
        hashlib.sha256,
    ).hexdigest()
    return payload_str, timestamp, signature


def send(payload: dict) -> requests.Response:
    payload_str, timestamp, signature = sign(payload)
    headers = {
        "Content-Type": "application/json",
        "X-Timestamp": timestamp,
        "X-Signature": signature,
    }
    return requests.post(BASE_URL + ENDPOINT, headers=headers, data=payload_str)


# ── Payload ──────────────────────────────────────────────────────────────────
# Idempotency key = (source, orderNumber) via unique constraint uk_order_source_number
request_body = {
    "eventType": "ORDER_SYNC",
    "eventTimestamp": "2026-06-30T10:00:00Z",
    "order": {
        "source": "SHOPIFY",
        "orderNumber": "ORDER-002",
        "currency": "USD",
        "totalAmount": 39.98,
        "items": [
            {
                "name": "Test Product",
                "sku": "SKU-002",
                "quantity": 2,
                "unitPrice": 19.99,
            }
        ],
    },
}

print("=" * 60)
print("Test 1/3 — First request (expected: 202 ACCEPTED)")
print("=" * 60)
r1 = send(request_body)
print(f"Status: {r1.status_code}")
print(f"Body:   {r1.text}")
print()

print("=" * 60)
print("Test 2/3 — Duplicate (expected: 400, order exists)")
print("=" * 60)
r2 = send(request_body)
print(f"Status: {r2.status_code}")
print(f"Body:   {r2.text}")
print()

print("=" * 60)
print("Test 3/3 — Another duplicate (expected: 400, order exists)")
print("=" * 60)
r3 = send(request_body)
print(f"Status: {r3.status_code}")
print(f"Body:   {r3.text}")
print()

# Summary
print("─" * 60)
statuses = [r.status_code for r in (r1, r2, r3)]
print(f"Summary: {statuses}")
if statuses[0] == 202 and all(s == 400 for s in statuses[1:]):
    print("✅ Idempotency working as expected")
elif statuses[0] == 202 and statuses[1] == 400:
    print("✅ Idempotency working (first OK, second blocked — third may differ)")
elif statuses[0] != 202:
    print("⚠ First request didn't get 202 — check validation / server state")
    print("  (You may need to use a unique orderNumber each run)")
else:
    print("❌ Unexpected results")
