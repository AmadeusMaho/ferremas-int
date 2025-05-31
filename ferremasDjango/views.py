from django.shortcuts import render

# Create your views here.

def verIndex(request):
    return render(request, 'index.html', {})