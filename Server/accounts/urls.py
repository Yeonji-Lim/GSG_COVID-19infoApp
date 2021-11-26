# 앱으로 전달되는 url 처리를 위한 파일

from django.conf.urls import url
from .views import hello_world


urlpatterns = [
    url('hello_world', hello_world),
]