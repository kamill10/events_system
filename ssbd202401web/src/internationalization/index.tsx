import i18next from "i18next";
import { initReactI18next } from "react-i18next";
import { LanguageType } from "../types/enums/LanguageType.enum";

// const language = {
//   POLISH: {
//     profileDetailsHeading: "Szczegóły profilu",
//     tableKey: "Klucz",
//     tableValue: "Wartość",
//     tableUsername: "Nazwa użytkownika",
//     tableRoles: "Role",
//     tableFirstName: "Imię",
//     tableLastName: "Nazwisko",
//     tableGender: "Płeć",
//     tableIsActive: "Czy aktywny",
//     tableIsVerified: "Czy zweryfikowany",
//     tableIsUnlocked: "Czy odblokowany",
//     tableLanguagePreference: "Preferencje językowe",
//     eventsLink: "Wydarzenia",
//   },
//   ENGLISH: {
//     profileDetailsHeading: "Profile details",
//     tableKey: "Key",
//     tableValue: "Value",
//     tableUsername: "Username",
//     tableRoles: "Password",
//     tableFirstName: "First name",
//     tableLastName: "Last name",
//     tableGender: "Gender",
//     tableIsActive: "Is active",
//     tableIsVerified: "Is verified",
//     tableIsUnlocked: "Is unlocked",
//     tableLanguagePreference: "Language preference",
//     eventsLink: "Events",
//   },
// };

i18next.use(initReactI18next).init({
  resources: {
    POLISH: {
      translation: {
        eventsLink: "Wydarzenia",
        tableKey: "Klucz",
        tableValue: "Wartość",
        eventsHeading: "Wydarzenia",
      },
    },
    ENGLISH: {
      translation: {
        eventsLink: "Events",
        tableKey: "Key",
        tableValue: "Value",
        eventsHeading: "Events",
      },
    },
  },
  lng: localStorage.getItem("language") ?? LanguageType.ENGLISH,
});

export default i18next;
