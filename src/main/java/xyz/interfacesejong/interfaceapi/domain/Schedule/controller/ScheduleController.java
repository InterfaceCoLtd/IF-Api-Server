package xyz.interfacesejong.interfaceapi.domain.Schedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.Schedule.domain.Schedule;
import xyz.interfacesejong.interfaceapi.domain.Schedule.dto.ScheduleDTO;
import xyz.interfacesejong.interfaceapi.domain.Schedule.service.ScheduleService;
import xyz.interfacesejong.interfaceapi.global.aop.Timer;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "일정 관리 API")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Timer
    @PostMapping()
    @Operation(summary = "신규 일정 등록", description = "신규 일정을 생성합니다.")
    public ResponseEntity<Schedule> createSchedule(ScheduleDTO scheduleDTO){
        return new ResponseEntity<>(scheduleService.save(scheduleDTO), HttpStatus.CREATED);
    }

    @Timer
    @GetMapping()
    @Operation(summary = "일정 전체 조회", description = "모든 일정을 조회합니다.")
    public ResponseEntity<List<ScheduleDTO>> findAllSchedules(){
        return new ResponseEntity<>(scheduleService.findAllSchedules(), HttpStatus.OK);
    }

    @Timer
    @GetMapping("{id}")
    @Operation(summary = "일정 단건 조회", description = "특정 일정을 해당 일정 id로 조회합니다.")
    public ResponseEntity<ScheduleDTO> findById(@PathVariable Long id){
        return new ResponseEntity<>(scheduleService.findById(id), HttpStatus.OK);
    }

    @Timer
    @GetMapping("date/{date}")
    @Operation(summary = "일정 일간 조회", description = "특정 날짜가 포함된 일정을 조회합니다.")
    public ResponseEntity<List<ScheduleDTO>> findByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return new ResponseEntity<>(scheduleService.findByDateTime(date), HttpStatus.OK);
    }

    @Timer
    @GetMapping("month/{month}")
    @Operation(summary = "일정 월간 조회", description = "특정 월이 포함된 일정을 조회합니다.")
    public ResponseEntity<List<ScheduleDTO>> findByMonth(@PathVariable Integer month){
        return new ResponseEntity<>(scheduleService.findByMonth(month), HttpStatus.OK);
    }

    @Timer
    @DeleteMapping("{id}")
    @Operation(summary = "일정 삭제", description = "해당 일정을 제거합니다.")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id){
        scheduleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
