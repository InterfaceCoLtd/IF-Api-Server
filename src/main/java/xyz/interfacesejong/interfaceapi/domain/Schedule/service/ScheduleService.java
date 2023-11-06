package xyz.interfacesejong.interfaceapi.domain.Schedule.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.interfacesejong.interfaceapi.domain.Schedule.domain.Schedule;
import xyz.interfacesejong.interfaceapi.domain.Schedule.domain.ScheduleRepository;
import xyz.interfacesejong.interfaceapi.domain.Schedule.dto.ScheduleResponse;
import xyz.interfacesejong.interfaceapi.domain.Schedule.dto.ScheduleRequest;
import xyz.interfacesejong.interfaceapi.global.fcm.PushNotificationService;
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

    private final PushNotificationService notificationService;

    private final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    /*
    * 일정 생성
    * */
    @Transactional
    public Schedule save(ScheduleRequest scheduleRequest){
        if (!scheduleRequest.getStartDate().isBefore(scheduleRequest.getEndDate())){
            LOGGER.info("[save] 잘못된 시간 순서");
            throw new IllegalArgumentException("INVALID TIME ORDER");
        }
        Schedule schedule;
        if (scheduleRequest.isAllDay()){
            schedule = scheduleRepository.save(Schedule.builder()
                    .title(scheduleRequest.getTitle())
                    .description(scheduleRequest.getDescription())
                    .startDate(scheduleRequest.getStartDate().withHour(0).withMinute(0).withSecond(0))
                    .endDate(scheduleRequest.getEndDate().withHour(23).withMinute(59).withSecond(59))
                    .allDay(scheduleRequest.isAllDay())
                    .type(scheduleRequest.getType()).build());
        }else{
            schedule = scheduleRepository.save(Schedule.builder()
                    .title(scheduleRequest.getTitle())
                    .description(scheduleRequest.getDescription())
                    .startDate(scheduleRequest.getStartDate())
                    .endDate(scheduleRequest.getEndDate())
                    .allDay(scheduleRequest.isAllDay())
                    .type(scheduleRequest.getType()).build());
        }

        notificationService.sendFcmScheduleAddedNotification(schedule.getId());

        LOGGER.info("[save] 일정 저장 완료");
        return schedule;
    }

    /*
    * 일정 조회
    * */
    @Transactional
    public List<ScheduleResponse> findByDateTime(LocalDate date){
        List<ScheduleResponse> schedules = scheduleRepository.findByDateTimeBetween(date.atStartOfDay());

        LOGGER.info("[findByDateTime] {} 일정 조회", date);
        return schedules;
    }

    /*
    * 달별 일정 조회
    * */
    @Transactional
    public List<ScheduleResponse> findByMonth(Integer month){
        if (month < 0 || month > 12){
            throw new IllegalArgumentException("ILLEGAL MONTH VALUE");
        }
        Year year = Year.of(LocalDate.now().getYear());
        LocalDateTime startOfMonth = year.atMonth(month).atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = year.atMonth(month % 12 + 1).atDay(1).atStartOfDay();
        if (month == 12){
            endOfMonth = endOfMonth.plusYears(1);
        }

        List<ScheduleResponse> schedules = scheduleRepository.findByMonth(startOfMonth, endOfMonth);

        LOGGER.info("[findByMonth] {}월 일정 조회", month);
        return schedules;

    }

    /*
    * 모든 일정 조회
    * */
    @Transactional
    public List<ScheduleResponse> findAllSchedules(){
        List<ScheduleResponse> schedules = scheduleRepository.findAll().stream()
                .map(schedule -> ScheduleResponse.builder()
                        .id(schedule.getId())
                        .title(schedule.getTitle())
                        .description(schedule.getDescription())
                        .startDate(schedule.getStartDate())
                        .endDate(schedule.getEndDate())
                        .allDay(schedule.isAllDay())
                        .type(schedule.getType())
                        .boardId(schedule.getBoardId()).build())
                .collect(Collectors.toList());

        LOGGER.info("[findScheduleAll] 모든 일정 조회");
        return schedules;
    }

    /*
    * id로 일정 조회*/
    @Transactional
    public ScheduleResponse findById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST SCHEDULE"));

        ScheduleResponse scheduleResponse = ScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .allDay(schedule.isAllDay()).build();

        LOGGER.info("[findById] {} 일정 조회", id);
        return scheduleResponse;
    }
    
    @Transactional
    public void updateBoardId(Long id, Long boardId){
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.info("[updateBoardId] 잘못된 일정 아이디");
                    return new EntityNotFoundException("NON EXIST SCHEDULE");
                });
        schedule.updateBoardId(boardId);
        scheduleRepository.save(schedule);

        LOGGER.info("[updateBoardId] 일정에 공지 등록");
    }

    /* TODO 수정 서비스 구현해야 함
    * 일정 수정
    * */

    /*
    * 일정 제거
    * */
    @Transactional
    public void deleteById(Long id){
        try {
            scheduleRepository.deleteById(id);
        }catch (EmptyResultDataAccessException exception){
            LOGGER.info("[delete] 존재하지 않는 id");
            throw new EntityNotFoundException("NON EXIST SCHEDULE");
        }

        LOGGER.info("[delete] {} 일정 삭제", id);
    }

}
