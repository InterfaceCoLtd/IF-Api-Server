#!/bin/bash

# GMT+9 시간대로 설정 (Asia/Seoul)
TZ='Asia/Seoul'
export TZ

# 현재 시각
current_time="$(date +"%Y-%m-%d %H:%M:%S")"

# interface-api 프로세스 확인
if ps aux | grep 'interface-api' > /dev/null; then
    process_status="Running"
    status_color="\e[0;32m" # 녹색
else
    process_status="Not Running"
    status_color="\e[0;31m" # 빨간색
fi

# CPU 및 메모리 사용량 확인
pid=$(pgrep -f 'interface-api')
if [ -n "$pid" ]; then
    cpu_usage=$(ps -p $pid -o %cpu | tail -n 1)
    mem_usage=$(ps -p $pid -o %mem | tail -n 1)

     # CPU 사용량 색상
    if (( $(echo "$cpu_usage > 5" | bc -l) )); then
        cpu_color="\e[0;31m"
    else
        cpu_color="\e[0;32m"
    fi

    # 메모리 사용량 색상
    if (( $(echo "$mem_usage > 50" | bc -l) )); then
        mem_color="\e[0;31m"
    else
        mem_color="\e[0;32m"
    fi
else
    cpu_usage="N/A"
    mem_usage="N/A"
    cpu_color="\e[0;37m"
    mem_color="\e[0;37m"
fi

# 결과 로깅
echo -e \
       "\e[0;37m[$current_time]\e[0m" \
       "\e[0;36mProcess Status\e[0m $status_color$process_status    " \
       "\e[0;36mCPU Usage\e[0m $cpu_color$cpu_usage%\e[0m    " \
       "\e[0;36mMemory Usage\e[0m $mem_color$mem_usage%\e[0m"

# 서버 재실행
if [ "$process_status" = "Not Running" ]; then
        ./run_server.sh
        echo "[$current_time] - Server Restart"
fi