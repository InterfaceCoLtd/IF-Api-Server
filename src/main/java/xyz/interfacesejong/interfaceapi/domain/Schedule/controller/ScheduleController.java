package xyz.interfacesejong.interfaceapi.domain.Schedule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.interfacesejong.interfaceapi.domain.Schedule.domain.Schedule;
import xyz.interfacesejong.interfaceapi.domain.Schedule.dto.ScheduleDTO;
import xyz.interfacesejong.interfaceapi.domain.Schedule.service.ScheduleService;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(ScheduleDTO scheduleDTO){
        return scheduleService.save(scheduleDTO);
    }
}
