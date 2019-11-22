# Demo Api config
## 개발환경 구성
1. Docker 설치 및 실행(db 서버: root)
```bash
yum install -y yum-utils device-mapper-persistent-data lvm2
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
yum install docker-ce

systemctl start docker
systemctl enable docker
```

3. docker mariadb 설치 및 계정 및 DB(db 서버: root)
```bash
docker container run -d -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=demo1234 \
  -v /var/lib/mariadb:/var/lib/mysql \
  --name mariadb mariadb:10.1

docker exec -it mariadb bash
mysql -uroot -p$MYSQL_ROOT_PASSWORD
> CREATE database demodb CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
> GRANT ALL PRIVILEGES ON demodb.* To 'demo'@'%' IDENTIFIED BY 'demo!234';
```

3. docker gitlab 설치
- 설치
```bash
docker run --detach \
    --hostname 211.253.135.7 \
    --publish 8443:443 --publish 8080:80 --publish 2222:22 \
    --name gitlab \
    --restart always \
    --volume /etc/gitlab/config:/etc/gitlab \
    --volume /var/log/gitlab:/var/log/gitlab \
    --volume /var/opt/gitlab:/var/opt/gitlab \
    --volume /var/opt/gitlab/backups:/var/opt/gitlab/backups \
    gitlab/gitlab-ee:rc
```

- 설정 (파일이 생성 되면 아래 항목 수정)
```bash
vi /etc/gitlab/config/gitlab.rb

external_url 'http://211.253.135.7:8080'


gitlab_rails['gitlab_shell_ssh_port'] = 2222

```
- 접속: http://211.253.135.7:8080

## api 설정

1. Git clone
- demo-web git clone
```bash
mkdir demo
cd demo
git clone http://211.253.135.7:8080/demo/demo-api.git

cd demo-api/bin
./run-dev.sh

```

2. 설정
- 설정 (DB)
```
spring:
  profiles:
    active: local
    include:
  datasource:
    url: jdbc:mysql://172.27.0.3:3306/demodb?useSSL=false&useUnicode=true&characterEncoding=utf8
    username: demo
    password: demo1234

```

- 설정 (APP)
```
# portal
portal:
  apiServerHost: ""
# Admin User
  admin:
    email: admin
    password: admin!234
    name: 관리자
  file:
    default:
      uploadPath: c:/workspace/upload
      downloadPath: http://localhost/upload
    deploy:
      uploadPath: c:/workspace/deploy
      downloadPath: http://localhost/deploy

```
3. Run api
```bash
cd demo-api/bin
./run-dev.sh
```
