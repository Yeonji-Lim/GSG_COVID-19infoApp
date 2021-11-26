from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.decorators import permission_classes
from rest_framework.parsers import JSONParser
from rest_framework.permissions import IsAuthenticated

from member_tracing.models import MemberTracing
from member_tracing.serializers import MemberTracingSerializer
import datetime


from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response

# 모든 사용자 동선 조회 or 새로운 동선 저장
@api_view(['GET', 'POST'])
# @permission_classes([IsAuthenticated])
def member_tracing_list(request):

    # 모든 사용자 동선 조회
    if request.method == 'GET':
        memberTracing = MemberTracing.objects.all()
        serializer = MemberTracingSerializer(memberTracing, many=True)
        return JsonResponse(serializer.data, safe=False)

    # 새로운 동선 저장
    elif request.method == 'POST':
        data = JSONParser().parse(request)
        if isinstance(data, dict):
            data = [data]
        for item in data:
            dateString = item['date']
            datetimeFormat = '%b %d, %Y %I:%M:%S %p'
            item['date'] = datetime.datetime.strptime(dateString, datetimeFormat)
            item['date'] = item['date'].strftime("%Y-%m-%d %H:%M:%S")
        serializer = MemberTracingSerializer(data=data, many=True)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201, safe=False)
        return JsonResponse(serializer.errors, status=400, safe=False)

# 사용자 동선을 검색, 업데이트, 삭제
@api_view(['GET', 'PUT', 'DELETE'])
def member_tracing_detail(request, pk):

    # 해당 사용자 동선을 받아옴
    try:
        memberTracing = MemberTracing.objects.get(pk=pk)
    except MemberTracing.DoesNotExist:
        return HttpResponse(status=404)

    # 조회
    if request.method == 'GET':
        serializer = MemberTracingSerializer(memberTracing)
        return JsonResponse(serializer.data)

    # 업데이트
    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        dateString = data['date']
        datetimeFormat = '%b %d, %Y %I:%M:%S %p'
        data['date'] = datetime.datetime.strptime(dateString, datetimeFormat)
        data['date'] = data['date'].strftime("%Y-%m-%d %H:%M:%S")
        serializer = MemberTracingSerializer(memberTracing, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data)
        return JsonResponse(serializer.errors, status=400)

    # 삭제 (사용 안할 수 있음)
    elif request.method == 'DELETE':
        memberTracing.delete()
        return HttpResponse(status=204)