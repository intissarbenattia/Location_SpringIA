#!/bin/bash

# 🧪 Test des Outils Backend et Responses Frontend
# Ce script teste tous les outils pour vérifier les résultats

echo "=========================================="
echo "🧪 TEST COMPLET DES OUTILS BACKEND"
echo "=========================================="
echo ""

# Fonction pour afficher les résultats
test_tool() {
    local name=$1
    local url=$2
    echo "▶️  Test: $name"
    echo "📍 URL: $url"
    
    response=$(curl -s "$url" 2>&1)
    
    # Vérifier si la réponse contient des données
    if echo "$response" | grep -q '"id"'; then
        count=$(echo "$response" | grep -o '"id"' | wc -l)
        echo "✅ SUCCÈS - Données reçues: $count éléments"
    elif echo "$response" | grep -q '"totalCount"'; then
        total=$(echo "$response" | grep -o '"totalCount":[0-9]*' | head -1 | cut -d: -f2)
        echo "✅ SUCCÈS - Total locations: $total"
    else
        echo "❌ ERREUR - Aucune donnée"
    fi
    echo ""
}

echo "LOCATION SERVICE (Port 9091)"
echo "============================="
test_tool "rechercher_locations" "http://localhost:9091/api/locations"
test_tool "rechercher_locations_disponibles" "http://localhost:9091/api/locations/available"
test_tool "rechercher_par_type APPARTEMENT" "http://localhost:9091/api/locations/type/APPARTEMENT"
test_tool "rechercher_par_type MAISON" "http://localhost:9091/api/locations/type/MAISON"
test_tool "rechercher_par_type VOITURE" "http://localhost:9091/api/locations/type/VOITURE"
test_tool "rechercher_disponibles_par_type APPARTEMENT" "http://localhost:9091/api/locations/available/APPARTEMENT"
test_tool "rechercher_disponibles_par_type MAISON" "http://localhost:9091/api/locations/available/MAISON"
test_tool "rechercher_disponibles_par_type VOITURE" "http://localhost:9091/api/locations/available/VOITURE"
test_tool "details_location 1" "http://localhost:9091/api/locations/1"

echo ""
echo "RESERVATION SERVICE (Port 9092)"
echo "================================"
test_tool "rechercher_reservations" "http://localhost:9092/api/reservations"
test_tool "rechercher_reservations_confirmees" "http://localhost:9092/api/reservations/statut/CONFIRMÉE"
test_tool "details_reservation 1" "http://localhost:9092/api/reservations/1"

echo ""
echo "AGENT IA SERVICE (Port 8081)"
echo "============================"
echo "▶️  Test Chat: Quels appartements sont disponibles ?"
response=$(curl -s -X POST http://localhost:8081/api/chat/test-session \
  -H "Content-Type: application/json" \
  -d '{"message":"Quels appartements sont disponibles ?"}' 2>&1)

if echo "$response" | grep -q "appartement\|disponible"; then
    echo "✅ SUCCÈS - Réponse reçue"
    echo "📝 Extrait: $(echo "$response" | head -c 200)..."
else
    echo "❌ ERREUR ou No response"
fi

echo ""
echo "=========================================="
echo "✅ TEST TERMINÉ"
echo "=========================================="
