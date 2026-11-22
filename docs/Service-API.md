# GameForge Service API

## Overview

The GameForge service API provides read-only access to the complete video-game catalog. All service routes use HTTP Basic authentication backed by registered users in MySQL.

Base path: `/service`

## Authentication

Send a registered username and password using the standard HTTP `Authorization: Basic ...` header. New registrations are stored with BCrypt password hashes.

| Condition | Response |
|---|---|
| Valid credentials | Request continues to the controller |
| Missing or invalid credentials | `401 Unauthorized` with a `WWW-Authenticate` header |

## Product representation

Each product contains:

| Field | Type | Description |
|---|---|---|
| `productId` | integer | Database game identifier |
| `title` | string | Game title |
| `price` | decimal | Catalog price |
| `platforms` | array of strings | Supported platform names |
| `releaseDate` | date string | ISO-8601 release date (`YYYY-MM-DD`) |
| `description` | string | Game description |
| `category` | string | Game category or genre |

Representative JSON product:

```json
{
  "productId": 1,
  "title": "GameForge Demo",
  "price": 19.99,
  "platforms": ["PC", "Xbox"],
  "releaseDate": "2026-01-01",
  "description": "A sample catalog game.",
  "category": "Action"
}
```

## Endpoints

### Get catalog as JSON

`GET /service/getjson`

- Authentication: required
- Request body: none
- Success: `200 OK`
- Content type: `application/json`
- Response body: array of product objects; an empty catalog returns an empty array

### Get catalog as XML

`GET /service/getxml`

- Authentication: required
- Request body: none
- Success: `200 OK`
- Content type: `application/xml`
- Response body: Jackson XML serialization of the product list; an empty catalog returns an empty list representation

## Error behavior

| Status | Meaning |
|---|---|
| `401 Unauthorized` | Credentials were missing, the username was unknown, or the password did not match the stored BCrypt hash |
| `500 Internal Server Error` | An unexpected application or database error occurred |

The current controller is intentionally read-only. Catalog creation, editing, and deletion remain server-rendered MVC operations and are not exposed under `/service`.
