from django.http import JsonResponse
from django.shortcuts import redirect, render
import requests

# Create your views here.

def verIndex(request):
    productos = obtener_productos()
    contexto = { "datos":productos}
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

def verRegistro(request):
    if request.method == 'POST':
        datos_registro = {
            'nombre': request.POST.get('nombre'),
            'apellido': request.POST.get('apellido'),
            'run': request.POST.get('run'),
            'dv': request.POST.get('dv'),
            'username': request.POST.get('username'),
            'email': request.POST.get('email'),
            'telefono': request.POST.get('telefono'),
        }
        try:
            response = requests.post('http://localhost:8088/api/cliente', json=datos_registro)
            if response.status_code in (200, 201):
                datos_usuario = {
                    'username': request.POST.get('username'),
                    'clave': request.POST.get('clave'),
                    'email': request.POST.get('email'),
                    'tipoUsuario': 2 # cliente = 2
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



    
    