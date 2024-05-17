import HomePage from "../pages/HomePage.tsx";
import { Pathnames } from "./Pathnames.ts";
import EventsPage from "../pages/EventsPage.tsx";
import LoginPage from "../pages/LoginPage.tsx";
import SigninPage from "../pages/SigninPage.tsx";
import ProfilePage from "../pages/ProfilePage.tsx";
import MyEventsPage from "../pages/MyEventsPage.tsx";
import EventPage from "../pages/EventPage.tsx";
import LocationsPage from "../pages/LocationsPage.tsx";
import LocationPage from "../pages/LocationPage.tsx";
import SpeakersPage from "../pages/SpeakersPage.tsx";
import SpeakerPage from "../pages/SpeakerPage.tsx";
import AccountPage from "../pages/AccountPage.tsx";
import AccountsPage from "../pages/AccountsPage.tsx";
import Logout from "../pages/Logout.tsx";
import ForgotPasswordPage from "../pages/ForgotPasswordPage.tsx";
import ResetPasswordPage from "../pages/ResetPasswordPage.tsx";
import { RouteType } from "../types/Components.ts";

export const PublicRoutes: RouteType[] = [
  {
    pathname: Pathnames.public.home,
    page: HomePage,
    name: "Home",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.public.events,
    page: EventsPage,
    name: "eventsLink",
    renderOnNavbar: true,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.public.login,
    page: LoginPage,
    name: "logInLink",
    renderOnNavbar: true,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.public.signIn,
    page: SigninPage,
    name: "registerLink",
    renderOnNavbar: true,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.public.forgotPassword,
    page: ForgotPasswordPage,
    name: "Forgot password",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
];

export const ParticipantRoutes: RouteType[] = [
  {
    pathname: Pathnames.participant.home,
    page: HomePage,
    name: "Home",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.participant.events,
    page: EventsPage,
    name: "eventsLink",
    renderOnNavbar: true,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.participant.event,
    page: EventPage,
    name: "eventsLink",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.participant.profile,
    page: ProfilePage,
    name: "profileLink",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.participant.myEvents,
    page: MyEventsPage,
    name: "myEventsLink",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.manager.logout,
    page: Logout,
    name: "logOutLink",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
];

export const ManagerRoutes: RouteType[] = [
  {
    pathname: Pathnames.manager.home,
    page: HomePage,
    name: "Home",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.manager.events,
    page: EventsPage,
    name: "eventsLink",
    renderOnNavbar: true,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.manager.event,
    page: EventPage,
    name: "Event",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.manager.locations,
    page: LocationsPage,
    name: "locationsLink",
    renderOnNavbar: true,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.manager.location,
    page: LocationPage,
    name: "Location",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.manager.speakers,
    page: SpeakersPage,
    name: "speakersLink",
    renderOnNavbar: true,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.manager.speaker,
    page: SpeakerPage,
    name: "Speaker",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.manager.profile,
    page: ProfilePage,
    name: "profileLink",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.manager.myEvents,
    page: MyEventsPage,
    name: "myEventsLink",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.manager.logout,
    page: Logout,
    name: "logOutLink",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
];

export const AdminRoutes: RouteType[] = [
  {
    pathname: Pathnames.admin.home,
    page: HomePage,
    name: "Home",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.admin.profile,
    page: ProfilePage,
    name: "profileLink",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.admin.accounts,
    page: AccountsPage,
    name: "accountsLink",
    renderOnNavbar: true,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.admin.account,
    page: AccountPage,
    name: "Account",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.manager.logout,
    page: Logout,
    name: "logOutLink",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.public.resetPassword,
    page: ResetPasswordPage,
    name: "Reset password",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
];
