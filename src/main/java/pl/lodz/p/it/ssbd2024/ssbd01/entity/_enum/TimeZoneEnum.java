package pl.lodz.p.it.ssbd2024.ssbd01.entity._enum;

import lombok.Getter;

@Getter
public enum TimeZoneEnum {
    WARSAW("Europe/Warsaw"),
    LONDON("Europe/London");
    private final String timeZone;

    TimeZoneEnum(String timeZone) {
        this.timeZone = timeZone;
    }
}
