# Travel Tracker

## 프로젝트 개요

Travel Tracker는 동행자와 여행 경비를 계획하고 정리할 수 있는 서비스입니다.

### 주요 기능

> 화면 기획서 및 자세한 기능 설명은 [WIKI](https://github.com/f-lab-edu/travel-tracker/wiki/%EA%B8%B0%ED%9A%8D%EC%84%9C)에서 확인할 수 있습니다.

- **회원가입 및 로그인:** 이메일 인증을 통한 일반 회원가입 및 Google, Naver 간편 로그인 지원
- **여행 일정 생성:** 여행 일정을 등록하고, 화폐 및 예산을 관리하며, 여행 멤버를 초대/관리
- **여행 기록:** 개인/공동 경비 기록, 항목별 카테고리 지정, 메모/사진/위치/별점 등의 정보 기록, 필터링 기능 제공
- **정산 및 데이터 시각화:** 그룹 멤버 간 정산 금액 자동 계산, 총 지출 및 카테고리별 지출 비율 제공

### 프로젝트 구조

## 기술 스택

## ERD

![초기 ERD](https://github.com/user-attachments/assets/18ef824b-de38-4c1e-a823-7d1a29f3d6f8)

## 로컬 실행 방법

1. 환경변수 설정   
   `.env` 파일을 프로젝트 루트 경로에 생성하고 아래 내용을 추가하세요.

```
MYSQL_URL=localhost
MYSQL_PORT=
MYSQL_ROOT_PASSWORD=
MYSQL_DATABASE=
MYSQL_USER=
MYSQL_PASSWORD=
```

2. MySQL 컨테이너 실행

```shell
docker-compose up -d 
```
