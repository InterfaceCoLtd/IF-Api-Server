#!/bin/bash

TZ='Asia/Seoul'
export TZ

CURRENT_DATE=$(date +"%Y-%m-%d")

nohup java -jar -Duser.timezone=Asia/Seoul build/libs/interface-api-0.0.1-SNAPSHOT.jar >> "${CURRENT_DATE}.log" 2>&1 &
