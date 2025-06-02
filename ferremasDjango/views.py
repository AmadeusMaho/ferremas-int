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
    contexto = { "datos":productos[:6],
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
    tipo_usuario = request.session.get('tipo_usuario')
    try:
        response = requests.get(url)
        data = response.json()
        return render(request, 'producto.html', {'producto': data, 'tipo_usuario':tipo_usuario})
    except Exception as e:
        print(e)
        return None

def verUsuario(request):
    usuarioId = request.session.get('usuarioId', '') 
    usuario = request.session.get('usuario', '')
    clave = request.session.get('clave', '')
    tipo_usuario = request.session.get('tipo_usuario', '')
    email = request.session.get('email', '')

    direcciones = []

    if usuarioId:
        try:
            response = requests.get(f"http://localhost:8088/api/cliente/{usuarioId}/direcciones")
            if response.status_code == 200:
                direcciones = response.json()
        except Exception as e:
            print(f"Error al obtener direcciones para el usuario {usuarioId}: {e}")

    contexto = {
        "usuario": usuario,
        "tipo_usuario": tipo_usuario,
        "email": email,
        "usuarioId": usuarioId,
        "clave": clave,
        "direcciones": direcciones,
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
        tipo_usuario = request.session.get('tipo_usuario')
        
        # recibir precio final del producto del HTML
        if request.method == "POST":
            productoId = request.POST.get("productoId")
            precio = request.POST.get("amount")
            cantidad = request.POST.get('quantity', '1')
        #recibe el producto desde el json con .get
        request.session['productoId'] = productoId
        request.session['cantidad'] = cantidad
        print(cantidad)
        #genera la orden de compra
        buy_order = f"ORD{random.randint(100000, 999999)}"
        session_id = "SES123"
        amount = int(precio)
        return_url = "http://localhost:8000/pago/retorno/"

        #se crea la transacción y recibe la respuesta de la API
        response = tx.create(buy_order, session_id, amount, return_url)
        print(response)
        return render(request, 'redireccion.html', {'url': response['url'],
        'token': response['token'],'tipo_usuario':tipo_usuario})

def retorno_pago(request):
    try:
        tipo_usuario = request.session.get('tipo_usuario')
        #guardamos el token generado anteriormente
        token=request.GET.get('token_ws')
        #buscamos el producto por ID (guardado en session storage)
        productoId = int(request.session.get('productoId'))
        cantidad = int(request.session['cantidad'])
        producto = getProductoporID(productoId)
        # configura las credenciales nuevamente
        tx = Transaction(WebpayOptions(IntegrationCommerceCodes.WEBPAY_PLUS, IntegrationApiKeys.WEBPAY, IntegrationType.TEST))
        # confirma la transacción y recibe los resultados del token
        response = tx.commit(token)
        if response.get('response_code') == 0 and response.get('status') == 'AUTHORIZED':
            print("pasa a response.getauthorized")
            fechaTransaccion = response.get('transaction_date')
            if len(fechaTransaccion) >= 8:
                    fecha_formateada = f"{fechaTransaccion[:4]}-{fechaTransaccion[5:7]}-{fechaTransaccion[8:10]}"
            print(fecha_formateada)
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
            if responsePost.status_code in (200, 201):
                print("Venta realizada exitosamente")
                return render(request, 'retorno.html', {
                    'response': response,
                    'venta': responsePost.json(),
                    'tipo_usuario': tipo_usuario
                })
            else:
                print(f"Error en el POST: {responsePost.text}")
                return render(request, 'retorno.html', {
                    'error': f'Error al registrar la venta: {responsePost.text}',
                    'response': response,
                    'tipo_usuario': tipo_usuario
                })
    
        return render(request, 'retorno.html', {'response': response, 'tipo_usuario': tipo_usuario})
    except Exception as e:
        return render(request, 'retorno.html', {'error': str(e), 'tipo_usuario': tipo_usuario} )
    
def verVentas(request):
    urlVenta = "http://localhost:8088/api/venta"
    tipo_usuario = request.session.get('tipo_usuario')
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

    return render(request, 'listaventas.html', {'ventas': ventas, 'tipo_usuario':tipo_usuario})



def verDetalleId(request, id):
    urlDetalle = f"http://localhost:8088/api/venta/{id}/detalle"
    tipo_usuario = request.session.get('tipo_usuario')
    try:
        responseDetalle = requests.get(urlDetalle)
        responseDetalle.raise_for_status()
        detalle = responseDetalle.json()
    except Exception as e:
        print(f"Error: {e}")
        return render(request, 'listadetalle.html', {'detalle': [], 'error': str(e),'tipo_usuario':tipo_usuario})

    return render(request, 'listadetalle.html', {'detalles': detalle, 'tipo_usuario':tipo_usuario})


def verUsuarios(request):
    urlDetalle = f"http://localhost:8088/api/cliente"
    tipo_usuario = request.session.get('tipo_usuario')
    try:
        responseDetalle = requests.get(urlDetalle)
        responseDetalle.raise_for_status()
        usuarios = responseDetalle.json()  # <-- JSON es una lista
        print(usuarios)
    except Exception as e:
        print(f"Error: {e}")
        return render(request, 'listaUsuarios.html', {'usuarios': [], 'error': str(e),'tipo_usuario':tipo_usuario})

    return render(request, 'listaUsuarios.html', {'usuarios': usuarios, 'tipo_usuario':tipo_usuario})

def buscarProducto(request):
    busqueda = request.GET.get('q', '').strip().lower()
    tipo_usuario = request.session.get('tipo_usuario')
    productos = obtener_productos()
    if busqueda:
        #p itera sobre productos si existe la búsqueda, luego "in" implica que mientras haya un mínimo de coincidencia se añada al array
        productos = [p for p in productos if busqueda in p.get('nombre', '').lower()]
    contexto = {
        'datos': productos,
        'query': busqueda,
        'tipo_usuario': tipo_usuario
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



            #detalle_venta = {
            #'fecha': response.get('transaction_date') ,
            #'medio_pago' : response.get('payment_type_code'),
            #'producto_id': productoId,
            #'cantidad' : cantidad,
            #'precio_unit' : producto.get('precio')
        #}

def direcciones(request):
    usuario_id = request.session.get('usuarioId', '')
    if not usuario_id:
        return redirect('login')

    if request.method == 'POST':
        if 'eliminar' in request.POST:
            direccion_id = request.POST['eliminar']
            try:
                response = requests.delete(f"http://localhost:8088/api/direccion/{direccion_id}")
                if response.status_code != 200:
                    print(f"Error eliminando dirección {direccion_id}: {response.text}")
            except Exception as e:
                print(f"Error eliminando dirección {direccion_id}: {e}")
            return redirect('usuario')

        elif 'guardar' in request.POST:
            # Aquí va tu código para actualizar y agregar direcciones (como antes)
            for key in request.POST:
                if key.startswith('direccion_') and key != 'nueva_direccion':
                    direccion_id = int(key.split('_')[1])
                    nueva_descripcion = request.POST.get(key).strip()
                    if nueva_descripcion != '':
                        try:
                            response = requests.put(
                                f"http://localhost:8088/api/direccion/{direccion_id}",
                                json={
                                    "nombre": "",
                                    "descripcion": nueva_descripcion,
                                    "cliente": {"clienteId": int(usuario_id)}
                                }
                            )
                            if response.status_code != 200:
                                print(f"Error actualizando dirección {direccion_id}: {response.text}")
                        except Exception as e:
                            print(f"Error actualizando dirección {direccion_id}: {e}")

            nueva_direccion = request.POST.get('nueva_direccion', '').strip()
            if nueva_direccion != '':
                try:
                    response = requests.post(
                        "http://localhost:8088/api/direccion",
                        json={
                            "nombre": "",
                            "descripcion": nueva_direccion,
                            "cliente": {"clienteId": int(usuario_id)}
                        }
                    )
                    if response.status_code != 200:
                        print(f"Error agregando nueva dirección: {response.text}")
                except Exception as e:
                    print(f"Error agregando nueva dirección: {e}")

            return redirect('usuario')

    return redirect('usuario')

def eliminar_direccion(request, direccion_id):
    if request.method == 'POST':
        try:
            response = requests.delete(f"http://localhost:8088/api/direccion/{direccion_id}")
            if response.status_code != 200:
                print(f"Error eliminando dirección: código {response.status_code}")
        except Exception as e:
            print(f"Error eliminando dirección {direccion_id}: {e}")
    return redirect('usuario')
