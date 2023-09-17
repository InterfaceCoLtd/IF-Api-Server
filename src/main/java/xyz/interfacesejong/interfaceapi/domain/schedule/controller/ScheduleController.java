package xyz.interfacesejong.interfaceapi.domain.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.schedule.domain.Schedule;
import xyz.interfacesejong.interfaceapi.domain.schedule.dto.ScheduleDTO;
import xyz.interfacesejong.interfaceapi.domain.schedule.service.ScheduleService;
import xyz.interfacesejong.interfaceapi.global.aop.Timer;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Timer
    @PostMapping("create")
    public ResponseEntity<Schedule> createSchedule(ScheduleDTO scheduleDTO){
        return scheduleService.save(scheduleDTO);
    }

    @Timer
    @GetMapping("find/all")
    public ResponseEntity<List<ScheduleDTO>> findByAll(){
        return scheduleService.findScheduleAll();
    }

    @Timer
    @GetMapping("find/{id}")
    public ResponseEntity<ScheduleDTO> findById(@PathVariable Long id){
        return scheduleService.findById(id);
    }

    @Timer
    @GetMapping("find/date/{date}")
    public ResponseEntity<List<ScheduleDTO>> findByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return scheduleService.findByDateTime(date);
    }

    @Timer
    @GetMapping("find/month/{month}")
    public ResponseEntity<List<ScheduleDTO>> findByMonth(@PathVariable Integer month){
        return scheduleService.findByMonth(month);
    }

    @Timer
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        return scheduleService.delete(id);
    }
}
