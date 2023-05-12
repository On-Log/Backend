# Nanal Backend

### 프레임워크 선정 이유

| Framework| 선정이유 |
|:------------------------------------------------------------------------------:| :--: |
| Spring  | 특정 환경이나 서버, 기술에 종속되지 않으며 유연한 애플리케이션을 개발|
|AWS EC2|free tier로 제공되는 EC2가 프로토 타입 개발에 있어 적합. 높은 안정성 및 확장성으로 추후 데이터 처리량 증가에 유연하게 대처 가능|
|Github Actions|배포 자동화를 위한 도구 Github를 협업툴로 사용하고 있는 상황에서 Github Action을 사용하는 것이 관리 차원에서 유리하다고 판단|
|Docker |독립된 개발 환경을 보장하고 개발/운영 환경을 통합|
|mariaDB|해당 애플리케이션이 다루는 구조 특성상, RDB가 적합하다고 판단. Mysql과 다르게 영리목적으로 사용시에도 비용이 청구되지 않음|
|Spring Data JPA |  간단한 설정을 통해 필수 로직들을 쿼리 작성 없이 사용 가능하기에, 개발 생산성 증가|
|Redis|key, value 형태로 데이터 저장 및 해당 데이터의 유효기간 설정을 통한 토큰 관리 용이|

## System Architecture
![시스템아키텍처 drawio (3)](https://user-images.githubusercontent.com/78543382/230613220-8840ac69-055b-491d-bbc8-7ef63e23c9b6.png)


## CI/CD PROCESS
![CI_CD 아키텍처 drawio (4)](https://user-images.githubusercontent.com/78543382/231061184-caca43e3-3685-4438-a141-4bf40b0d53a8.png)

## ERD
![화면 캡처 2023-04-07 220401](https://user-images.githubusercontent.com/78543382/230613585-e3d47be2-fefa-4d65-b1a9-3e7987dbfcef.png)

## 커밋 컨벤션

### 커밋 제목
- 최소한의 기능을 기준으로 issue를 생성
- 이슈 이름은 `[커밋 타입] 기능 설명` 으로 통일
  ex) [feat] 검색 뷰 통신 연결
- 이슈 템플릿을 활용해 작업 설명과 진행 상황을 작성
- 진행 상황은 ☑️(todo)로 작성한다.

### 커밋 메세지
- feat: 새로운 기능 구현
- fix: 오류, 버그 해결
- add: feat 이외의 부수적인 코드 추가, 라이브러리 추가
- docs: README나 WIKI 같은 문서 개정
- chore: 간단한 코드 수정, 내부 파일 수정
- rename: 파일 이름 변경이 있을 때 사용
- del: 쓸모없는 코드 삭제
- style: 코드 스타일 혹은 포맷 등에 관한 커밋
- refact:  코드 리팩토링에 대한 커밋
- test : 테스트 코드 수정에 대한 커밋

