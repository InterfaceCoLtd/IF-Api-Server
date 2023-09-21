#!/bin/bash

# .jar 파일의 이름 (예: myapp.jar)
JAR_NAME="your_jar_name.jar"

# pgrep를 사용하여 .jar 프로세스 검색
pgrep -f $JAR_NAME > /dev/null

# 프로세스가 실행 중이지 않다면 이메일 전송
if [ $? -ne 0 ]; then
    # 이메일 내용 설정
    SUBJECT="Monitoring Alert: JAR Process Not Running"
    TO="kkwjdfo@gmail.com"
    MESSAGE="The JAR process ($JAR_NAME) is not running on $(hostname) as of $(date)."

    # sendmail을 사용하여 이메일 전송
    echo -e "Subject: $SUBJECT\nTo: $TO\n\n$MESSAGE" | sendmail -t
fi