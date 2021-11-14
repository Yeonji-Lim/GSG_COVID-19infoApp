from rest_framework import serializers
from .models import MemberTracing

class MemberTracingSerializer(serializers.ModelSerializer):
    class Meta:
        # 모델 설정
        model = MemberTracing

        # 필드 설정
        fields = ('id', 'name', 'address')
