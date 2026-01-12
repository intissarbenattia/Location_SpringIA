@echo off
REM Script pour démarrer Ollama et vérifier la configuration

echo.
echo ========================================
echo Ollama Setup for SmartRent
echo ========================================
echo.

echo [1] Vérification d'Ollama...
ollama --version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ❌ Ollama n'est pas installé!
    echo.
    echo Pour installer Ollama:
    echo   1. Téléchargez Ollama depuis: https://ollama.ai
    echo   2. Installez-le
    echo   3. Relancez ce script
    echo.
    pause
    exit /b 1
)
echo ✅ Ollama est installé

echo.
echo [2] Démarrage du serveur Ollama (http://localhost:11434)...
echo.
start "Ollama Server" ollama serve
timeout /t 5 /nobreak

echo.
echo [3] Vérification de la disponibilité du serveur...
:retry_check
curl -s http://localhost:11434/api/version >nul 2>&1
if %errorlevel% neq 0 (
    echo Attente du démarrage du serveur...
    timeout /t 2 /nobreak
    goto retry_check
)
echo ✅ Serveur Ollama démarré!

echo.
echo [4] Vérification des modèles disponibles...
ollama list
echo.

echo [5] Si vous n'avez pas le modèle 'llama3:8b', téléchargez-le avec:
echo    ollama pull llama3:8b
echo.

echo ✅ Ollama est prêt!
echo.
echo Vous pouvez maintenant lancer: start-all-with-ollama.bat
echo.
pause
