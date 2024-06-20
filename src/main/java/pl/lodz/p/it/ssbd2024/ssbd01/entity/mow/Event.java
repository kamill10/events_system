package pl.lodz.p.it.ssbd2024.ssbd01.entity.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.EntityIsUnmodifiableException;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ControlledEntity;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Table(name = "event")
public class Event extends ControlledEntity {

    @Column(nullable = false, unique = true, updatable = true)
    @NotBlank(message = ExceptionMessages.INCORRECT_NAME)
    @Size(min = 3, max = 128, message = ExceptionMessages.INCORRECT_NAME)
    private String name;

    @Size(max = 1024, message = ExceptionMessages.INCORRECT_DESCRIPTION)
    @NotNull(message = ExceptionMessages.INCORRECT_DESCRIPTION)
    @Column(columnDefinition = "varchar(1024)", nullable = false)
    private String descriptionPL;

    @Size(max = 1024, message = ExceptionMessages.INCORRECT_DESCRIPTION)
    @NotNull(message = ExceptionMessages.INCORRECT_DESCRIPTION)
    @Column(columnDefinition = "varchar(1024)", nullable = false)
    private String descriptionEN;

    @Column(nullable = false)
    @NotNull
    private Boolean isNotCanceled = true;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "event")
    private List<Session> sessions = new ArrayList<>();


    /**
     * Sessions that are part of event cannot start before start date.
     */
    @NotNull(message = ExceptionMessages.INCORRECT_START_DATE)
    private LocalDateTime startDate;

    /**
     * Sessions that are part of event cannot start after end date.
     */
    @NotNull(message = ExceptionMessages.INCORRECT_END_DATE)
    private LocalDateTime endDate;


    public Event(String name, String descriptionPL, List<Session> sessions,
                 LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.descriptionPL = descriptionPL;
        this.sessions = sessions;
        this.startDate = startDate;
        this.endDate = endDate;
        this.descriptionEN = "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event event)) {
            return false;
        }
        return Objects.equals(name, event.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

}
