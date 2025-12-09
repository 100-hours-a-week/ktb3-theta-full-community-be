# INFIO Community (Backend)

<table>
  <tr>
    <td valign="top" width="200">
      <img src="https://github.com/user-attachments/assets/0571a8fb-27bd-4b3a-8b5b-6df4a7542bc8" alt="Infio Logo" width="100%" />
    </td>
    <td valign="middle">
      미니멀리즘적인 UI/UX를 통해 빠르고 정확하게 <b>정보를 공유하는 웹 커뮤니티 서비스</b>입니다. <br/> 
      불필요한 복잡함은 전부 덜어내고 정보(Information)와 관심사에만 집중할 수 있어요.
    </td>
  </tr>
</table>

## 개요
Java, Spring Boot 기반 REST API 서버

- 개발 기간 : 2025.10. - 2025. 12. (2개월)
- 개발 인원 및 담당 역할 : 1인 (기획, 디자인, 개발)

### 프로젝트 구조
```
ktb3-theta-full-community-be/
  ├── build.gradle, settings.gradle
  ├── gradle/ , gradlew*                       # Gradle Wrapper
  ├── upload/                                  # 샘플 업로드 파일 (articles, profiles)
  ├── src/
  │   ├── main/
  │   │   ├── java/ktb/week4/community/
  │   │   │   ├── domain/
  │   │   │   │   ├── article | comment | like | user   # controller/service/repository/entity/dto/validator/
  loader/policy
  │   │   │   ├── security/                            # JWT, principal, filter, handler 등 보안 구성
  │   │   │   └── global/                              # 공통 config, exception, apiPayload, converter,
  resolver, util, file
  │   │   └── resources/
  │   │       ├── application.yml                      # DB/로그/업로드 경로 설정
  │   │       ├── static/                              # 정적 리소스
  │   └── test/java/ktb/week4/community/               # domain별 단위/통합 테스트
  │       ├── domain/*/entity                          # 엔티티 단위 테스트
  │       └── auth | file | comment | like | user | article
  └── build/                                           # 생성물(클래스, 리소스, 리포트)
```

### ERD
<img width="710" height="442" alt="KTB_community" src="https://github.com/user-attachments/assets/aa9acfb6-d3af-449d-b615-66432a41e778" />

## 기술 스택
<img src="https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"> <img src="https://img.shields.io/badge/Spring_Boot_3.5.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/Lombok-BC0230?style=for-the-badge&logo=lombok&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/P6Spy-000000?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"> <img src="https://img.shields.io/badge/Swagger_(OpenAPI)-85EA2D?style=for-the-badge&logo=swagger&logoColor=black">

## 기술적 도전

### 🗞️ SRP 기반 리팩토링

**문제 상황**

Controller에 Swagger 어노테이션과 controller 로직이 섞여있음
Service에 검증 로직이 중복되어 코드 가독성이 떨어짐

**해결 방안**

- 인터페이스 분리
	- Swagger 관련 설정과 Validation 어노테이션을 모두 `ApiSpecification` 인터페이스로 이동시키고, 구현체(Controller)에서는 순수 로직만 남김.
- Service 계층화
	- 비즈니스 로직과 검증 로직이 섞이는 것을 막기 위해 `Loader`(조회), `Validator`(검증), `Policy`(정책) 클래스로 역할을 명확히 분리.

**성과**

- API 명세 변경 시 비즈니스 로직을 건드리지 않아도 되는 유지보수성 확보
- Controller와 Service 클래스의 코드 라인 수 감소 및 응집도 향상

### 🗂️ JPA N+1 문제 해결 및 기본값(Default Value) 처리

**문제 상황**
1.  게시글 목록 조회 시 작성자 정보를 가져오기 위해 게시글 개수(N)만큼 `SELECT * FROM user` 쿼리가 추가 발생하는 N+1 문제 발생
2. 엔티티에 `@ColumnDefault`를 설정했음에도 `Hibernate`가 `null` 값을 명시적으로 삽입하여 DB 기본값이 적용되지 않는 문제 발생

**해결 방안**
- Fetch Join 적용
	- `@EntityGraph(attributePaths = {"user"})`를 적용, `LEFT OUTER JOIN`을 통해 한 번의 쿼리로 연관 데이터를 조회
