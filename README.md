# 🙋‍♂your-connection
#### Java, Spring으로 개발하는 언어교환 SNS 프로젝트 
## 💡 프로젝트 목적

- 언어교환 SNS 서비스 프로젝트 입니다.<br>
- 영어 공부를 하면서 첫 언어교환 모임을 참여했을 때 느꼈던 아쉬움과 어려움을 프로젝트의 주제로 녹여나간다면  몰입의 즐거움을 느낄 수 있을 것 같습니다.<br>
- 꾸준히 업데이트해 나가며 해당 프로젝트의 규모를 점진적으로 늘려나갈 예정이고 여러 가지의 것들을 많이 시도해 보고 싶습니다.
## 🎯 프로젝트 기간
 2023.08.23 ~ ing
 <br></br>
## 🔧 사용 기술 스택
- Java 17
- Spring boot 2.7
- MySQL 8
- Spring Data JPA
- Redis
<br></br>
## 📍 프로젝트 컨벤션
첫 팀 프로젝트를 진행 당시 팀에 코드 컨벤션과 커밋 메시지 규격에 대한 명확한 기준이 없었습니다.<br>
이로 인해 프로젝트는 여러 차질을 겪게 되었고, 이는 개발 과정에 상당한 혼란을 초래했습니다.<br>
그 경험을 통해 코드와 커밋 메시지의 일관성의 중요성을 깨닫고 이를 습관화하려 합니다.
### docs (template)
- [[Commit template]](https://github.com/NinjaYoung98/your-connection/blob/main/docs/git_convention.txt)
- [[DB_convention]](https://github.com/NinjaYoung98/your-connection/blob/main/docs/db_convention)
- [[PR_template]](https://github.com/NinjaYoung98/your-connection/blob/main/.github/pull_request_template.md)
### Code Style
- [Google code Style 적용](https://google.github.io/styleguide/javaguide.html)
<br></br>
## 📑 깃 브랜치 전략
<img width="305" alt="스크린샷 2023-08-30 054216" src="https://github.com/NinjaYoung98/your-connection/assets/124491136/10a04678-aeff-454e-a322-6be2d2486556"> <br>
- 현재 프로젝트 규모를 고려하여 상대적으로 가벼운 GitHub-flow 정책을 도입하였습니다.<br>
- 요구사항에 대한 깃 이슈 번호를 할당받고 그에 맞는 feature 브랜치를 생성합니다.<br>
- GitHub에서 제공하는 칸반보드와 UI 도구인 Git Kraken으로 깃 이슈번호와 브랜치를 관리해 주었습니다.<br>
 - [[Github-flow Reference1]](https://build5nines.com/introduction-to-git-version-control-workflow/)
- [[Github-flow Reference2]](https://blog.hwahae.co.kr/all/tech/9507)<br>
<details>
   <summary> <ins> 클릭하여 이미지 보기</ins></summary><br>
   <strong>[Git Kraken]</strong><br>
  <img width="685" alt="스크린샷 2023-08-30 045044" src="https://github.com/NinjaYoung98/your-connection/assets/124491136/8a20c18d-0ce6-4253-8426-91429842b77b"> <br>
   <strong>[project board]</strong><br>
  <br>
  <img width="865" alt="스크린샷 2023-08-30 045225" src="https://github.com/NinjaYoung98/your-connection/assets/124491136/10d6e02f-e3bd-481f-b5a2-1d84b6564869">
</details>

## 🚀 프로젝트 중점사항 요약
  
- <Strong>[Clean code]</Strong>
  - 효율적인 코드를 만들기 위해 코드의 규칙성을 캐치하려 합니다.
  - 코드의 가독성을 위해 단순하고 간단한 네이밍을 지향합니다.
  - resolver, interceptor 등을 활용하여 불필요한 controller의 로직을 줄여나갑니다. 
  <br></br>
- <Strong>[서비스 로직]</Strong>
  - 로그인 기능
    - SMTP, Redis 활용한 이메일 보안 코드 발신 및 회원가입 인증
    - spring security를 활용한 토큰 인증 
    - OAuth2 로 구현한 카카오 , 네이버 로그인
    - user Acitivity ,ci 값 등록 (예정)
    - 사용자의 로그인 실패횟수를 트래킹하고, 일정 횟수 이상 실패시 로그인을 거부하거나 Captcha를 요구(예정)
  - 비즈니스 로직
    - DeepL api를 활용한 다국어 번역 기능
    - redis pub/sub 활용한 채팅 기능 지원 (예정)
    <br></br>
- <Strong>[서비스 운영]</Strong>
  - 텔레그램을 통한 에러 모니터링 시스템 구축
  - 1초에 10번 이상의 Rate limit 발생 될 경우 텔레그램으로 알림
   <br></br>
- <Strong> [성능 개선] </Strong>
  - 페이지네이션으로 한 번에 조회되는 데이터의 size를 조정합니다.
  - JPA N+1 문제를 의식하고 이를 해결할 수 있는 방법들을 도입합니다 (fetch join , batch size...)<br>
  <br>
<ins><이 기술들을 왜 사용했으며 어떤 문제점들이 있었을까요?></ins>
    
