from django.http import JsonResponse
from django.shortcuts import redirect, redirect, render
import requests
from django.conf import settings
from transbank.webpay.webpay_plus.transaction import Transaction, IntegrationCommerceCodes, IntegrationApiKeys
from transbank.common.options import WebpayOptions
from transbank.common.integration_type import IntegrationType
import random

# Create your views here.

def verIndex(request):
    productos = obtener_productos()
    tipo_usuario = request.session.get('tipo_usuario')
    try:
        tipo_usuario_int = int(tipo_usuario)
    except (TypeError, ValueError):
        tipo_usuario_int = -1
    contexto = { "datos":productos,
                 "tipo_usuario": tipo_usuario_int}
    print(contexto)
    return render(request, 'index.html', contexto)

def verLogin(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        clave = request.POST.get('clave')

        try:
            response = requests.post('http://localhost:8088/api/auth/login', json={
                'username': username,
                'clave': clave,
            })

            if response.status_code == 200:
                data = response.json()
                
                request.session['jwt_token'] = data['token']
                request.session['usuarioId'] = data['usuarioId']
                request.session['usuario'] = data['username']
                request.session['clave'] = data['clave']
                request.session['email'] = data['email']
                request.session['tipo_usuario'] = data['tipo_usuario']
                return redirect('index')
            else:
                error = "Credenciales invalidas"
        except Exception as e:
            error = str(e)

        return render(request, 'login.html', {'error': error})
    return render(request, 'login.html')

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

def login_spring_boot(usuario, clave):
    response = requests.post('http://localhost:8088/api/auth/login', json={
        'username': usuario,
        'clave': clave
    })

    if response.status_code == 200:
        data = response.json()
        token = data['token']
        tipo_usuario = data['tipo_usuario']
        return token, tipo_usuario
    else:
        raise Exception('Login fallado: ' + response.text)
    
def get_data(request):
    token = request.session.get('jwt_token')
    headers = {'Authorization': f'Bearer {token}'}
    response = requests.get('http://localhost:8088/api/protected', headers=headers)
    if response.status_code == 200:
        return JsonResponse(response.json())
    else:
        return JsonResponse({'error': 'Unauthorized'}, status=401)
    
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

def verUsuario(request):
    usuarioId = request.session.get('usuarioId', '') 
    usuario = request.session.get('usuario', '')
    clave = request.session.get('clave', '')
    tipo_usuario = request.session.get('tipo_usuario', '')
    email = request.session.get('email', '')
    contexto = {
        "usuario": usuario,
        "tipo_usuario": tipo_usuario,
        "email": email,
        "usuarioId": usuarioId,
        "clave": clave
    }
    return render(request, 'usuario.html', contexto)

def actualizarUsuario(request):
    if request.method == 'POST' and request.POST.get('_method') == 'PUT':
        username = request.POST.get('username')
        email = request.POST.get('email')
        clave = request.POST.get('clave')
        usuarioId = request.POST.get('usuarioId')
        tipo_usuario = request.session.get('tipo_usuario', '')
        datos_actualizados = {
            'username': username,
            'clave': clave,
            'email': email,
            'tipo_usuario': tipo_usuario
        }
        try:
            response = requests.put(f'http://localhost:8088/api/usuario/{usuarioId}', json=datos_actualizados)

            if response.status_code == 200:
                request.session['usuario'] = username
                request.session['clave'] = clave
                request.session['email'] = email
                return redirect('usuario')
            else:
                error = f"No se pudo actualizar el usuario: {response.text}"
        except Exception as e:
            error = str(e)
        
        return render(request, 'usuario.html', {
            'error': error,
            'username': username,
            'email': email,
            'tipo_usuario': tipo_usuario,
            'usuarioId': usuarioId,
        })
    return redirect('usuario')

import re
import requests
from django.shortcuts import render, redirect

def verRegistro(request):
    if request.method == 'POST':
        nombre = request.POST.get('nombre', '').strip()
        apellido = request.POST.get('apellido', '').strip()
        run = request.POST.get('run', '').strip()
        dv = request.POST.get('dv', '').strip().upper()
        username = request.POST.get('username', '').strip()
        email = request.POST.get('email', '').strip()
        telefono = request.POST.get('telefono', '').strip()
        clave = request.POST.get('clave', '')
        confirmar_clave = request.POST.get('confirmar_clave', '')

        error = None

        if not nombre:
            error = "El campo Nombre es obligatorio."
        elif not apellido:
            error = "El campo Apellido es obligatorio."
        elif not run:
            error = "El campo RUN es obligatorio."
        elif not dv:
            error = "El campo Dígito Verificador es obligatorio."
        elif not username:
            error = "El campo Usuario es obligatorio."
        elif not email:
            error = "El campo Email es obligatorio."
        elif not telefono:
            error = "El campo Teléfono es obligatorio."
        elif not clave:
            error = "El campo Contraseña es obligatorio."
        elif not confirmar_clave:
            error = "Debe confirmar la contraseña."
        
        elif not re.fullmatch(r'[A-Za-z]{1,100}', nombre):
            error = "Nombre debe tener solo letras (máx 100 caracteres)."
        elif not re.fullmatch(r'[A-Za-z]{1,100}', apellido):
            error = "Apellido debe tener solo letras (máx 100 caracteres)."
        elif not re.fullmatch(r'\d{8}', run):
            error = "RUN debe tener exactamente 8 números."
        elif not re.fullmatch(r'[0-9Kk]', dv):
            error = "Dígito verificador debe ser un número o 'K'."
        elif len(username) > 50:
            error = "Usuario debe tener hasta 50 caracteres."
        elif len(email) > 100 or not re.fullmatch(r'[^@]+@[^@]+\.[^@]+', email):
            error = "Email inválido o demasiado largo (máx 100 caracteres)."
        elif not re.fullmatch(r'\+56\d{7,10}', telefono):
            error = "Teléfono debe comenzar con +56 y tener hasta 10 números."
        elif len(clave) < 8 or len(clave) > 50:
            error = "Contraseña debe tener entre 8 y 50 caracteres."
        elif clave != confirmar_clave:
            error = "Las contraseñas no coinciden."

        if error:
            form_data = {
                'nombre': nombre,
                'apellido': apellido,
                'run': run,
                'dv': dv,
                'username': username,
                'email': email,
                'telefono': telefono,
            }
            return render(request, 'registro.html', {'error': error, 'form_data': form_data})

        datos_registro = {
            'nombre': nombre,
            'apellido': apellido,
            'run': run,
            'dv': dv,
            'username': username,
            'email': email,
            'telefono': telefono,
        }

        try:
            response = requests.post('http://localhost:8088/api/cliente', json=datos_registro)
            if response.status_code in (200, 201):
                datos_usuario = {
                    'username': username,
                    'clave': clave,
                    'email': email,
                    'tipoUsuario': 2  # cliente = 2
                }
                response_usuario = requests.post('http://localhost:8088/api/usuario', json=datos_usuario)
                if response_usuario.status_code in (200, 201):
                    return redirect('login')
                else:
                    error = f"No se pudo registrar el usuario: {response_usuario.text}"
            else:
                error = f"No se pudo registrar el cliente: {response.text}"
        except Exception as e:
            error = str(e)

        return render(request, 'registro.html', {'error': error})

    return render(request, 'registro.html')

def logout(request):

    request.session.flush()
    return redirect('login')


#devuelve el producto como json
def getProductoporID(producto_id):
    url = f"http://localhost:8088/api/producto/{producto_id}"
    try:
        response = requests.get(url)
        response.raise_for_status() 
        return response.json()
    except Exception as e:
        print(f"Error: {e}")
        return None


def realizar_pago(request):
        #configurar credenciales de transbank
        tx = Transaction(WebpayOptions(IntegrationCommerceCodes.WEBPAY_PLUS, IntegrationApiKeys.WEBPAY, IntegrationType.TEST))
        # recibir precio final del producto del HTML
        if request.method == "POST":
            productoId = request.POST.get("productoId")
            precio = request.POST.get("amount")
            cantidad = request.POST.get("quantity")
        #recibe el producto desde el json con .get
        request.session['productoId'] = productoId
        request.session['cantidad'] = cantidad
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
        #buscamos el producto por ID (guardado en session storage)
        productoId = request.session.get('productoId')
        cantidad = 1
        producto = getProductoporID(productoId)
        
        # configura las credenciales nuevamente
        tx = Transaction(WebpayOptions(IntegrationCommerceCodes.WEBPAY_PLUS, IntegrationApiKeys.WEBPAY, IntegrationType.TEST))
        # confirma la transacción y recibe los resultados del token
        response = tx.commit(token)
        if response.get('response_code') == 0 and response.get('status') == 'AUTHORIZED':
            
             #si la respuesta es correcta se genera una venta en la bdd
            venta = {
                'fecha': response.get('transaction_date'),
                'cliente': {
                    'clienteId': int(request.session.get('usuarioId', ''))
                },
                'medioPago': response.get('payment_type_code'),  # <-- camelCase exacto
                'detallesVentas': [],
                'despachos': []
            }

            detalle_venta = {
                'cantidad': cantidad,
                'precioUnit': producto.get('precio'),
                'producto': {
                    'productoId': productoId
                }
            }
            venta['detallesVentas'].append(detalle_venta)
            responsePost = requests.post('http://localhost:8088/api/venta', json=venta)
            try:
                if responsePost.status_code in (200, 201):
                    print("venta realizada")
                else:
                    print("error en el post")
            except Exception as e:
                print(e)
        return render(request, 'retorno.html', {'response': response})
    except Exception as e:
        return render(request, 'retorno.html', {'error': str(e)})
    
def verVentas(request):
    urlVenta = "http://localhost:8088/api/venta"

    try:
        responseVentas = requests.get(urlVenta)
        responseVentas.raise_for_status()
        ventas = responseVentas.json()

        for venta in ventas:
            cliente = venta.get('cliente', {})
            venta['nombreCliente'] = cliente.get('nombre', '') + ' ' + cliente.get('apellido', '')
            venta['clienteId'] = cliente.get('clienteId', None)

            venta_total = 0
            for detalle in venta.get('detallesVentas', []):
                venta_total += detalle['precioUnit'] * detalle['cantidad']
            venta['total'] = venta_total

    except Exception as e:
        print(f"Error: {e}")
        return render(request, 'listaventas.html', {'ventas': [], 'error': str(e)})

    return render(request, 'listaventas.html', {'ventas': ventas})

def verDetalleId(request, id):
    urlDetalle = f"http://localhost:8088/api/venta/{id}/detalle"
    try:
        responseDetalle = requests.get(urlDetalle)
        responseDetalle.raise_for_status()
        detalles = responseDetalle.json()
    except Exception as e:
        print(f"Error: {e}")
        return render(request, 'listadetalle.html', {'detalles': [], 'error': str(e)})

    return render(request, 'listadetalle.html', {'detalles': detalles})




def buscarProducto(request):
    busqueda = request.GET.get('q', '').strip().lower()
    #tipo_usuario = request.session.get('tipo_usuario')
    productos = obtener_productos()
    if busqueda:
        #p itera sobre productos si existe la búsqueda, luego "in" implica que mientras haya un mínimo de coincidencia se añada al array
        productos = [p for p in productos if busqueda in p.get('nombre', '').lower()]
    contexto = {
        'datos': productos,
        'query': busqueda 
    }
    return render(request, 'listaProductos.html', contexto)

def verProductosLista(request):
    productos = obtener_productos()
    tipo_usuario = request.session.get('tipo_usuario')
    try:
        tipo_usuario_int = int(tipo_usuario)
    except (TypeError, ValueError):
        tipo_usuario_int = -1
    contexto = { "datos":productos,
                 "tipo_usuario": tipo_usuario_int}
    print(contexto)
    return render(request, 'listaProductos.html', contexto)