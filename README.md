# 🙋‍♂your-connection
#### Java, Spring으로 개발하는 SNS 프로젝트 
</br>


## 🎯 프로젝트 기간
 2023.08.23 ~ 2023.10.06
 
 </br>
 
## 🔧 사용 기술 스택
- Java 17
- Spring boot 2.7
- MySQL 8
- Spring Data JPA
- Redis
- AWS S3
<br></br>

## 📌 프로젝트 공통 사항

<details>
   <summary> <ins> 프로젝트 공통 사항 (클릭하여 펼치기)</ins></summary><br>
 
### docs (template)
- [[Commit template]](https://github.com/NinjaYoung98/your-connection/blob/main/docs/git_convention.txt)
- [[DB_convention]](https://github.com/NinjaYoung98/your-connection/blob/main/docs/db_convention)
- [[PR_template]](https://github.com/NinjaYoung98/your-connection/blob/main/.github/pull_request_template.md)
### Code Style
- [Google code Style 적용](https://google.github.io/styleguide/javaguide.html)
<br></br>
## 📑 깃 브랜치 전략
<img width="505" alt="스크린샷 2023-08-30 054216" src="https://github.com/NinjaYoung98/your-connection/assets/124491136/10a04678-aeff-454e-a322-6be2d2486556"> <br>
- 현재 프로젝트 규모를 고려하여 상대적으로 가벼운 GitHub-flow 정책을 도입하였습니다.<br>
- 요구사항에 대한 깃 이슈 번호를 할당받고 그에 맞는 feature 브랜치를 생성합니다.<br>
- GitHub에서 제공하는 칸반보드와 UI 도구인 Git Kraken으로 깃 이슈번호와 브랜치를 관리해 주었습니다.<br>
 - [[Github-flow Reference1]](https://build5nines.com/introduction-to-git-version-control-workflow/)
- [[Github-flow Reference2]](https://blog.hwahae.co.kr/all/tech/9507)<br>

   <strong>[Git Kraken]</strong><br>
  <img width="685" alt="스크린샷 2023-08-30 045044" src="https://github.com/NinjaYoung98/your-connection/assets/124491136/8a20c18d-0ce6-4253-8426-91429842b77b"> <br>
   <strong>[project board]</strong><br>
  <br>
  <img width="865" alt="스크린샷 2023-08-30 045225" src="https://github.com/NinjaYoung98/your-connection/assets/124491136/10d6e02f-e3bd-481f-b5a2-1d84b6564869">
</details>

## 🚀 프로젝트 중점사항 요약

- 계정 보호를 위한 사용자 로그인 실패 횟수 트래킹 및 일정 횟수 초과시 추가 인증 요구
- Redis, Lua script 를 활용하여 트래킹시 발생되는 조건부 카운트에 대한 동시성 제어
- Rate limit 핸들링을 통한 무분별한 API 호출 방지하여 서비스 안정성 높이기
- 1시간 안에 10번 이상의 rate limit 발생시 지정된 메신저로 알림 발송하는 대응 시스템 구축
  
- 전략 패턴을 이용하여 최소의 비용으로 효율적인 다중 파일 업로드 구현
- User 변경 사항 관리 및 추적을 통한 유저 정보 데이터 관리 
- 관리자를 통한 신고 조치 서비스를 이용하여 컨텐츠 및 서비스 품질 높이기 
- 팔로우 서비스를 응용한 함께 아는 친구 기능 제공
- 이메일 인증 및 OAuth2를 활용한 로그인 인증 서비스 구현 
- 이외에 SNS 서비스 기능 구현(게시물, 댓글, 좋아요,번역...)  
  
<a href= "https://velog.io/@whcksdud8" target= "blank"> [프로젝트 정리 블로그 링크]</a>

</br>
<details>
   <summary> <ins> 프로젝트 기능 명세  (클릭하여 펼치기)</ins></summary><br>
 
## ✅ User

### User 공통 사항
- User 활동 로그에 대한 4개의 Activity 값 `NORMAL(일반 유저)`,`FLAGGED(신고받은 유저)`,`LOCKED(일시정지)`,`BAN(이용 제한 유저)`이 존재합니다. 
  - `FLAGGED(신고받은 유저)`는 다른 유저들에게 신고를 받은 누적 횟수가 허용치를 초과하게 된다면 도달하는 상태이며,</br>
     Admin계정에 의해 관리될 수 있습니다.
  - `LOCKED(일시정지)`는  계정을 보호하기 위한 레벨로서 추가적인 인증을 해야합니다.
  - `LOCKED(일시정지)`상태에서 인증이 성공적으로 완료되었다면 그 이전의 Activity 값으로 원복되며 서비스를 다시 이용할 수 있습니다.
  - `BAN(이용 제한 유저)`상태가 되면, 특별한 조치가 없는 이상 유저는 서비스를 이용할 수 없습니다.
- User의 role 은 `USER`, `ADMIN` 으로 구분됩니다.
- JWT 토큰을 통해 유저를 식별합니다.
---

- **회원가입**
  - `username`, `password`, 이메일, 닉네임을 입력받습니다. 
  - `username`과 이메일은 중복될 수 없으며 하나의 이메일당 하나의 계정을 생성할 수 있습니다.
  - 회원가입시 유저의 이메일을 통해 보안 코드가 발송되며 5분 이내에 이를 입력해야 합니다.(보안 코드는 `Redis`의 TTL 활용하여 관리)
- **로그인**
  - 서비스 자체 로그인 또는 소셜 로그인(카카오, 네이버)을 할 수 있습니다.
  - 로그인 5회 이상 실패 시 사용자는 추가적인 인증을 해야 하며 계정은 `Locked` 상태에 진입하게 됩니다.
  - 실패 횟수에 대한 카운트 누적 및 원자성을 보장받기 위해 redis + lua script 를 활용하였습니다.
- **유저 프로필 이미지 등록**
  - 프로필 이미지를 업로드/삭제 할 수 있습니다.(프로필의 이미지는 AWS S3의 버킷에 저장됩니다.)
  - 이미지의 사이즈가 일정치를 초과했을때 `Marvin` 라이브러리를 통해 이미지는 리사이징 됩니다.
- **회원 정보 변경**
  - 로그인 이후에 해당 서비스를 이용할 수 있습니다.
  - `username`, 패스워드등을 변경할 수 있습니다.


</br>

## ✅ Admin

### Admin 공통 사항
- `UserRole.ADMIN`의 권한을 가진 계정이 어드민 서비스를 이용할 수 있습니다.
---
- **유저 조회**
  - `UserActivity`를 기준으로 유저를 검색하고 조회할 수 있습니다.
- **유해 컨텐츠 탐색**
  -  유저들에게 신고를 받은 컨텐츠를 탐색하고 조회할 수 있습니다.
- **신고 내역 조회**
  - 유저 및 컨텐츠에 대한 신고 내역(신고자,신고 대상, 신고 일자, 신고 내용)을 확인할 수 있습니다.
 - **신고 조치 서비스**
   - 신고 내역을 기반으로 그에 따른 조치를 취할 수 있습니다.
   - 이상 없다고 판단 될 경우 `NORMAL` 상태로 변경
   - 문제가 된다고 판단 될 경우 `BAN`을 할 수 있으며 `BAN`상태에 진입 시 서비스 이용이 불가능합니다.
</br>

## ✅ Content

### Content 공통 사항
- post(게시물) 파일 업로드는 AWS S3의 버킷에 저장되며 여러 파일을 동시에 업로드할 수 있습니다.
- 파일마다 지원하는 확장자 및 필요한 작업(리사이징, 용량 제한)등이 다르기에 전략 패턴을 구사했습니다.
- 다량의 조회 또는 페이지네이션 환경에서 발생하는 N+1문제를 적절한 방식(fetch join, Bactch size...)으로 해결했습니다.
- 신고 누적에 따른 컨텐츠 상태 변화
   - GENERAL("일반 컨텐츠"), 일반 컨텐츠입니다.
   - FLAGGED("선정성이 있는 컨텐츠"), 신고 허용치를 넘어선 컨텐츠이며 Admin의 관리를 필요로합니다.
   - RESTRICTED("제한된 컨텐츠"), 제한된 컨텐츠이며 Admin에 의해서만 상태 변화가 이루어집니다.
  
---
 

- **게시물 기능**
  - 게시물에 대한 CRUD 작업을 수행할 수 있습니다.
- **게시물 키워드 조회**
  - 키워드로 게시물을 검색할 수 있습니다. 
- **파일 업로드 기능**
  - 이미지등 여러 파일을 동시에 업로드할 수 있습니다. 
- **좋아요 기능**
  - 게시물에 대해서 "좋아요/취소" 기능을 이용할 수 있습니다.
- **댓글 기능**
  - 댓글에 대한 CUUD 작업을 수행할 수 있습니다.  

- **Follow(팔로우 기능)**
  - 유저는 자기 자신을 제외한 유저에 대해서 "팔로우/언팔로우" 할 수 있습니다.
  - 팔로우 및 팔로워 리스트를 조회할 수 있습니다.
  - "함께 아는 친구(나의 팔로잉 리스트들의 팔로잉 목록)" 기능을 제공하며, 이를 통해서 특정 유저 검색시 해당 유저를 팔로우하고 있는 나의 팔로잉의 수와 목록을 조회할 수 있습니다.
  
</br>

## ✅ Report

### Report 공통 사항 
 - 자기 자신을 신고하거나 중복된 신고에 대해서는 신고 접수가 이루어지지 않습니다.
 - 신고한 유저가 이미 BAN 되었다면 신고 접수가 이루어지지 않습니다.

---
- **신고 서비스**
  - 유저 및 게시물에 대해서 신고를 접수할 수 있습니다.
  - 신고 횟수가 지정된 횟수를 넘어선다면 해당 유저 또는 게시물에 대한 Activity 값이 `FLAGGED` 상태로 변경됩니다.
</br>

## ✅ 운영적 요소  

### Protection
- 인터셉터단에서 IP( IPv4)로 요청의 근원지를 식별합니다.
- Bucket4j 라이브러리를 통해 api 호출에 대한 버켓과 토큰을 지정했습니다.
- Concurrenthashmap을 통해 호출에 대한 횟수를 누적시키며 모든 토큰을 소모할 시 1회 rate limit으로 간주합니다.


### Notification

- Http status 500 발생 시 지정된 메신저(현재 텔레그램)로 에러에 대한 메시지가 발송됩니다.
- 1시간 안에 rate limit 10 회 이상 발생 시 텔레그램으로 발생시킨 근원지에 대한 메시지가 발송됩니다.

</details>




     
    
