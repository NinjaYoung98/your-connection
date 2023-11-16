# 🙋‍♂your-connection

<img  src="https://github.com/NinjaYoung98/your-connection/assets/124491136/cb53c3eb-5c3a-4832-9450-668d35f78b0b" height="200px" width="300px"></p>

#### Java, Spring으로 개발하는 SNS 프로젝트 

`Your Connection` 프로젝트는 RESTful 원칙에 기반하여 설계되었고, </br>
사용자 간의 상호작용을 최적화하기 위한 다양한 SNS 기능을 구현하고 있습니다.
</br>


## 🎯 프로젝트 기간
 2023.08.23 ~ 2023.10.06
 
 </br>
 
## 🔧 사용 기술 스택
- Java 17
- Spring boot 2.7
- MySQL 8
- Spring Data JPA
- Spring Security
- Redis
- AWS S3
<br></br>


## 🚀 프로젝트 중점사항 요약

**[성능 향상 및 서비스 안정성]**

- 로그인 실패 횟수 트래킹시 조건부 카운트에 대한 동시성 이슈를 Redis, Lua script를 활용하여 해결 
- 무분별한 api 호출을 방지하기 위해 1시간 안에 10번 이상의 rate limit 발생 시 지정된 메신저로 알림 발송하는 시스템 구축
- 조회 작업시  N+1 문제로 인한 성능 저하를 Fetch join, Batch size등 상황에 적절한 방식으로 해결

**[사용자 경험 향상]**

- 코드의 복잡성이 증가하는 다중 파일 업로드 기능을 전략 패턴을 활용해, 최소한의 비용으로 구현
- 관리자를 통한 신고 조치 서비스를 제공하여 컨텐츠 및 서비스 품질 높이기

 **[보안 및 인증]**
- 로그인 실패 횟수 트래킹 및 일정 횟수 초과시 추가 인증 요구
- 이메일 인증 및 OAuth2를 활용한 로그인 인증 서비스 구현

 **[데이터 관리]**
- User 변경 사항 관리 및 추적을 통한 유저 정보 데이터 관리

 **[SNS 기능]**
- 게시물, 댓글, 좋아요,팔로우등 SNS 서비스 기능 구현
- 컨텐츠 번역 기능 구현

</br>

###  📌 Project Posting
- <a href= "https://github.com/NinjaYoung98/your-connection/wiki/Projecting-Posting" target= "blank"> **[프로젝트 관련 포스팅 바로가기]**</a>

### **📌 프로젝트 기능 명세**
- <a href= "https://github.com/NinjaYoung98/your-connection/wiki/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B8%B0%EB%8A%A5-%EB%AA%85%EC%84%B8" target= "blank"> **[프로젝트 기능 명세 바로가기]**</a>

</br>


## 🚀 프로젝트 공통 사항

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

   <strong>[Git Kraken]</strong><br>
  <img width="615" alt="스크린샷 2023-08-30 045044" src="https://github.com/NinjaYoung98/your-connection/assets/124491136/8a20c18d-0ce6-4253-8426-91429842b77b"> <br>
  </br>
   <strong>[project board]</strong><br>
  <br>
  <img width="715" alt="스크린샷 2023-08-30 045225" src="https://github.com/NinjaYoung98/your-connection/assets/124491136/10d6e02f-e3bd-481f-b5a2-1d84b6564869">

- <a href= "https://github.com/users/NinjaYoung98/projects/21" target= "blank"> **[Project Board 바로가기]**</a>

</br>

## 🚀 Project ERD 

- <a href= "https://github.com/NinjaYoung98/your-connection/wiki/Project-ERD" target= "blank"> **[Project ERD 바로가기]**</a>

</br>

## 🚀 API Endpoint


- <a href= "https://docs.google.com/spreadsheets/d/1jz_DEeR26WXDwrCQ6Ko0PzThTtQmLhCU9YCYCwg5mhc/edit?usp=sharing" target= "blank"> **[API Endpoint 구글 시트 바로가기]**</a>

    
