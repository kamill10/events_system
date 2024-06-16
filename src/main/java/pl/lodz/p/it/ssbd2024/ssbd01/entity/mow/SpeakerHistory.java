package pl.lodz.p.it.ssbd2024.ssbd01.entity.mow;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.ActionTypeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@NoArgsConstructor
@Table(name = "speaker_history")
public class SpeakerHistory extends AbstractEntity {

    @Setter(AccessLevel.NONE)
    @ManyToOne
    @JoinColumn(name = "speaker_id", nullable = false, updatable = false)
    private Speaker speaker;

    @Column(nullable = false, length = 32)
    @NotBlank
    @Size(min = 2, max = 32)
    private String firstName;

    @Column(nullable = false, length = 64)
    @NotBlank
    @Size(min = 2, max = 64)
    private String lastName;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by", updatable = false)
    private Account createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Account updatedBy;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "action_type", nullable = false)
    private ActionTypeEnum actionType;

    public SpeakerHistory(Speaker speaker) {
        this.speaker = speaker;
        this.firstName = speaker.getFirstName();
        this.lastName = speaker.getLastName();
        this.createdAt = speaker.getCreatedAt();
        this.updatedAt = speaker.getUpdatedAt();
        this.createdBy = speaker.getCreatedBy();
        this.updatedBy = speaker.getUpdatedBy();
        this.actionType = speaker.getActionType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpeakerHistory speakerHistory)) {
            return false;
        }
        return getId() != null && getId().equals(speakerHistory.getId());
    }

    @Override
    public final int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

}
