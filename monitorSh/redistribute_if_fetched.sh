#!/bin/bash

# Fetch the latest changes from the remote repository
git fetch

# Check differences between local and remote branches
LOCAL=$(git rev-parse HEAD)
REMOTE=$(git rev-parse origin/master)

if [ "$LOCAL" != "$REMOTE" ]; then
    echo "Local branch is behind remote. Pulling changes..."
    git pull
else
    echo "Local branch is up to date with remote."
fi

# pgrep -f $JAR_NAME 으로 PID 가져오기
# kill -15 로 jar 종료
# build 다시하기
# 빌드 오류 체크하기
# run 하기
# 다시 pgrep -f $JAR_NAME 으로 PID 가져오기
# 재배포 메일 전송하기