package pl.lodz.p.it.ssbd2024.ssbd01.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Mail {

    private final String mailFrom = "eventsymphony202401@gmail.com";
    private String mailTo;
    private String mailSubject;
    private String mailContent;

    public Mail(String mailTo, String mailSubject, String mailContent) {
        this.mailTo = mailTo;
        this.mailSubject = mailSubject;
        this.mailContent = mailContent;
    }
}
