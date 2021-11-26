from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
from member_tracing import views

urlpatterns = [
    path('', views.member_tracing_list),
    path('<int:pk>/', views.member_tracing_detail),
]