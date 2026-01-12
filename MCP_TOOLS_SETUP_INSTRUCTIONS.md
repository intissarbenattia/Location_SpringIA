## 🚀 INSTRUCTIONS DE REDÉMARRAGE - MCP TOOLS 100% FONCTIONNELS

### ⚠️ ÉTAPE 1 : ARRÊTER TOUS LES SERVICES

Appuyez sur **Ctrl+C** dans chaque terminal pour arrêter tous les services en cours :

- Location Service (port 9091)
- Reservation Service (port 9092)  
- Agent IA Service (port 8081)
- Gateway (port 8888)
- Discovery Service (port 8761)

### 🔄 ÉTAPE 2 : NETTOYER LES BASES DE DONNÉES

```bash
# Option 1 : Supprimer les fichiers H2 ou MySQL (si utilisé)
# Pour H2 (fichiers dans le répertoire du service):
# Supprimez les fichiers .mv.db ou .trace.db

# Pour MySQL, connectez-vous et exécutez:
mysql -u root -p
> DROP DATABASE db_locations;
> DROP DATABASE db_reservation;
> CREATE DATABASE db_locations;
> CREATE DATABASE db_reservation;
> exit
```

### 🟢 ÉTAPE 3 : REDÉMARRER LES SERVICES DANS L'ORDRE CORRECT

#### 1️⃣ Discovery Service (Eureka)
```bash
cd c:\loca\discovery-service
mvn clean spring-boot:run
# Attendez le message: "Eureka started in ... ms"
```

#### 2️⃣ Location Service
```bash
cd c:\loca\location-service
mvn clean spring-boot:run
# Attendez: ✅ 11 Locations initialisées
```

#### 3️⃣ Reservation Service
```bash
cd c:\loca\reservation-service
mvn clean spring-boot:run
# Attendez: ✅ 9 Réservations initialisées
```

#### 4️⃣ Gateway Service
```bash
cd c:\loca\gateway-service
mvn clean spring-boot:run
# Attendez: "Started GatewayApplication"
```

#### 5️⃣ Agent IA Service
```bash
cd c:\loca\agent-ia-service
mvn clean spring-boot:run
# Attendez: "Started AgentIaApplication"
```

#### 6️⃣ Frontend Angular
```bash
cd c:\loca\frontend-angular
ng serve
# Attendez: "Application bundle generation successful"
```

### ✅ ÉTAPE 4 : VÉRIFIER LES DONNÉES

#### Tester les endpoints debug Location Service
```bash
# Compter les locations
curl http://localhost:9091/api/debug/locations/count

# Vérifier les appartements
curl http://localhost:9091/api/debug/locations/by-type/APPARTEMENT

# Vérifier les voitures
curl http://localhost:9091/api/debug/locations/by-type/VOITURE
```

**Réponses attendues :**
- Total : 11 locations (3 APPARTEMENT, 3 MAISON, 1 STUDIO, 3 VOITURE, 2 EQUIPEMENT)
- APPARTEMENT disponibles : 3 (Studio Centre-Ville, Appartement 2P Marais, Loft moderne Bastille)
- VOITURE disponibles : 3 (Peugeot 208, Renault Clio, BMW Série 5)

### 🧪 ÉTAPE 5 : TESTER LE CHAT IA

Ouvrez `http://localhost:4200` et testez les requêtes :

**Test 1 :**
```
Quels appartements sont disponibles ?
```
✅ Attendu : 3 appartements avec noms complets

**Test 2 :**
```
Montre-moi les voitures de location
```
✅ Attendu : 3 voitures avec noms et prix

**Test 3 :**
```
Récupère toutes les locations
```
✅ Attendu : 11 locations au total (tous les types)

**Test 4 :**
```
Y a-t-il des réservations confirmées ?
```
✅ Attendu : 9 réservations avec noms des locations

### 🔍 DEBUGGING EN CAS DE PROBLÈME

**Si vous voyez seulement 2 locations :**
1. La base de données n'a pas été supprimée avant le redémarrage
2. Arrêtez le Location Service
3. Supprimez les fichiers de base de données
4. Redémarrez le Location Service

**Si les outils retournent des erreurs de paramètres :**
1. Les logs du service doivent afficher les messages de débogage
2. Vérifiez les logs du Agent IA Service pour voir quels outils sont appelés
3. Vérifiez que les outils MCP sont correctement reconnus

**Logs à surveiller :**
```
✅ Tool: rechercher_locations appelé
✅ 11 locations trouvées au total
✅ Tool: rechercher_appartements_disponibles appelé
✅ 3 appartements disponibles trouvés
```

### 🎯 RÉSULTAT FINAL ATTENDU

Après redémarrage complet, TOUS les outils MCP doivent :
- ✅ Retourner les données COMPLÈTES (11 locations, 9 réservations)
- ✅ Afficher les NOMS des locations et clients (pas les IDs)
- ✅ Fonctionner sans erreurs
- ✅ Être appelés automatiquement par le Chat IA
- ✅ Retourner des résultats PROFESSIONNELS et COMPLETS

---

**Besoin d'aide ?** Vérifiez les logs des services - ils affichent tous les détails du débogage !
