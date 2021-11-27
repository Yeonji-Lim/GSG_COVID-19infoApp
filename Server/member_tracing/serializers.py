from rest_framework import serializers
from .models import MemberTracing

class MemberTracingListSerializer(serializers.ListSerializer):
    def create(self, validated_data):
        memberTracing = [MemberTracing(**item) for item in validated_data]
        return MemberTracing.objects.bulk_create(memberTracing)

class MemberTracingSerializer(serializers.ModelSerializer):
    class Meta:
        listSerializerClass = MemberTracingListSerializer

        # 모델 설정
        model = MemberTracing
        owner = serializers.ReadOnlyField(source='owner.key')

        # 필드 설정
        fields = ('owner', 'date', 'id', 'latitude', 'longitude')
