# GSG_COVID-19infoApp
2021-2 소프트웨어공학개론 공쏘공 팀 - 전염병 확산 방지 프로그램

## 목차

- [목적 및 용도](#목적-및-용도)
- [기능](#기능)
- [이용방법](#이용방법)
  * [0. 보안 파일 요청](#0.-보안-파일-요청)
  * [1. 서버 가동](#1.-서버-가동)
  * [2. 앱 설치](#2.-앱-설치)
- [현재 격리자 정보, 격리 해제자 정보제공 중단](#현재-격리자-정보,-격리-해제자-정보제공-중단)


## 목적 및 용도
기존 코로나 확진자 동선 제공 프로그램의 경우, 

지도에 확진자 동선만 보여주기 때문에 사용자에게 근접한 동선을 보기 힘들다.

우리 프로젝트는 사용자 동선과 확진자 동선을 동시에 한 지도에서 같이 볼 수 있게 해서 

사용자가 원하는 정보를 한눈에 빠르게 습득할 수 있게 하였다.

또한 확진자 통계와 사용자 통계도 제공한다.

## 기능
1. 코로나 동선 조회

<img src="https://user-images.githubusercontent.com/48210134/144595603-cacf5655-d320-4542-b652-ee3bc68c5da6.png" width="25%">   <img src="https://user-images.githubusercontent.com/48210134/144595672-9e7b0c04-7537-4f22-919b-6298a59fd874.png" width="25%">

2. 사용자 동선 기록

<img src="https://user-images.githubusercontent.com/48210134/144595660-01bb1a93-b081-4800-943c-f1c27b5999ad.png" width="25%">   <img src="https://user-images.githubusercontent.com/48210134/144595671-b969052f-6169-4150-9fac-faade7233ada.png" width="25%">


3. 코로나 통계 조회

<img src="https://user-images.githubusercontent.com/48210134/144595676-58b337ec-1cc0-4ebc-abf9-d21ab1e2fabe.png" width="25%">   <img src="https://user-images.githubusercontent.com/48210134/144595679-57883ce6-3e0f-4472-84d2-4483bf406499.png" width="25%">

<img src="https://user-images.githubusercontent.com/48210134/144595654-e48d4f38-8e59-4496-b655-0314d8e06711.png" width="25%">   <img src="https://user-images.githubusercontent.com/48210134/144595681-c210e3bd-a3c1-4af0-a551-26ec2a91755b.png" width="25%">   <img src="https://user-images.githubusercontent.com/48210134/144595682-e56e7603-0e39-46a2-9a8b-afb6f0eb5593.png" width="25%">

## 이용방법
### 0. 보안 파일 요청
my_settings.py, secrets.json이 Server 파일 하위에 있어야 함
### 1. 서버 가동
**A. 가상환경**

Server 폴더 아래에 devenv 라는 이름의 venv 생성
~~~
$ cd Server/
~~~
~~~
$ python -m venv devenv	        //window
$ python3 -m venv devenv	//mac
~~~
가상환경 활성화
~~~
$ devenv\Scripts\activate     //window
$ source devenv/bin/activate  //mac
~~~

패키지 설치
~~~
$ pip install -r requirements.txt
~~~

**B. secrets.json 설정**

인증 이메일을 보낼 계정 정보를 입력

**C. DB 연동**

MySQL 계정 설정 후 schema 하나 생성

my_settings.py에 해당 정보 입력

다음 명령을 차례로 실행
~~~
$ python manage.py inspectdb
$ python manage.py makemigrations
$ python manage.py migrate
~~~
MySQL에서 확인 : 
~~~
mysql> show tables;
~~~

**에러가 발생하는 경우 (NameError: name '_mysql' is not defined)**

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

$ pip install mysqlclient

$ export DYLD_LIBRARY_PATH=/usr/local/mysql/lib/mysql
~~~

이후 원래의 명령을 진행하면 해결됨
~~~
$ python manage.py inspectdb
~~~

**D. 서버 구동**

로컬에서 실행
~~~
$ python manage.py runserver
~~~

내부ip 사용
~~~
$ python manage.py runserver 내부ip:포트
~~~

### 2. 앱 설치

---
## 현재 격리자 정보, 격리 해제자 정보제공 중단

![Screenshot 2021-12-03 202202](https://user-images.githubusercontent.com/48210134/144595659-e7972547-f333-4088-bffe-8c6341bd373e.png)
---
