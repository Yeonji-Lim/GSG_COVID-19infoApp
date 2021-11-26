# url에 따라 안내된 request들을 처리하기 위한 파일\

from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from rest_framework.permissions import AllowAny # 1. 특정 permission을 import 한 후

@api_view(['GET']) # 어떤 request 함수가 사용될 지 결정
# 이 함수에 적용될 permission을 적용할 수 있음!
# @permission_classes([AllowAny]) # 모든 사람에게 허용하는 함수로 설정
def hello_world(request):
    print(request.query_params) # 링크로 들 오언 정보 출력하기
    print(request.query_params['id']) # 특정 키를 가진 값 출력

    # 프론트로부터 숫자 받아와 2 곱하여 넘겨주기
    number = request.query_params['num']
    new_number = int(number)*2
    return Response({"message": "Hello, world!", 'result':new_number})