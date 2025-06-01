"""
URL configuration for ferremas project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from ferremasDjango import views


urlpatterns = [
    path('admin/', admin.site.urls),
    path('',views.verIndex),
    path('login/', views.verLogin),
    path('template/', views.verTemplate),
 
    #transbank
    path('pago/iniciar/', views.realizar_pago, name='realizar_pago'),
    path('pago/retorno/', views.retorno_pago, name='retorno_pago'),
    path('', views.verIndex),
    path('index/', views.verIndex, name="index"),
    path('producto/<str:id>/', views.obtenerProducto_ID, name='producto'),
    path('login/', views.verLogin, name='login'),
    path('template/', views.verTemplate),
    path('usuario/', views.verUsuario, name="usuario"),
    path('update/', views.actualizarUsuario, name='actualizarUsuario'),
    path('registro/', views.verRegistro, name='registroUsuario'),
    path('logout/', views.logout, name='logout'),
    path('ventas/', views.verVentas, name='listaventas'),
    path('usuarios/', views.verUsuarios, name='verUsuarios'),
    path('detalle/<str:id>/', views.verDetalleId, name='listadetalles'),
    path('buscar/', views.buscarProducto, name='buscarProducto'),
    path('productos/', views.verProductosLista, name='verProductosLista')
    
]
