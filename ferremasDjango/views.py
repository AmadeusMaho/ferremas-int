from django.shortcuts import render
import requests

# Create your views here.

def verIndex(request):
    productos = obtener_productos()
    contexto = { "datos":productos}
    print(contexto)
    return render(request, 'index.html', contexto)

def obtener_productos():
    url="http://localhost:8088/api/producto"
    try:
        response = requests.get(url)
        data = response.json()
        return data
    except Exception as e:
        return None
    
def obtenerProducto_ID(request, id):
    url="http://localhost:8088/api/producto/"+id
    try:
        response = requests.get(url)
        data = response.json()
        return render(request, 'producto.html', {'producto': data})
    except Exception as e:
        print(e)
        return None
    
    
    