@echo off
REM Script de redémarrage des microservices SmartRent
REM Ordre de démarrage OBLIGATOIRE pour les dépendances

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║   🚀 Redémarrage des Microservices SmartRent               ║
echo ║   Synchronisation Frontend-Backend 16 MCP Tools            ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

REM Couleurs (simples avec format)
setlocal enabledelayedexpansion

echo [1/7] ⏹️  Arrêt des services existants...
taskkill /F /IM java.exe 2>nul
timeout /t 2 /nobreak

echo.
echo [2/7] 🔄 Nettoyage des caches et bases de données...
REM Supprimer les fichiers .mv.db (H2) si présents
if exist "%cd%\discovery-service\target\h2data.mv.db" del "%cd%\discovery-service\target\h2data.mv.db"
if exist "%cd%\auth-service\target\h2data.mv.db" del "%cd%\auth-service\target\h2data.mv.db"
if exist "%cd%\location-service\target\h2data.mv.db" del "%cd%\location-service\target\h2data.mv.db"
if exist "%cd%\reservation-service\target\h2data.mv.db" del "%cd%\reservation-service\target\h2data.mv.db"
if exist "%cd%\gateway-service\target\h2data.mv.db" del "%cd%\gateway-service\target\h2data.mv.db"
if exist "%cd%\agent-ia-service\target\h2data.mv.db" del "%cd%\agent-ia-service\target\h2data.mv.db"

echo.
echo [3/7] 🏠 Démarrage Discovery Service (port 8761)...
cd discovery-service
start "Discovery Service" cmd /k mvn spring-boot:run 2>nul
cd ..
timeout /t 3 /nobreak

echo.
echo [4/7] 🏘️  Démarrage Location Service (port 9091)...
cd location-service
start "Location Service" cmd /k mvn spring-boot:run 2>nul
cd ..
timeout /t 3 /nobreak

echo.
echo [5/7] 📅 Démarrage Reservation Service (port 9092)...
cd reservation-service
start "Reservation Service" cmd /k mvn spring-boot:run 2>nul
cd ..
timeout /t 3 /nobreak

echo.
echo [6/7] 🌉 Démarrage Gateway Service (port 8888)...
cd gateway-service
start "Gateway Service" cmd /k mvn spring-boot:run 2>nul
cd ..
timeout /t 3 /nobreak

echo.
echo [7/7] 🤖 Démarrage Agent IA Service (port 8081)...
cd agent-ia-service
start "Agent IA Service" cmd /k mvn spring-boot:run 2>nul
cd ..

echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║   ✅ Services démarrés! Attente du chargement complet...   ║
echo ║   Cela peut prendre 30-60 secondes...                       ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

timeout /t 10 /nobreak

echo.
echo 📍 Services Status:
echo   • Discovery Service (Eureka): http://localhost:8761
echo   • Location Service: http://localhost:9091
echo   • Reservation Service: http://localhost:9092
echo   • Gateway Service: http://localhost:8888
echo   • Agent IA Service: http://localhost:8081
echo.

echo 🧪 Vérification des données:
echo   • Locations count: http://localhost:8888/api/debug/locations/count
echo   • Apartments: http://localhost:8888/api/debug/locations/by-type/APPARTEMENT
echo   • Houses: http://localhost:8888/api/debug/locations/by-type/MAISON
echo   • Cars: http://localhost:8888/api/debug/locations/by-type/VOITURE
echo.

echo 🎯 Test Chat:
echo   • Ouvre http://localhost:4200
echo   • Écris: "Quels appartements sont disponibles ?"
echo   • Expected: 3 appartements avec détails
echo.

pause
