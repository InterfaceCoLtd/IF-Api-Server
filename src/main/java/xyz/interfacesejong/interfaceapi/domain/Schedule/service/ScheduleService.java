package xyz.interfacesejong.interfaceapi.domain.Schedule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.interfacesejong.interfaceapi.domain.Schedule.domain.Schedule;
import xyz.interfacesejong.interfaceapi.domain.Schedule.domain.ScheduleRepository;
import xyz.interfacesejong.interfaceapi.domain.Schedule.dto.ScheduleDTO;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService extends BaseTime {

    private final ScheduleRepository scheduleRepository;

    /*
    * 일정 생성
    * */
    @Transactional
    public ResponseEntity<Schedule> save(ScheduleDTO scheduleDTO){
        Schedule schedule;
        if (scheduleDTO.isAllDay()){
            schedule = Schedule.builder()
                    .title(scheduleDTO.getTitle())
                    .description(scheduleDTO.getDescription())
                    .startDate(scheduleDTO.getStartDate().withHour(0).withMinute(0).withSecond(0))
                    .endDate(scheduleDTO.getEndDate().withHour(23).withMinute(59).withSecond(59))
                    .allDay(scheduleDTO.isAllDay())
                    .type(scheduleDTO.getType()).build();
        }else{
            schedule = Schedule.builder()
                    .title(scheduleDTO.getTitle())
                    .description(scheduleDTO.getDescription())
                    .startDate(scheduleDTO.getStartDate())
                    .endDate(scheduleDTO.getEndDate())
                    .allDay(scheduleDTO.isAllDay())
                    .type(scheduleDTO.getType()).build();
        }

        scheduleRepository.save(schedule);

        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    /*
    * 일정 조회
    * */

    /*
    * 모든 일정 조회
    * */
    @Transactional
    public ResponseEntity<List<ScheduleDTO>> findAllSchedule(){
        List<ScheduleDTO> schedules = scheduleRepository.findAll().stream()
                .map(schedule -> ScheduleDTO.builder()
                        .id(schedule.getId())
                        .title(schedule.getTitle())
                        .description(schedule.getDescription())
                        .startDate(schedule.getStartDate())
                        .endDate(schedule.getEndDate())
                        .allDay(schedule.isAllDay()).build())
                .collect(Collectors.toList());

        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ScheduleDTO> findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Non Exist Schedule"));

        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .allDay(schedule.isAllDay()).build();

        return new ResponseEntity<>(scheduleDTO, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ScheduleDTO> findScheduleByDate(LocalDate date){
        return null;
    }

    /*
    * 일정 수정
    * */

    /*
    * 일정 제거
    * */
    @Transactional
    public ResponseEntity<Void> delete(Long id){
        scheduleRepository.delete(scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Non Exist Schedule")));

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
