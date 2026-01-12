# 📮 Collections Postman - SmartRent V2

Toutes les collections Postman pour tester les microservices de SmartRent.

---

## 🔐 AUTH SERVICE (Port 8080)
**File:** `auth-service/postman_collection.json`

### Endpoints testés:

#### 1. **Login** - POST
```
http://localhost:8080/api/auth/login
```
**Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```
**Response:** JWT Token
- admin / admin123
- user / user123

#### 2. **Validate Token** - GET
```
http://localhost:8080/api/auth/validate
```
**Headers:**
```
Authorization: Bearer YOUR_JWT_TOKEN_HERE
```
**Response:** Token validation status

---

## 🏠 LOCATION SERVICE (Port 9091)
**File:** `location-service/postman_collection.json`

### Endpoints testés:

#### 1. **Get All Locations** - GET
```
http://localhost:9091/api/locations
```
**Response:** Liste complète de toutes les locations

#### 2. **Get Location by ID** - GET
```
http://localhost:9091/api/locations/1
```
**Response:** Détails d'une location spécifique

#### 3. **Get Available Locations** - GET
```
http://localhost:9091/api/locations/disponibles
```
**Response:** Locations actuellement disponibles

#### 4. **Get Locations by Type** - GET
```
http://localhost:9091/api/locations/type/Appartement
```
**Types disponibles:**
- Appartement
- Maison
- Voiture
- Équipement

**Response:** Locations d'un type spécifique

#### 5. **Create Location** - POST
```
http://localhost:9091/api/locations
```
**Body:**
```json
{
  "titre": "Bel Appartement",
  "description": "Description détaillée",
  "type": "Appartement",
  "prix": 100.00,
  "adresse": "123 Rue de Paris",
  "disponible": true
}
```

#### 6. **Update Location** - PUT
```
http://localhost:9091/api/locations/1
```
**Body:** Même structure que Create

#### 7. **Delete Location** - DELETE
```
http://localhost:9091/api/locations/1
```

### Exemples de création:

#### Create Apartment
```
POST http://localhost:9091/api/locations
Body: Appartement à Paris
```

#### Create House
```
POST http://localhost:9091/api/locations
Body: Maison avec jardin
```

#### Create Car
```
POST http://localhost:9091/api/locations
Body: Voiture de location
```

#### Create Equipment
```
POST http://localhost:9091/api/locations
Body: Équipement (mobilier, outils, etc.)
```

---

## 📅 RESERVATION SERVICE (Port 9092)
**File:** `reservation-service/postman_collection.json`

### Endpoints testés:

#### 1. **Health Check** - GET
```
http://localhost:9092/api/reservations
```
**Response:** List of all reservations

#### 2. **Create Reservation** - POST
```
http://localhost:9092/api/reservations
```
**Body:**
```json
{
  "locationId": 1,
  "clientNom": "Jean Dupont",
  "clientEmail": "jean.dupont@example.com",
  "dateDebut": "2025-02-01",
  "dateFin": "2025-02-05"
}
```

#### 3. **Get Reservation by ID** - GET
```
http://localhost:9092/api/reservations/1
```

#### 4. **Get Reservations by Location** - GET
```
http://localhost:9092/api/reservations/location/1
```

#### 5. **Get Reservations by Client Email** - GET
```
http://localhost:9092/api/reservations/email/jean.dupont@example.com
```

#### 6. **Get Reservations by Status** - GET
```
http://localhost:9092/api/reservations/statut/CONFIRMÉE
```
**Status options:**
- CONFIRMÉE
- EN_ATTENTE
- ANNULÉE

#### 7. **Update Reservation** - PUT
```
http://localhost:9092/api/reservations/1
```

#### 8. **Confirm Reservation** - POST
```
http://localhost:9092/api/reservations/1/confirm
```

#### 9. **Cancel Reservation** - POST
```
http://localhost:9092/api/reservations/1/cancel
```

#### 10. **Delete Reservation** - DELETE
```
http://localhost:9092/api/reservations/1
```

---

## 🚀 GATEWAY SERVICE (Port 8888)
**Routeur principal vers tous les services**
```
Gateway Base: http://localhost:8888
```

**Routes disponibles:**
- `/api/auth/*` → Auth Service (8080)
- `/api/locations/*` → Location Service (9091)
- `/api/reservations/*` → Reservation Service (9092)
- `/api/agent/*` → Agent IA Service (8081)

---

## 💬 AGENT IA SERVICE (Port 8081)
**Pour les requêtes IA et MCP**
```
http://localhost:8081/api/agent/chat
```

**Body:**
```json
{
  "message": "Montre-moi les locations disponibles",
  "sessionId": "optional-session-id"
}
```

---

## 📊 Résumé des Collections

| Service | Port | Collection | Endpoints |
|---------|------|-----------|-----------|
| **Auth** | 8080 | ✅ postman_collection.json | 2 |
| **Location** | 9091 | ✅ postman_collection.json | 11+ |
| **Reservation** | 9092 | ✅ postman_collection.json | 10 |
| **Gateway** | 8888 | — | Routes tous |
| **Agent IA** | 8081 | — | Chat & MCP |

---

## 🔗 Import dans Postman

1. Ouvrir **Postman**
2. Cliquer sur **Import**
3. Sélectionner le fichier `postman_collection.json`
4. Choisir le répertoire:
   - `auth-service/postman_collection.json`
   - `location-service/postman_collection.json`
   - `reservation-service/postman_collection.json`

---

## 💡 Notes Importantes

- **Tous les services** sont accessibles via le **Gateway (8888)**
- Les **tokens JWT** du login auth doivent être mis dans le header `Authorization: Bearer TOKEN`
- Les **collections** incluent les exemples de data avec des Apartments, Houses, Cars, Equipment
- Le **réservation-service** utilise le port **9092** (pas 8082 comme indiqué dans la collection)

---

**Créé:** 7 Janvier 2026
**Version:** SmartRent V2 Complete
