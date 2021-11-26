from django.urls import path
from member_tracing import views

urlpatterns = [
    path('', views.member_tracing_list),
    path('<int:pk>/', views.member_tracing_detail),
]