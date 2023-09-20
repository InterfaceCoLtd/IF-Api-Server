package xyz.interfacesejong.interfaceapi.domain.Schedule.controller;

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
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Timer
    @PostMapping()
    public ResponseEntity<Schedule> createSchedule(ScheduleDTO scheduleDTO){
        return new ResponseEntity<>(scheduleService.save(scheduleDTO), HttpStatus.CREATED);
    }

    @Timer
    @GetMapping()
    public ResponseEntity<List<ScheduleDTO>> findAllSchedules(){
        return new ResponseEntity<>(scheduleService.findAllSchedules(), HttpStatus.OK);
    }

    @Timer
    @GetMapping("{id}")
    public ResponseEntity<ScheduleDTO> findById(@PathVariable Long id){
        return new ResponseEntity<>(scheduleService.findById(id), HttpStatus.OK);
    }

    @Timer
    @GetMapping("date/{date}")
    public ResponseEntity<List<ScheduleDTO>> findByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return new ResponseEntity<>(scheduleService.findByDateTime(date), HttpStatus.OK);
    }

    @Timer
    @GetMapping("month/{month}")
    public ResponseEntity<List<ScheduleDTO>> findByMonth(@PathVariable Integer month){
        return new ResponseEntity<>(scheduleService.findByMonth(month), HttpStatus.OK);
    }

    @Timer
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id){
        scheduleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
