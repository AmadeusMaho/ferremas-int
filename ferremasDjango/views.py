from django.shortcuts import render
import requests

# Create your views here.

def verIndex(request):
    productos = obtener_productos()
    contexto = { "datos":productos}
    print(contexto)
    return render(request, 'index.html', contexto)

def verLogin(request):
    return render(request, 'login.html', {})

def obtener_productos():
    url="http://localhost:8088/api/producto"
    try:
        response = requests.get(url)
        data = response.json()
        return data
    except Exception as e:
        return None
    
    