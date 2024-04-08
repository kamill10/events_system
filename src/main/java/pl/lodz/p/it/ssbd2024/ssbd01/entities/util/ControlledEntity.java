package pl.lodz.p.it.ssbd2024.ssbd01.entities.util;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.enums.ActionTypeEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class ControlledEntity {

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne
//    @JoinColumn(name = "created_by", updatable = false)
    private Account createdBy;

    @OneToOne
    @JoinColumn(name = "updated_by")
    private Account updatedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionTypeEnum actionType;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        actionType = ActionTypeEnum.CREATE;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        actionType = ActionTypeEnum.UPDATE;
    }

}
