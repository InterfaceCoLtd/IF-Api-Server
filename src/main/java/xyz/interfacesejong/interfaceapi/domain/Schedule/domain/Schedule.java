package xyz.interfacesejong.interfaceapi.domain.Schedule.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Schedule extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean allDay;

    private ScheduleType type;

    @Builder
    public Schedule(String title, String description, LocalDateTime startDate, LocalDateTime endDate, boolean allDay, ScheduleType type) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.allDay = allDay;
        this.type = type;
    }
}
