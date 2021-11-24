from django.db import models

class MemberTracing(models.Model):
    """
    업데이트 된 사용자 정보
    {
    "date": "Nov 23, 2021 11:17:44 AM",
    "id": "f9a7ec5b-c098-4ca0-9347-cbb9f9b8b018",
    "latitude": 34.793605,
    "longitude": 126.8735283
    }
    """

    date = models.DateTimeField(auto_now_add=False)
    id = models.CharField(max_length=100, primary_key=True, unique=True)
    latitude = models.FloatField(null=True, default='0')
    longitude = models.FloatField(null=True, default='0')

    class Meta: # 일시순 정렬
        ordering = ['date']