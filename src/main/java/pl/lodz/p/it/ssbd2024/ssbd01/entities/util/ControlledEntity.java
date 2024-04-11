package pl.lodz.p.it.ssbd2024.ssbd01.entities.util;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.enums.ActionTypeEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class ControlledEntity extends AbstractEntity {

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "created_by", updatable = false)
    private Account createdBy;

    @OneToOne
    @JoinColumn(name = "updated_by")
    private Account updatedBy;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "action_type", nullable = false)
    private ActionTypeEnum actionType;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        actionType = ActionTypeEnum.CREATE;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Account) {
            createdBy = (Account) authentication.getPrincipal();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        actionType = ActionTypeEnum.UPDATE;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Account){
            updatedBy = (Account) authentication.getPrincipal();
        }
    }

}
