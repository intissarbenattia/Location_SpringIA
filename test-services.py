#!/usr/bin/env python3
"""
Script pour tester les services SmartRent
"""

import requests
import json
import sys
from datetime import datetime

# Configuration des services
SERVICES = {
    'Discovery (Eureka)': 'http://localhost:8761/eureka/apps',
    'Auth Service': 'http://localhost:8080/api/auth/health',
    'Location Service': 'http://localhost:9091/api/locations',
    'Reservation Service': 'http://localhost:9092/api/reservations',
    'Agent IA Service': 'http://localhost:8081/api/agent/health',
    'Gateway Service': 'http://localhost:8888',
}

def test_service(name, url, method='GET', data=None):
    """Test un service en faisant une requête HTTP"""
    try:
        if method == 'POST':
            response = requests.post(url, json=data, timeout=5)
        else:
            response = requests.get(url, timeout=5)
        
        status = '✓ OK' if response.status_code < 400 else f'✗ HTTP {response.status_code}'
        print(f'{name:30} {status}')
        return response.status_code < 400
    except requests.exceptions.ConnectionError:
        print(f'{name:30} ✗ NOT REACHABLE')
        return False
    except Exception as e:
        print(f'{name:30} ✗ ERROR: {str(e)[:30]}')
        return False

def test_gateway_routing():
    """Test le routage du Gateway vers Agent IA"""
    try:
        response = requests.post(
            'http://localhost:8888/api/agent/chat',
            json={'message': 'test'},
            timeout=5
        )
        if response.status_code == 200:
            print(f'{"Gateway Routing (Agent IA)":30} ✓ OK')
            return True
        else:
            print(f'{"Gateway Routing (Agent IA)":30} ✗ HTTP {response.status_code}')
            return False
    except requests.exceptions.ConnectionError:
        print(f'{"Gateway Routing (Agent IA)":30} ✗ NOT REACHABLE')
        return False
    except Exception as e:
        print(f'{"Gateway Routing (Agent IA)":30} ✗ ERROR: {str(e)[:30]}')
        return False

def main():
    print()
    print('=' * 60)
    print('SmartRent Services Health Check')
    print('=' * 60)
    print(f'Time: {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}')
    print()
    
    results = []
    
    # Tester les services
    for name, url in SERVICES.items():
        results.append(test_service(name, url))
    
    print()
    
    # Tester le routage du Gateway
    test_gateway_routing()
    
    print()
    print('=' * 60)
    passed = sum(results)
    total = len(results)
    print(f'Summary: {passed}/{total} services are reachable')
    print('=' * 60)
    print()
    
    return 0 if passed == total else 1

if __name__ == '__main__':
    sys.exit(main())
