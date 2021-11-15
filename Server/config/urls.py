"""config URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.2/topics/http/urls/
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
from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),
    # rest_framework
    # url path는 변경하여 사용 가능
    path('api-auth/', include('rest_framework.urls')),
    
    # django-rest-auth 기능 사용을 위한 url 추가
    path('rest-auth/', include('rest_auth.urls')),

    # django-rest-auth의 회원 가입 기능 사용을 위한 url 추가
    path('rest-auth/registration/', include('rest_auth.registration.urls')),
]
