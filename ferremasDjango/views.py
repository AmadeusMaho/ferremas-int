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
                'clave': clave
            })

            if response.status_code == 200:
                data = response.json()
                
                usuario=username
                
                request.session['jwt_token'] = data['token']
                request.session['tipo_usuario'] = data['tipo_usuario']
                request.session['usuario'] = username
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
    
    