- DynamicInsert 도입 후 취소
	- @DynamicInsert`를 적용하여 `null`인 필드는 `INSERT` 쿼리에서 제외시켜 DB가 설정된 `Default Value`를 자동으로 채움
	- 하지만 DynamicInsert는 미리 준비된 쿼리를 사용하지 않기에 성능이 떨어짐이 예상 → nullable로 허용하고 프론트엔드 단에서 따로 처리하게 수정

**성과**
- 게시글 조회 쿼리 수 N+1회 → 1회로 단축
- 불필요한 더미 데이터 생성 방지 및 데이터 무결성 확보

### 🗂️ Spring Security와 JSON 인증 필터 커스터마이징

**문제 상황**

프론트엔드에서 x-www-form-urlencoded가 아닌 JSON 형식으로 로그인 데이터를 보내면서, Spring Security의 기본 formLogin()을 사용할 수 없게 됨

**해결 방안**

- Custom Filter 구현
	- AbstractAuthenticationProcessingFilter`를 상속받은 `JsonAuthenticationFilter`를 구현하여 `ObjectMapper`로 JSON 데이터를 파싱하도록 변경
- EntryPoint 도입
	- 인증 실패 시 403 Forbidden이 아닌 명확한 401 응답을 위해 `AuthenticationEntryPoint`를 구현하여 공통 응답 포맷(`ApiResponse`)을 반환하도록 설정

**성과**

- REST API 표준을 준수하는 인증 흐름 완성

### 🧪 테스트 코드 작성

**고려사항**

테스트 코드에 대해 고찰하고 유의미한 테스트 코드를 작성하고자 노력

**성과**

스스로 정한 기준에 맞추어 테스트 코드 작성
 테스트 커버리지 91% 달성, 총 81개 테스트 작성
과정을 [블로그 글(테스트 코드 - 유의미한 테스트와 커버리지 사이의 균형)](https://velog.io/@tl1l1l1s/infio-test)에 작성

## 🔗 관련 블로그 글

- [테스트 코드 - 유의미한 테스트와 커버리지 사이의 균형](https://velog.io/@tl1l1l1s/infio-test)

## 상세 화면
_클릭 시 유튜브로 이동합니다._ <br />
[![Video Label](https://img.youtube.com/vi/sKGC0xNvk4k/0.jpg)](https://youtu.be/sKGC0xNvk4k)

### 홈 화면
<img width="1512" height="824" alt="Screenshot 2025-12-07 at 23 16 01" src="https://github.com/user-attachments/assets/c8bd55da-adba-4953-ac36-90e535bb89ea" />
<img width="1512" height="822" alt="Screenshot 2025-12-07 at 23 14 38" src="https://github.com/user-attachments/assets/a8eec40d-d7bd-43b4-8f57-9f4848d7e010" />

### 회원가입
<img width="1512" height="822" alt="Screenshot 2025-12-07 at 23 18 51" src="https://github.com/user-attachments/assets/4193cd5e-ac33-4156-97d3-70cd0629edab" />

### 로그인
<img width="1512" height="822" alt="Screenshot 2025-12-07 at 23 15 01" src="https://github.com/user-attachments/assets/89e73048-1b53-47b9-bb7a-b117b704bef7" />

### 게시글
<img width="1512" height="815" alt="Screenshot 2025-12-07 at 23 14 55" src="https://github.com/user-attachments/assets/03049399-f4a4-4b7f-87d3-3d138c7eab76" />

### 마이페이지
<img width="1512" height="823" alt="Screenshot 2025-12-07 at 23 16 07" src="https://github.com/user-attachments/assets/ee285143-970c-4e5b-81d3-a5facc2b54cf" />

### 회원정보 수정
<img width="1512" height="824" alt="Screenshot 2025-12-07 at 23 16 13" src="https://github.com/user-attachments/assets/866b9d1d-4b46-48e8-af1d-5586c22d98ba" />
<img width="1512" height="823" alt="Screenshot 2025-12-07 at 23 16 20" src="https://github.com/user-attachments/assets/9c4b1fe7-7af1-4540-8dca-3e698b095d21" />


## 설치 및 실행 방법
1) 저장소 클론: `git clone <repo-url>`
2) 백엔드 디렉터리 이동: `cd ktb3-theta-full-community-be`
3) 환경 준비: JDK 21, MySQL 데이터베이스 `ktb_community` 생성 후 `src/main/resources/application.yml`의 접속 정보 확인/수정
4) 애플리케이션 실행: `./gradlew bootRun`
5) 선택 실행: 테스트 `./gradlew test`, 문서(UI) `http://localhost:8080/swagger-ui.html` 확인

## Frontend Repository...
[👉 Infio-FE-Repository](https://github.com/tl1l1l1s/infio-project-fe)
