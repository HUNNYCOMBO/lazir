## 참고
이 프로젝트는 인프런 백기선님 강의 "스프링과 JPA 기반 웹 애플리케이션 개발"로 학습하며 만들었습니다.  
- [백기선 - 스프링과 JPA기반 웹앱](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-JPA-%EC%9B%B9%EC%95%B1/dashboard)




<details>
  <summary>움짤로 보는 Gather Up</summary>

### 움짤로 보는 동작
#### 1. 회원가입 검증과 가입 후 자동로그인, 메일 보내기
![gather-signupvalidate](https://user-images.githubusercontent.com/78904413/164649157-2bd03dea-7302-49cc-8eaa-71273922fae8.gif)  
- 회원가입시 랜덤한 토큰값을 저장해서, javamailsender를 이용해 메일을 보냅니다.
- InitBinder 어노테이션을 통해 form객체의 요청이 오면, validator 객체를 통해 중복값을 체크하거나, 같은 주소로 이메일을 많이 보내지 않게 방지합니다.

#### 2. 이메일 인증과 프로필 수정
![gather-levelup](https://user-images.githubusercontent.com/78904413/164650170-99f55038-f970-4a2d-86a2-c0dc9ad02bfa.gif)  
- 파라미터의 token값을 통해 이메일 인증을 완료하면 정회원으로 등록되어 팀만들기와 게시판글쓰기 등의 활동이 가능해집니다.
- password는 인코딩되어 저장됩니다.

##### 2.1. 이메일 인증 전 db정보
![gather-signupdatabase](https://user-images.githubusercontent.com/78904413/164651634-cfbe5358-f6d0-4ff2-8813-fa1ce1454db4.jpg)  

##### 2.2. 이메일 인증 후 db정보(Role의 변화)
![gather-levelupafter](https://user-images.githubusercontent.com/78904413/164651690-4427f1c5-eec1-49e7-8be3-d041ff5bfbde.jpg)  

#### 3. 로그인
##### 3.1. 일반 로그인
![gather-login](https://user-images.githubusercontent.com/78904413/164650791-b71ff1c7-b5e2-467e-a849-7d8efa66ebb9.gif)  
- 앞서 만든 계정으로 로그인 합니다.

##### 3.2. 외부 로그인
![gather-oauthlogin](https://user-images.githubusercontent.com/78904413/164650877-2e4b1061-faaf-4f47-9a94-17fc494e2f68.gif)  - google 계정을 통해 로그인합니다. 로그인과 동시에 회원가입 처리가 되며 자동으로 정회원으로 등록됩니다.

#### 4. 팀
##### 4.1. 팀 생성
![gather-maketeam](https://user-images.githubusercontent.com/78904413/164650475-a32f52bf-9e95-4bab-bc15-85ccf3d4e53d.gif)  
- 팀을 만들기 위한 간단한 정보를 입력하고 생성하면 해당 계정은 팀의 관리자로 등록됩니다.
- 관리자는 모집 신청을 수락하거나 거절하는 등 팀의 설정이 가능합니다.

##### 4.2. 팀 가입하기
![gather-jointeam](https://user-images.githubusercontent.com/78904413/164651226-605ba71d-9d27-410c-af03-20a80adfae7c.gif)  
- 공개 중이며 모집 중인 팀은 가입신청이 활성화 되어 가입신청이 가능합니다.
- 관리자 계정이 수락을 해야 팀원으로 등록되고 팀의 인원이 늘어나는 것을 확인 할 수 있습니다.

##### 4.3. 팀 상태수정
![gather-updateteam](https://user-images.githubusercontent.com/78904413/164651897-c861dce9-7e00-4f06-8214-551628c391dd.gif) 
- 관리자는 팀을 비공개로 하거나 모집을 중단하는 등의 상태를 변경 할 수 있습니다.

#### 5. 게시판
![gather-crud](https://user-images.githubusercontent.com/78904413/164675115-8b3a054a-42e8-43e3-b668-1d9b2bc9e3fa.gif)  
- 간단한 게시판과 댓글 CRUD 입니다.
  
</details>
