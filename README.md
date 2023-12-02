# demo

## MySQL 도커 사용하기

Docker Desktop 또는 도커 데몬이 이미 설치 및 실행되고 있어야 한다.

docker/mysql 디렉터리 이동하여 다음과 같이 실행한다.

```shell
docker compose up --build
```

다음은 컨테이너를 중단시킨다.

```shell
docker compose down
```

### 테이블 초기화

컨테이너를 실행할 때 테이블이 새롭게 초기화 되며, 다음 경로의 파일에 초기화할 테이블을 정의할 수 있다.

```
docker/mysql/init/db/demo/origin.sql
```

## Redis 도커 사용하기

docker/redis 디렉터리 이동하여 다음과 같이 실행한다.

```shell
docker compose up --build
```

다음은 컨테이너를 중단시킨다.

```shell
docker compose down
```

## API 서버 실행

```shell
./gradlew api:bootRun
```

## 배치 실행

```shell
./gradlew batch:bootRun --args='--job.name=sampleJob execAt=2023-12-01T15:00:00'
```
