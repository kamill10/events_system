package pl.lodz.p.it.ssbd2024.ssbd01.entities.util;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Account;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ActionLog extends AbstractEntity{
    @OneToOne
    @Column(nullable = false)
    private Account operationPerformer;
    @OneToOne
    @Column(nullable = false)
    private AbstractEntity operationTarget;
    @ManyToOne
    @Column(nullable = false)
    private ActionType actionType;
    private LocalDateTime actionTime;
}
