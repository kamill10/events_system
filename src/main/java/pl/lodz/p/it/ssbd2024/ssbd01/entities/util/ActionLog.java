package pl.lodz.p.it.ssbd2024.ssbd01.entities.util;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ActionLog extends AbstractEntity{
    @OneToOne
    @JoinColumn(nullable = false)
    private Account operationPerformer;
    @OneToOne
    @JoinColumn(nullable = false)
    private AbstractEntity operationTarget;
    @ManyToOne
    @JoinColumn(nullable = false)
    private ActionType actionType;
    private LocalDateTime actionTime;
}
