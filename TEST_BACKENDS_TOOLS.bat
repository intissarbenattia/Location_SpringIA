@echo off
REM 🧪 Test des Outils Backend - Version Windows CMD

setlocal enabledelayedexpansion

echo.
echo ==========================================
echo 🧪 TEST COMPLET DES OUTILS BACKEND
echo ==========================================
echo.

REM Test Location Service
echo LOCATION SERVICE (Port 9091)
echo ===========================

echo.
echo [1/9] rechercher_locations
curl -s http://localhost:9091/api/locations | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo [2/9] rechercher_locations_disponibles
curl -s http://localhost:9091/api/locations/available | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo [3/9] rechercher_par_type APPARTEMENT
curl -s http://localhost:9091/api/locations/type/APPARTEMENT | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo [4/9] rechercher_par_type MAISON
curl -s http://localhost:9091/api/locations/type/MAISON | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo [5/9] rechercher_par_type VOITURE
curl -s http://localhost:9091/api/locations/type/VOITURE | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo [6/9] rechercher_disponibles_par_type APPARTEMENT
curl -s http://localhost:9091/api/locations/available/APPARTEMENT | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo [7/9] rechercher_disponibles_par_type MAISON
curl -s http://localhost:9091/api/locations/available/MAISON | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo [8/9] rechercher_disponibles_par_type VOITURE
curl -s http://localhost:9091/api/locations/available/VOITURE | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo [9/9] details_location 65 (first location)
curl -s http://localhost:9091/api/locations/65 | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo RESERVATION SERVICE (Port 9092)
echo ================================

echo.
echo [10/12] rechercher_reservations
curl -s http://localhost:9092/api/reservations | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo [11/12] rechercher_reservations_confirmees
curl -s http://localhost:9092/api/reservations/statut/CONFIRMÉE | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo [12/12] details_reservation 1
curl -s http://localhost:9092/api/reservations/1 | find "id" >nul
if !errorlevel! equ 0 (echo ✅ SUCCÈS) else (echo ❌ ERREUR)

echo.
echo ==========================================
echo 🤖 TEST AGENT IA SERVICE (Port 8081)
echo ==========================================
echo.

echo Test: Quels appartements sont disponibles ?
curl -s -X POST http://localhost:8081/api/chat/test-session ^
  -H "Content-Type: application/json" ^
  -d "{\"message\":\"Quels appartements sont disponibles ?\"}"

echo.
echo ==========================================
echo ✅ TEST TERMINÉ
echo ==========================================
echo.

pause
