#!/usr/bin/env bash
REPOSITORY=/root/codelia_spring
PROJECT_NAME=new
cd $REPOSITORY || exit

# 해당 디렉토리에서 -> jar 패턴 검색 -> 맨 마지막 라인 1줄 출력
JAR_NAME=$(find ./build/libs -name "$PROJECT_NAME*[^(plain)].jar" | tail -n 1)

# 실행중인 프로세스 중 PROJECT_NAME 검색
CURRENT_PID=$(pgrep -f "${PROJECT_NAME}" | head -n 1)

echo "> $CURRENT_PID"
# CURRENT_PID 의 길이가 0이면 참
if [ -z "$CURRENT_PID" ];
then
  echo "> 구동중인 application 없음"
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 "$CURRENT_PID"
fi

# background 실행
echo "> new application deploy"
# 2>&1 == 표준 에러만 표준 출력 , & == background 실행
nohup java -jar \
  -Dspring.profiles.active=prod \
  "$JAR_NAME" > /root/app_log.out 2>&1 &

