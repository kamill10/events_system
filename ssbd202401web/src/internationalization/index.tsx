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
        accountsLink: "Konta",
        eventsLink: "Wydarzenia",
        logInLink: "Zaloguj się",
        registerLink: "Rejestracja",
        profileLink: "Profil",
        logOutLink: "Wyloguj",
        myEventsLink: "Moje wydarzenia",
        locationsLink: "Lokalizacje",
        speakersLink: "Prelegenci",

        roles: "Role",
        userName: "Nazwa konta",
        firstName: "Imie",
        lastName: "Nazwisko",
        gender: "Płeć",
        isActive: "Aktywne",
        isVerified: "Zweryfikowe",
        isUnlocked: "Odblokowane",

        tableKey: "Klucz",
        tableValue: "Wartość",
        eventsHeading: "Wydarzenia",
        languagePref: "Preferencje językowe",
        lastSuccLogin: "Ostatnie poprawne logowanie",
        lastFailedLogin: "Ostatnie niepoprawne logowanie",
        lockedUntil: "Zablokowane do",
        notLocked: "Nie zablokowane",

        yes: "Tak",
        no: "Nie",
        never: "Nigdy",
        save: "zapisz",
        saveChanges: "zapisz zmiany",

        POLISH: "Polski",
        ENGLISH: "Angielski",

        ROLE_ADMIN: "Administrator",
        ROLE_PARTICIPANT: "Uczestnik",
        ROLE_MANAGER: "Zarządca",

        changePersonalData: "Zmień dane personalne",
        enterNewPersonalData: "Podaj nowe dane personalne konta!",

        NotKnown: "Nieznana",
        Male: "Mężczyzna",
        Female: "Kobieta",
        NotSpecified: "Nie podano",
      },
    },
    ENGLISH: {
      translation: {
        accountsLink: "Accounts",
        eventsLink: "Events",
        logInLink: "Log In",
        registerLink: "Sign in",
        profileLink: "Profile",
        logOutLink: "Log-out",
        myEventsLink: "My events",
        locationsLink: "Locations",
        speakersLink: "Speakers",

        roles: "Roles",
        userName: "UserName",
        firstName: "First Name",
        lastName: "Last Name",
        gender: "Gender",
        isActive: "Is Active",
        isVerified: "Is Verified",
        isUnlocked: "Is Unlocked",

        tableKey: "Key",
        tableValue: "Value",
        eventsHeading: "Events",
        languagePref: "Language preference",
        lastSuccLogin: "Last successful login",
        lastFailedLogin: "Last failed login",
        lockedUntil: "Locked until",
        notLocked: "Not Locked",

        yes: "Yes",
        no: "No",
        never: "Never",
        save: "save",
        saveChanges: "Save changes",

        POLISH: "Polish",
        ENGLISH: "English",

        ROLE_ADMIN: "Administrator",
        ROLE_PARTICIPANT: "Participant",
        ROLE_MANAGER: "Manager",

        changePersonalData: "Change personal data",
        enterNewPersonalData: "Enter account's new personal data below!",

        NotKnown: "Not Known",
        Male: "Male",
        Female: "Female",
        NotSpecified: "Not Specified",
      },
    },
  },
  lng: localStorage.getItem("language") ?? (navigator.language === "pl" ? LanguageType.POLISH : LanguageType.ENGLISH)
})
export default i18next;
