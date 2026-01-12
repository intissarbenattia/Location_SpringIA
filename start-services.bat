@echo off
REM Script pour démarrer tous les microservices dans le bon ordre

echo ========================================
echo SmartRent Microservices Startup
echo ========================================
echo.

REM Définir les variables
set DISCOVERY_PORT=8761
set AUTH_PORT=8080
set LOCATION_PORT=9091
set RESERVATION_PORT=9092
set AGENT_PORT=8081
set GATEWAY_PORT=8888

echo [1/6] Démarrage Discovery Service (port %DISCOVERY_PORT%)...
start "Discovery Service" cmd /k "cd discovery-service && mvn spring-boot:run"
timeout /t 15 /nobreak

echo [2/6] Démarrage Auth Service (port %AUTH_PORT%)...
start "Auth Service" cmd /k "cd auth-service && mvn spring-boot:run"
timeout /t 10 /nobreak

echo [3/6] Démarrage Location Service (port %LOCATION_PORT%)...
start "Location Service" cmd /k "cd location-service && mvn spring-boot:run"
timeout /t 10 /nobreak

echo [4/6] Démarrage Reservation Service (port %RESERVATION_PORT%)...
start "Reservation Service" cmd /k "cd reservation-service && mvn spring-boot:run"
timeout /t 10 /nobreak

echo [5/6] Démarrage Agent IA Service (port %AGENT_PORT%)...
start "Agent IA Service" cmd /k "cd agent-ia-service && mvn spring-boot:run"
timeout /t 10 /nobreak

echo [6/6] Démarrage Gateway Service (port %GATEWAY_PORT%)...
start "Gateway Service" cmd /k "cd gateway-service && mvn spring-boot:run"
timeout /t 10 /nobreak

echo.
echo ========================================
echo Tous les services ont été lancés!
echo ========================================
echo.
echo Vérifiez que les services sont en cours d'exécution:
echo - Discovery:   http://localhost:%DISCOVERY_PORT%/eureka/apps
echo - Auth:        http://localhost:%AUTH_PORT%/api/auth/health
echo - Location:    http://localhost:%LOCATION_PORT%/api/locations
echo - Reservation: http://localhost:%RESERVATION_PORT%/api/reservations
echo - Agent IA:    http://localhost:%AGENT_PORT%/api/agent/health
echo - Gateway:     http://localhost:%GATEWAY_PORT%
echo.
pause
