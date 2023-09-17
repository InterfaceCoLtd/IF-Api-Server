package xyz.interfacesejong.interfaceapi.domain.Schedule.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService extends BaseTime {

    private final ScheduleRepository scheduleRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

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

        LOGGER.info("[save] 일정 저장 완료");
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    /*
    * 일정 조회
    * */
    @Transactional
    public ResponseEntity<List<ScheduleDTO>> findByDateTime(LocalDate date){
        List<ScheduleDTO> schedules = scheduleRepository.findByDateTimeBetween(date.atStartOfDay());

        LOGGER.info("[findByDateTime] {} 일정 조회", date);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    /*
    * 달별 일정 조회
    * */
    @Transactional
    public ResponseEntity<List<ScheduleDTO>> findByMonth(Integer month){
        Year year = Year.of(LocalDate.now().getYear());
        LocalDateTime startOfMonth = year.atMonth(month).atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = year.atMonth(month % 12 + 1).atDay(1).atStartOfDay();

        List<ScheduleDTO> schedules = scheduleRepository.findByMonth(startOfMonth, endOfMonth);

        LOGGER.info("[findByMonth] {}월 일정 조회", month);
        return new ResponseEntity<>(schedules, HttpStatus.OK);

    }

    /*
    * 모든 일정 조회
    * */
    @Transactional
    public ResponseEntity<List<ScheduleDTO>> findScheduleAll(){
        List<ScheduleDTO> schedules = scheduleRepository.findAll().stream()
                .map(schedule -> ScheduleDTO.builder()
                        .id(schedule.getId())
                        .title(schedule.getTitle())
                        .description(schedule.getDescription())
                        .startDate(schedule.getStartDate())
                        .endDate(schedule.getEndDate())
                        .allDay(schedule.isAllDay()).build())
                .collect(Collectors.toList());

        LOGGER.info("[findScheduleAll] 모든 일정 조회");
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    /*
    * id로 일정 조회*/
    @Transactional
    public ResponseEntity<ScheduleDTO> findById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST SCHEDULE"));

        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .allDay(schedule.isAllDay()).build();

        LOGGER.info("[findById] {} 일정 조회", id);
        return new ResponseEntity<>(scheduleDTO, HttpStatus.OK);
    }

    /* TODO 수정 서비스 구현해야 함
    * 일정 수정
    * */

    /*
    * 일정 제거
    * */
    @Transactional
    public ResponseEntity<Void> delete(Long id){
        try {
            scheduleRepository.deleteById(id);
        }catch (EmptyResultDataAccessException exception){
            LOGGER.info("[delete] 존재하지 않는 id");
            throw new EntityNotFoundException("NON EXIST SCHEDULE");
        }

        LOGGER.info("[delete] {} 일정 삭제", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
