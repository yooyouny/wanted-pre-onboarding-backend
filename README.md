# wanted-pre-onboarding-backend

#### [원티드 프리온보딩 백엔드 인턴십 - 선발 과제](https://github.com/yooyouny/wanted-pre-onboarding-backend)

--- 

### 1️⃣ 지원자의 성명  

- 김유연  

--- 

### 2️⃣ 애플리케이션의 실행 방법  

- 배포 주소
    ```
  http://3.37.10.158:8080  
    ```  


- 빌드 및 실행  

    ```
  $ git clone https://github.com/yooyouny/wanted-pre-onboarding-backend.git`  
  $ cd wanted-pre-onboarding-backend  
  $ ./gradlew clean build
  $ docker-compose up --build
    ```
  💡테스트 오류 시 ./gradlew clean build -x test로 테스트 제외하고 재빌드  
  

    
- 엔드포인트 호출 방법  

  | description | HTTP Method | End Point|
  |------------|-------------|-----|
  | 회원가입 | POST        | /api/v1/members/register |
  | 로그인 | POST        | /api/v1/members/login |
  | 포스트 등록 | POST        | /api/v1/posts |
  | 특정 포스트 조회 | GET         | /api/v1/posts/{postId} |
  | 특정 포스트 수정 | PUT         | /api/v1/posts/{postId} |
  | 특정 포스트 삭제 | DELETE      | /api/v1/posts/{postId} |
  | 포스트 목록 조회 | GET         | /api/v1/posts |     
  

--- 

### 3️⃣ 시스템 구조 및 기술스택 

### 📌 ERD  

  ![ERO.png](image/ERD.png)
  
### 📌 System Diagram  

  ![System Diagram.png](image/System_Diagram.png)  
  

### 📌 Skill

  - Java 17
  - Spring Boot 3.1.2
  - Spring Data JPA
  - Spring Security
  - Gradle
  - MySQL 8.0
  - Junit5
  - Docker  

---   

### 4️⃣ 구현한 API의 동작을 촬영한 데모 영상



--- 

### 5️⃣ 요구사항 및 구현한 내용 설명  


### 📌 요구사항  
✔️ 데이터 저장소로 MySQL 8.0 사용  

✔️ JWT 인증 방식 적용  

✔️ 통합 테스트 또는 단위 테스트 코드 추가  

✔️ docker compose를 이용한 애플리케이션 환경 구성  

✔️ 클라우드 환경(AWS)에 배포 환경을 설계하고 애플리케이션을 배포  



### 📌 구현내용  

  ✔️ 유효성 검사 
   - spring validation 적용
   - DTO인 request 객체에서 필수 String 값인 email, password의 @NotEmpty 체크  
  
✔️ 비밀번호 암호화  
  -  BCryptPasswordEncoder 적용하여 인코딩 된 비밀번호를 DB에 저장  

  ✔️ 중복코드 최소화 
  -  JpaAuditing 적용으로 엔티티의 생성시간, 수정시간 자동관리 
  -  엔드포인트 별 request/response DTO 분리로 layer별 의존성을 줄임  
    
  ✔️ Soft delete 처리
  - 데이터를 완전히 삭제하지 않고 delete flag 값을 true로, delete_at에 LocalDateTime.now() 갱신하여 처리  
  
  ✔️ 트랜잭션 처리 
  - 쓰기작업(Create, Update, Delete)에 @Transactional 처리로 데이터 정합성 유지 

  ✔️ Pagenation 적용
  - Spring Data Jpa가 제공하는 페이징 기능을 활용하여 사용자가 요구하는 Page Number, size에 따른 결과 제공
      
  ✔️ 응답 및 예외처리 
  - @RestControllerAdice로 공통 예외 처리
  - ApiRespose 객체로 규격화된 응답 처리
  - @AuthenticationPrincipal로 claims에 저장된 member정보와 비교하여 수정, 삭제 시 FORBIDDEN 처리  

  ✔️️ 테스트 코드 
  - layer 별 단위테스트 수행(repository, service, controller, domain) 
  - controller 통합테스트 수행으로 드의 신뢰성을 높임

  ✔️️ 배포
  - docker-compose 활용으로 어플리케이션 환경을 쉽게 구성 
  

---


### 6️⃣ API 명세(request/response 포함)


### 1. 회원가입  
- POST `/api/v1/members/register`

  ➡️ request
    ```
    {
      "email": "wated_user@naver.com",
      "password" : "password"
    }
    ```  

  ✅️ response
   - 200 OK
      ```
     {
       "status": "OK",
       "message": "SUCCESS",
       "data": {
            "id": 2,
            "email": "wated_user@naver.com",
            "password": "$2a$10$Fym8KNIMMFfLmbSF7mF/wunQNPGDBPKgZBFdqq7jFu8YVQeqMwVB2",
            "registeredAt": "2023-08-15T06:13:38.085124274"
        }
     }
      ```
   - 400 Bad Request
      ```
     {
       "status": "BAD_REQUEST",
       "message": "BAD_REQUEST",
       "data": [
           "이메일에 '@' 문자가 포함되어 있어야 합니다",
           "비밀번호는 최소 8자 이상이어야 합니다" 
       ]
     }
        ```
   - 409 Conflict
      ```
       {
          "status": "CONFLICT",
          "message": "Email is duplicated",
          "data": null
       }
        ```
### 2. 로그인  
- POST `/api/v1/members/login`  

  ➡️ request 
    ```
    {
      "email": "wated_user@naver.com",
      "password" : "password"
    }
    ```  

  ✅️ response
   - 200 OK
       ```
     {
         "status": "OK",
         "message": "SUCCESS",
         "data": {
             "token": "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IndhdGVkX3VzZXJAbmF2ZXIuY29tIiwiaWF0IjoxNjkyMDgxMzc5LCJleHAiOjE2OTQ2NzMzNzl9.lo2_4AGAOz3GRqeJnMML-c3V7F_9y0Hiyox3i4z-jGo"
         }
     }

   - 401 Unauthorized
       ```
     {
         "status": "UNAUTHORIZED",
         "message": "Password is invalid",
         "data": null
     }
       ```
   - 404 Not Found
      ```
     {
         "status": "NOT_FOUND",
         "message": "Email not founded",
         "data": null
     }  
       
### 3. 포스트 등록
- POST `/api/v1/posts`
- Headers `Authorization: Bearer {token}`

  ➡️ request
    ```
    {
      "title": "title",
      "body" : "body"
    }
    ```  

  ✅️ response
  - 200 OK
     ```
    {
      "status": "OK",
      "message": "SUCCESS",
      "data": {
         "id": 1,
         "title": "title",
         "body": "password",
         "email": "user@naver.com",
         "registeredAt": "2023-08-15T03:54:26.298941179"
      }
    }
     ```
  - 400 Bad Request
     ```
    {
      "status": "BAD_REQUEST",
      "message": "BAD_REQUEST",
      "data": [
          "게시글 내용은 필수입니다",
          "게시글 제목은 필수입니다"
       ]
    }
     ```
### 4. 특정 포스트 조회
  - GET `/api/v1/posts/{postId}`
  - Headers `Authorization: Bearer {token}`
 
    ✅️ response
    - 200 OK
        ```
      {
          "status": "OK",
          "message": "SUCCESS",
          "data": {
              "id": 5,
              "title": "test",
              "body": "contents",
              "email": "test@naver.com",
              "registeredAt": "2023-08-15T07:04:50.374106",
              "modifiedAt": "2023-08-15T07:04:50.374106"
          }
      }
        ```
    - 404 Not Found
      ```
      {
        "status": "NOT_FOUND",
        "message": "Post not founded",
        "data": null
      }
        ```
### 5. 특정 포스트 수정
- PUT `/api/v1/posts/{postId}`
- Headers `Authorization: Bearer {token}`

  ➡️ request
    ```
  {
      "title": "modified title",
      "body" : "modified body"
  }
    ```  

  ✅️ response
  - 200 OK
      ```
    {
        "status": "OK",
        "message": "SUCCESS",
        "data": {
          "id": 5,
          "title": "modified title",
          "body": "modified contents",
          "email": "test@naver.com",
          "registeredAt": "2023-08-15T07:04:50.374106",
          "modifiedAt": "2023-08-15T07:28:21.442714841"
        }
    }
      ```
  - 400 Bad Request
     ```
    {
      "status": "BAD_REQUEST",
      "message": "BAD_REQUEST",
      "data": [
          "게시글 내용은 필수입니다",
          "게시글 제목은 필수입니다"
       ]
    }
     ```
  - 403 Forbidden
      ```
    {
        "status": "FORBIDDEN",
        "message": "This post is accessible only to the author of the post",
        "data": null
      }
      ```
  - 404 Not Found
      ```
      {
        "status": "NOT_FOUND",
        "message": "Post not founded",
        "data": null
      }
      ```
### 6. 특정 포스트 삭제
- DELETE `/api/v1/posts/{postId}`
- Headers `Authorization: Bearer {token}`

  ✅️ response
    - 200 OK
      ```
      {
        "status": "OK",
        "message": "SUCCESS",
        "data": null
      }
      ```
    - 403 Forbidden
      ```
      {
          "status": "FORBIDDEN",
          "message": "This post is accessible only to the author of the post",
          "data": null
      }
      ```
    - 404 Not Found
      ```
      {
          "status": "NOT_FOUND",
          "message": "Post not founded",
          "data": null
      }
      ```
### 7. 포스트 목록 조회
- GET `/api/v1/posts`
- Headers `Authorization: Bearer {token}`

  ✅️ response
    - 200 OK
      ```
      {
          "status": "OK",
          "message": "SUCCESS",
          "data": {
              "content": [
                {
                    "id": 5,
                    "title": "modified title",
                    "body": "modified contents",
                    "email": "test@naver.com",
                    "registeredAt": "2023-08-15T07:04:50.374106",
                    "modifiedAt": "2023-08-15T07:52:32.165673"
                },
                {
                    "id": 4,
                    "title": "wanted",
                    "body": "contents",
                    "email": "wanted_user@naver.com",
                    "registeredAt": "2023-08-15T07:04:28.243249",
                    "modifiedAt": "2023-08-15T07:04:28.243249"
                }
              ],
              "pageable": {
              "sort": {
                  "empty": false,
                  "sorted": true,
                  "unsorted": false
              },
              "offset": 0,
              "pageNumber": 0,
              "pageSize": 5,
              "paged": true,
              "unpaged": false
              },
              "totalPages": 1,
              "totalElements": 2,
              "last": true,
              "size": 5,
              "number": 0,
              "sort": {
                  "empty": false,
                  "sorted": true,
                  "unsorted": false
              },
              "numberOfElements": 2,
              "first": true,
              "empty": false
              }
          }   
      ```