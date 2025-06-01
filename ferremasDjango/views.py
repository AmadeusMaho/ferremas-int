from django.shortcuts import redirect, render
import requests
from django.conf import settings
from transbank.webpay.webpay_plus.transaction import Transaction, IntegrationCommerceCodes, IntegrationApiKeys
from transbank.common.options import WebpayOptions
from transbank.common.integration_type import IntegrationType
import random

# Create your views here.

def verIndex(request):
    productos = obtener_productos()
    contexto = { "datos":productos}
    return render(request, 'index.html', contexto)

def verLogin(request):
    return render(request, 'login.html', {})

def verTemplate(request):
    return render(request, 'template.html', {})

def obtener_productos():
    #recibe todos los productos
    url="http://localhost:8088/api/producto"
    try:
        response = requests.get(url)
        data = response.json()
        return data
    except Exception as e:
        return None
    
def obtenerProducto_ID(request, id):
    #carga el producto a partir de la ID
    url="http://localhost:8088/api/producto/"+id
    try:
        response = requests.get(url)
        data = response.json()
        return render(request, 'producto.html', {'producto': data})
    except Exception as e:
        print(e)
        return None
    

def realizar_pago(request):
        #configurar credenciales de transbank
        tx = Transaction(WebpayOptions(IntegrationCommerceCodes.WEBPAY_PLUS, IntegrationApiKeys.WEBPAY, IntegrationType.TEST))
        # recibir precio final del producto del HTML
        if request.method == "POST":
            productoId = request.POST.get("producto_id")
            precio = request.POST.get("amount")

        #genera la orden de compra
        buy_order = f"ORD{random.randint(100000, 999999)}"
        session_id = "SES123"
        amount = int(precio) 
        return_url = "http://localhost:8000/pago/retorno/"

        #se crea la transacción y recibe la respuesta de la API
        response = tx.create(buy_order, session_id, amount, return_url)
        print(response)
        return render(request, 'redireccion.html', {'url': response['url'],
        'token': response['token']})

def retorno_pago(request):
    try:
        #guardamos el token generado anteriormente
        token=request.GET.get('token_ws')
        # configura las credenciales nuevamente
        tx = Transaction(WebpayOptions(IntegrationCommerceCodes.WEBPAY_PLUS, IntegrationApiKeys.WEBPAY, IntegrationType.TEST))
        # confirma la transacción y recibe los resultados del token
        response = tx.commit(token)
        return render(request, 'retorno.html', {'response': response})
    except Exception as e:
        return render(request, 'retorno.html', {'error': str(e)})
