"""
참고 링크 목록
https://developers.google.cn/maps/documentation/places/android-sdk/reference/com/google/android/libraries/places/api/Places?hl=ko
https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial?hl=ko
https://www.django-rest-framework.org/api-guide/fields/#datetimefield
"""


from django.db import models

class MemberTracing(models.Model):
    """
    업데이트 된 사용자 정보
    {
        "key":""
        "latitude":""
        "longitude":""
        "date":""
    }
    """

    latitude = models.FloatField(null=True, default='0')
    longitude = models.FloatField(null=True, default='0')
    date = models.DateTimeField(auto_now_add=False)

    class Meta: # 일시순 정렬
        ordering = ['date']