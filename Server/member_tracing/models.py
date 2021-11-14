"""
참고 링크 목록
https://developers.google.cn/maps/documentation/places/android-sdk/reference/com/google/android/libraries/places/api/Places?hl=ko
https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial?hl=ko
https://www.django-rest-framework.org/api-guide/fields/#datetimefield
"""


from django.db import models

class MemberTracing(models.Model):
    created = models.DateTimeField(auto_now_add=True)                   # 기록된 일시
    name = models.CharField(max_length=100, blank=True, default='')     # 장소 이름
    address = models.CharField(max_length=100)                          # 장소 주소

    # 추가 예상
    # LatLng -> Class.. latitude, longitude
    #           참조 : https://developers.google.com/android/reference/com/google/android/gms/maps/model/LatLng.html?hl=ko
    # Attributions -> List<String>

    class Meta: # 일시순 정렬
        ordering = ['created']