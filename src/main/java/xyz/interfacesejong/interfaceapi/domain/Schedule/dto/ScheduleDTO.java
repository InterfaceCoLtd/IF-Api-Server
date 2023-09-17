package xyz.interfacesejong.interfaceapi.domain.Schedule.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.interfacesejong.interfaceapi.domain.Schedule.domain.ScheduleType;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class ScheduleDTO {
    private Long id;

    @NotNull
    private String title;

    private String description;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime endDate;

    private boolean allDay;

    private ScheduleType type;

    @Builder
    public ScheduleDTO(Long id, String title, String description, LocalDateTime startDate, LocalDateTime endDate, boolean allDay, ScheduleType type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.allDay = allDay;
        this.type = type;
    }
}
