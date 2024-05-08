package pl.lodz.p.it.ssbd2024.ssbd01.entity._enum;

import lombok.Getter;

@Getter
public enum LanguageEnum {
    POLISH("pl-PL"),
    ENGLISH("en-US");
    private final String languageCode;

    LanguageEnum(String languageCode) {
        this.languageCode = languageCode;
    }

}
