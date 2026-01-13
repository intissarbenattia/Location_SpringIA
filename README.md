# SmartRent - Plateforme Intelligente de Gestion d'Immobiles

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-green.svg)
![Angular](https://img.shields.io/badge/Angular-19.2.0-red.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)

## 📋 Description

**SmartRent** est une plateforme cloud-native moderne de gestion immobilière basée sur une architecture microservices. Elle offre une solution complète pour la gestion des locations, des réservations et des agents intelligents alimentés par l'IA.

### 🎯 Caractéristiques Principales

- ✅ **Architecture Microservices** scalable et distribuée
- ✅ **API Gateway** centralisée avec Spring Cloud Gateway
- ✅ **Service Discovery** automatique avec Eureka
- ✅ **Authentification & Autorisation** sécurisées (JWT)
- ✅ **Agents IA** intégrés via Spring AI (LLM support)
- ✅ **Frontend Moderne** avec Angular 19 et Material Design
- ✅ **Streaming Temps Réel** avec Streamlit
- ✅ **Base de Données Relationnelle** PostgreSQL/MySQL
- ✅ **Communication Inter-Services** avec Feign & REST

---

## 🏗️ Architecture et Microservices

```
SmartRent (Parent Maven)
├── 📍 location-service (Port 8081)
│   └── Gestion des propriétés immobilières et localisations
├── 🔖 reservation-service (Port 8082)
│   └── Gestion des réservations et disponibilités
├── 🔐 auth-service (Port 8083)
│   └── Authentification, autorisation et JWT
├── 🤖 agent-ia-service (Port 8084)
│   └── Agents intelligents avec Spring AI et LLM
├── 🚪 gateway-service (Port 8080)
│   └── API Gateway et routage des requêtes
├── 🔍 discovery-service (Port 8761)
│   └── Service Discovery et Eureka Server
└── 🎨 frontend-angular (Port 4200)
    └── Interface utilisateur Angular Material
```

### 📊 Modules Microservices

#### **1. Location Service** (Java/Spring Boot)
- ✓ CRUD opérations sur les propriétés
- ✓ Gestion des localisations géographiques
- ✓ Intégration Feign avec Reservation Service
- ✓ API REST standardisée

#### **2. Reservation Service** (Java/Spring Boot)
- ✓ Gestion complète des réservations
- ✓ Vérification des disponibilités
- ✓ Historique et détails de réservations
- ✓ Communication avec Location Service

#### **3. Auth Service** (Java/Spring Boot)
- ✓ Authentification utilisateurs (JWT)
- ✓ Gestion des rôles et permissions
- ✓ Refresh tokens
- ✓ Sécurité Spring Security

#### **4. Agent IA Service** (Java/Spring Boot)
- ✓ Intégration Spring AI 1.1.0
- ✓ Support des LLM (Ollama, OpenAI, etc.)
- ✓ Chatbots intelligents
- ✓ Traitement du langage naturel (NLP)

#### **5. API Gateway Service** (Java/Spring Boot)
- ✓ Routage centralisé des requêtes
- ✓ Load balancing
- ✓ Rate limiting
- ✓ Logging et monitoring

#### **6. Discovery Service** (Java/Spring Boot)
- ✓ Eureka Server pour le service discovery
- ✓ Health checks automatiques
- ✓ Registration/Deregistration des services

#### **7. Frontend Angular** (TypeScript/Angular 19)
- ✓ UI moderne avec Material Design
- ✓ Bootstrap 5 pour responsive design
- ✓ ApexCharts pour visualisation de données
- ✓ Routing et state management RxJS

#### **8. Streamlit App** (Python)
- ✓ Dashboards interactifs
- ✓ Visualisations temps réel
- ✓ Analytics et reporting

---

## 🚀 Installation et Configuration

### Prérequis

- **Java 21** ou supérieur
- **Maven 3.8+** ou utiliser `mvnw`
- **Node.js 18+** (pour Angular)
- **npm ou yarn**
- **Docker** (optionnel mais recommandé)
- **PostgreSQL/MySQL** (optionnel, H2 en-memory par défaut)

### 1️⃣ Cloner le Repository

```bash
git clone https://github.com/intissarbenattia/locaa.git
cd loca
```

### 2️⃣ Configuration des Variables d'Environnement

Créez un fichier `.env` à la racine du projet :

```bash
# Postgres Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=smartrent
DB_USER=postgres
DB_PASSWORD=your_password

# JWT Configuration
JWT_SECRET=your_secret_key_here
JWT_EXPIRATION=86400000

# Spring AI Configuration
SPRING_AI_OPENAI_API_KEY=sk-your-api-key
# OU pour Ollama
OLLAMA_BASE_URL=http://localhost:11434

# Services Configuration
EUREKA_SERVER=http://localhost:8761
GATEWAY_URL=http://localhost:8080
```

### 3️⃣ Construction du Projet

#### Construire tous les services Maven :

```bash
# Windows
mvnw clean package -DskipTests

# Linux/Mac
./mvnw clean package -DskipTests
```

#### Construire le Frontend Angular :

```bash
cd frontend-angular
npm install
npm run build
cd ..
```

### 4️⃣ Démarrage des Services

#### Option A : Démarrage Manuel

```bash
# Terminal 1 - Discovery Service
cd discovery-service
mvnw spring-boot:run

# Terminal 2 - Location Service
cd location-service
mvnw spring-boot:run

# Terminal 3 - Reservation Service
cd reservation-service
mvnw spring-boot:run

# Terminal 4 - Auth Service
cd auth-service
mvnw spring-boot:run

# Terminal 5 - Agent IA Service
cd agent-ia-service
mvnw spring-boot:run

# Terminal 6 - Gateway Service
cd gateway-service
mvnw spring-boot:run

# Terminal 7 - Frontend Angular
cd frontend-angular
npm start
```

#### Option B : Scripts Batch (Windows)

```bash
# Démarrer tous les services
start-services.bat

# Avec support Ollama
start-all-with-ollama.bat

# Tester les services
test-services.bat
```

#### Option C : Docker Compose (Recommandé)

```bash
docker-compose up -d
```

---

## 📡 URLs d'Accès

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend** | http://localhost:4200 | Interface utilisateur Angular |
| **API Gateway** | http://localhost:8080 | Point d'entrée API |
| **Eureka Server** | http://localhost:8761 | Service Discovery Dashboard |
| **Location Service** | http://localhost:8081 | API Locations |
| **Reservation Service** | http://localhost:8082 | API Réservations |
| **Auth Service** | http://localhost:8083 | API Authentification |
| **Agent IA Service** | http://localhost:8084 | API IA/Chatbot |
| **Streamlit App** | http://localhost:8501 | Dashboards |

---

## 🔌 API REST Endpoints

### Authentication
```http
POST /api/auth/login
POST /api/auth/register
POST /api/auth/refresh-token
```

### Locations
```http
GET    /api/locations                    # Lister toutes les propriétés
GET    /api/locations/{id}               # Détails d'une propriété
POST   /api/locations                    # Créer une propriété
PUT    /api/locations/{id}               # Modifier une propriété
DELETE /api/locations/{id}               # Supprimer une propriété
```

### Reservations
```http
GET    /api/reservations                 # Lister les réservations
GET    /api/reservations/{id}            # Détails d'une réservation
POST   /api/reservations                 # Créer une réservation
PUT    /api/reservations/{id}            # Modifier une réservation
DELETE /api/reservations/{id}            # Annuler une réservation
```

### IA Agent
```http
POST /api/agent/chat                     # Chat avec l'agent IA
POST /api/agent/query                    # Query à l'IA
GET  /api/agent/status                   # Status de l'agent
```

---

## 🧪 Testing

### Unit Tests
```bash
mvnw test
```

### Integration Tests
```bash
mvnw verify
```

### Load Tests (JMeter)
```bash
test-services.bat
```

### Test avec Postman
Imports des collections :
- `location-service/postman_collection.json`
- `reservation-service/postman_collection.json`

---

## 📊 Structure du Projet

```
smartrent/
├── location-service/          # Service de gestion des locations
│   ├── src/main/java/
│   ├── src/test/java/
│   ├── pom.xml
│   └── postman_collection.json
├── reservation-service/       # Service de gestion des réservations
│   ├── src/main/java/
│   ├── src/test/java/
│   ├── pom.xml
│   └── postman_collection.json
├── auth-service/              # Service d'authentification
│   ├── src/main/java/
│   ├── src/test/java/
│   └── pom.xml
├── agent-ia-service/          # Service IA avec Spring AI
│   ├── src/main/java/
│   ├── src/test/java/
│   └── pom.xml
├── gateway-service/           # API Gateway
│   ├── src/main/java/
│   ├── src/test/java/
│   └── pom.xml
├── discovery-service/         # Eureka Discovery Service
│   ├── src/main/java/
│   ├── src/test/java/
│   └── pom.xml
├── frontend-angular/          # Frontend Angular
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── angular.json
├── streamlit-app/             # Dashboards Python
│   ├── app.py
│   └── requirements.txt
├── pom.xml                    # Parent POM Maven
├── README.md                  # Ce fichier
├── .gitignore                 # Git ignore rules
└── docker-compose.yml         # Docker configuration
```

---

## 🔐 Sécurité

- ✅ **JWT Authentication** pour les API
- ✅ **Spring Security** sur tous les services
- ✅ **HTTPS/TLS** en production
- ✅ **CORS** configuré sur la Gateway
- ✅ **Rate Limiting** sur l'API Gateway
- ✅ **Input Validation** sur tous les endpoints
- ✅ **SQL Injection Prevention** avec Prepared Statements

### Exemple Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123"
  }'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "...",
  "expiresIn": 86400
}
```

---

## 🤖 Integration IA avec Spring AI

### Configuration Ollama (Local)
```properties
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.embedding.model=nomic-embed-text
```

### Configuration OpenAI (Cloud)
```properties
spring.ai.openai.api-key=${SPRING_AI_OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4
```

### Exemple d'Utilisation
```java
@RestController
@RequestMapping("/api/agent")
public class AgentController {
    @Autowired
    private ChatClient chatClient;
    
    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        return chatClient.call(message);
    }
}
```

---

## 📈 Monitoring & Observabilité

- **Spring Boot Actuator** : `/actuator`
- **Health Checks** : `/actuator/health`
- **Metrics** : `/actuator/metrics`
- **Logs** : Centralisé via SLF4J + Logback
- **Tracing Distribué** : Spring Cloud Sleuth (optionnel)

---

## 🤝 Contribution

1. **Fork** le repository
2. Créez une branche (`git checkout -b feature/AmazingFeature`)
3. **Commitez** vos changements (`git commit -m 'Add AmazingFeature'`)
4. **Poussez** la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une **Pull Request**

### Conventions de Code
- Respect des conventions Java (camelCase, etc.)
- Tests unitaires pour chaque fonctionnalité
- Documentation des API avec Javadoc
- Commits explicites et atomiques

---

## 📝 Licence

Ce projet est sous licence **MIT**. Voir [LICENSE](LICENSE) pour plus de détails.

---

## 👥 Auteurs et Contributeurs

- **Intissar Ben Attia** - Architecture & Développement Principal
- Liste des contributeurs à venir

---

## 📞 Support et Assistance

- 📧 **Email** : support@smartrent.com
- 💬 **Issues** : GitHub Issues
- 📖 **Documentation** : [Wiki](../../wiki)
- 🐛 **Bug Reports** : [Bug Tracker](../../issues)

---

## 📚 Ressources Supplémentaires

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Angular Documentation](https://angular.io/docs)
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Eureka Documentation](https://github.com/Netflix/eureka/wiki)

---

## 🎯 Roadmap Futur

- [ ] Intégration Docker Swarm
- [ ] Kubernetes manifests (Helm charts)
- [ ] GraphQL API
- [ ] WebSockets pour notifications temps réel
- [ ] Machine Learning pour prédiction de prix
- [ ] Multi-language support (i18n)
- [ ] Mobile App (React Native)
- [ ] Advanced Analytics Dashboard

---

**Dernière mise à jour** : Janvier 2026

Fait avec ❤️ par SmartRent Team
