@echo off
REM Script pour tester les services après démarrage

echo.
echo ========================================
echo Test des Services SmartRent
echo ========================================
echo.

echo [TEST 1] Discovery Service (Eureka)...
curl -s http://localhost:8761/eureka/apps | findstr /c:"UP" >nul && echo ✓ OK || echo ✗ FAILED
timeout /t 2 /nobreak

echo [TEST 2] Auth Service...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:8080/api/auth/health
timeout /t 2 /nobreak

echo [TEST 3] Location Service...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:9091/api/locations
timeout /t 2 /nobreak

echo [TEST 4] Reservation Service...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:9092/api/reservations
timeout /t 2 /nobreak

echo [TEST 5] Agent IA Service...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:8081/api/agent/health
timeout /t 2 /nobreak

echo [TEST 6] Gateway Service...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:8888/
timeout /t 2 /nobreak

echo [TEST 7] Gateway routing to Agent IA...
curl -s -X POST http://localhost:8888/api/agent/chat -H "Content-Type: application/json" -d "{\"message\":\"test\"}" | findstr /c:"error" >nul && echo ✗ ERROR || echo ✓ Response received
timeout /t 2 /nobreak

echo.
echo ========================================
echo Tests terminés!
echo ========================================
echo.
pause
