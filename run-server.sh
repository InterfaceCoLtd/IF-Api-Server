#!/bin/bash

# GMT+9 시간대로 설정 (Asia/Seoul)
TZ='Asia/Seoul'
export TZ

# 현재 날짜와 시간을 '년-월-일' 형식으로 가져옴
CURRENT_DATE=$(date +"%Y-%m-%d")

# nohup으로 Java 애플리케이션 실행하면서 로그 파일명을 현재 날짜로 설정
nohup java -jar -Duser.timezone=Asia/Seoul build/libs/interface-api-0.0.1-SNAPSHOT.jar >> "${CURRENT_DATE}.log" 2>&1 &
