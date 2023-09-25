package xyz.interfacesejong.interfaceapi.domain.Schedule.dto;

import lombok.*;
import xyz.interfacesejong.interfaceapi.domain.Schedule.domain.ScheduleType;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleRequest {
    private String title;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean allDay;

    private ScheduleType type;

    @Builder
    public ScheduleRequest(String title, String description, LocalDateTime startDate, LocalDateTime endDate, boolean allDay, ScheduleType type) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.allDay = allDay;
        this.type = type;
    }
}
