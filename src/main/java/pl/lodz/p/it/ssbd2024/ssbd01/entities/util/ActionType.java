package pl.lodz.p.it.ssbd2024.ssbd01.entities.util;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ActionType extends AbstractEntity{
    @Column(nullable = false, unique = true, updatable = false, length = 50)
    public String actionName;
}
