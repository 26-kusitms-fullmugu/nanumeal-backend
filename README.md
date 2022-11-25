# nanumeal-backend
한국의 미쉐린 심대성과 숭실의 로맨틱 가이 김민근이 관리하는 백엔드

## Commit Convention

|표기법|설명|
|:---:|:-----------:|
|ADD|추가시|
|UPDATE|환경 설정 추가/변경 시|
|REMOVE|삭제 시|
|FIX|수정 시|
|CORRECT|문법 오류, 타입, 이름 변경 시|
|REFACTOR|리팩토링 시|
|STYLE|스타일 관련 기능|

> 커밋 방법

|번호|설명|
|:--:|:----------:|
|1|git clone 레포 http 주소 복사|
|2|git remote add origin 레포 http주소 복사|
|3|브랜치 생성 : git branch 자기 브랜치 이름|
|4|내 브랜치로 이동 git checkout자기 브랜치 이름|
|5|git commit -m “커밋메세지”|
|6|git add .|
|7|병합 : git merge origin/내 브랜치|
|8|메인 브랜치로 이동: git checkout main|
|9|푸시: git push|

> Git Flow

![README_GitFlow](https://mblogthumb-phinf.pstatic.net/MjAxODAyMDNfOTgg/MDAxNTE3NjI3MzI0NjU1.V2GkhqrdgVSj0N7n8PDlWb9JvEQInMis5jW1b7QnCE8g.PQtKm7LOuraB3UeBICJ-byEe4SOTiWfIzQylWvzAPxog.PNG.aufcl4858/kF7Uf.png?type=w2)

## 기술 스택
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white" alt="icon" /> <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white"> <img src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white"> <img src="https://img.shields.io/badge/AWS S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white"> <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white"> <img src="https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=Prometheus&logoColor=white"> <img src="https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=Grafana&logoColor=white"> <img src="https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=NGINX&logoColor=white"> <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white">
 
## 시스템 아키텍처
![System Architecture](https://user-images.githubusercontent.com/86938974/203930665-992347d9-dbef-4c65-a186-89a38a7f6e46.png)
- `Springboot(Java)`를 이용한 API 서버를 개발하였고, JPA(ORM)과 `MySQL`을 이용한 CRUD 구현하였습니다.<br>
- 유저의 로그인 정보를 더 안전하게 관리할 수 있는 `Spring Security` 프레임워크를 사용했습니다.<br>
- 정보에 인증이 되어 있고 믿을 수 있는 `JWT`를 사용했습니다.<br>
- 이 때 소셜 로그인을 구현하기 위해 Kakao API를 사용했고, 폼 로그인을 구현하기 위해 이메일 전송 라이브러리를 사용했습니다.<br>
- 객체와 RDBMS를 쉽게 맵핑하고자 JPA, Hibernate, Spring Data JPA를 사용했습니다.<br>
- `AWS EC2`를 이용해 서버를 배포하였고, `AWS RDS`를 이용해 DB 서버를 구성하였습니다.<br>
- `AWS S3`를 이용해 이미지 데이터를 관리하였습니다.<br>
- `Springboot`로 구축한 API 서버와 `Android`를 연결하고 무중단 배포하기 위해서 Nginx를 사용했습니다. <br>
- API문서 자동화를 통해 테스트를 쉽고 편하게 하기 위해 `Swagger`를 사용했습니다. <br>
- 협업 및 코드 버전 관리를 위해 `Github`을 이용하였으며 <br>
- 자동 배포를 위해 `Github actions`를 이용하였습니다. <br>


## 서버 모니터링
![Server Monitoring](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/f9c12fd8-ad05-485d-91ee-46f09570e2e7/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221125%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221125T085051Z&X-Amz-Expires=86400&X-Amz-Signature=157da2616b988b6c2fd11516504290b1d38ffb29b4cbc83630f08905497743eb&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22Untitled.png%22&x-id=GetObject)
- `Prometheus`를 이용해 서버의 데이터 로그를 수집하고, `Grafana`를 이용해 데이터를 시각화하였습니다. <br>


## CI / CD 과정
![CICD_Architecture](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/fa34f9ef-4154-40ae-8112-ca1807620b81/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221125%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221125T075043Z&X-Amz-Expires=86400&X-Amz-Signature=da92d705bc24f39fb332bbe77d0b25ffdb7eb1ac438296d6a1707058302c7f81&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22Untitled.png%22&x-id=GetObject)
1. Git Flow에 따라 기능 구현
2. dev 브랜치에 Pull Request를 보내 코드를 merge
3. merge된 코드가 잘 작동되는 것을 확인 후 master 브랜치에 Pull Request를 보내 merge
4. 코드가 merge되면 Github Actions에서 프로젝트를 빌드 후 해당 JAR 파일을 AWS S3에 전달
5. Github Actions에서 CodeDeploy에 해당 JAR 파일을 배포하도록 전달
6. CodeDeploy는 EC2 서버에 있는 CodeDeploy Agent가 S3 버킷에서 JAR 파일을 받아와 주어진 쉘 스크립트에 따라 배포를 진행하도록 함
7. 새로운 Spring Boot WAS를 띄운 뒤, Nginx 스위칭을 통해 무중단 배포를 진행
