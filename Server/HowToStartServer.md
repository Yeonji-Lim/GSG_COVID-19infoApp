How to Start Server (백엔드 과정 기록) - 2021.11.8 임연지 생성
2021.11.12 임연지 수정

# Server 파일 처음 받아오는 경우 시작 과정

my_settings.py를 Server로 이동

## 가상환경 설정
Server 폴더 아래에 devenv 라는 이름의 venv 생성 (하고 싶은 이름으로 생성)
~~~
$ python -m venv devenv	//window
$ python3 -m venv devenv	//mac
~~~

## 가상환경 활성화 
~~~
$ devenv\Scripts\activate     //window
$ source devenv/bin/activate  //mac
~~~

command라인 앞에 (devenv) 표시 확인

## 가상환경에 필요한 패키지 설치
~~~
(devenv) $ pip install -r requirements.txt
~~~
오류 'No matching distribution found for requirements.txt' 인 경우

명령어에서 -r를 입력하지 않았는지 확인하기

## MySQL 연동

~~~
(devenv) $ python manage.py inspectdb
(devenv) $ python manage.py makemigrations
(devenv) $ python manage.py migrate
~~~

확인 : my_settings.py에 DATABASE에 DB접근 정보로 접속

## 서버 실행
~~~
(devenv) $ python manage.py runserver
~~~

서버 종료 : 해당 터미널에서 ctrl+c

# ERROR
## NameError: name '_mysql' is not defined
DB 설정 후, 적용 시 발생하는 에러. 아래와 같은 명령 실행시 발생
~~~
$ python manage.py inspectdb
~~~

라이브러리 설치 및 경로 설정에 문제가 있어서 발생한 케이스

-> 심볼릭 링크를 생성하여 올바르게 라이브러리를 설치하면 해결

아래의 명령을 차례로 진행한다.

~~~
$ sudo ln -s /usr/local/mysql/lib /usr/local/mysql/lib/mysql

$ pip uninstall mysqlclient 
~~~

y/n 입력 나오면 y 선택

~~~
$ pip install mysqlclient

$ export DYLD_LIBRARY_PATH=/usr/local/mysql/lib/mysql
~~~

이후 원래의 명령을 진행하면 해결됨
~~~
$ python manage.py inspectdb
~~~

참고 : https://devday.tistory.com/entry/%EB%A7%A5-Mac%EC%97%90%EC%84%9C-%ED%8C%8C%EC%9D%B4%EC%8D%AC-Python-import-MySQLdb-%EC%8B%9C-image-not-found-%EC%97%90%EB%9F%AC



