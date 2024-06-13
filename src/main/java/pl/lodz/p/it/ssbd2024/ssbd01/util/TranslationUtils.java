package pl.lodz.p.it.ssbd2024.ssbd01.util;

import com.deepl.api.DeepLException;
import com.deepl.api.Translator;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Event;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.EventDTOConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TranslationUtils {

    public static void translateEvent(Event event, String language, String apiKey) throws DeepLException, InterruptedException {

        if (apiKey == null || apiKey.isEmpty()) {
            return;
        }

        Translator translator = new Translator(apiKey);
        event.setDescriptionPL(translator.translateText(event.getDescriptionPL(), null, "pl").getText());
        event.setDescriptionEN(translator.translateText(event.getDescriptionPL(), null, "en-US").getText());
    }

    public static List<GetEventDTO> resolveEventsLanguage(List<Event> events, String language) {
        List<GetEventDTO> eventDTOs = new ArrayList<>();

        for (Event event : events) {
            eventDTOs.add(resolveEventLanguage(event, language));
        }

        return eventDTOs;
    }

    public static GetEventDTO resolveEventLanguage(Event event, String language) {
        if (Objects.equals(language.substring(0, 2), "pl")) {
            return EventDTOConverter.getEventPlDTO(event);
        } else {
            if (event.getDescriptionEN() == null || event.getDescriptionEN().isEmpty()) {
                return EventDTOConverter.getEventPlDTO(event);
            } else {
                return EventDTOConverter.getEventEnDTO(event);
            }
        }
    }

}
