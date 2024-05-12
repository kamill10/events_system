import { RouteType } from "../types/RouteType.ts";
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
import UserPage from "../pages/UserPage.tsx";
import UsersPage from "../pages/UsersPage.tsx";
import Logout from "../pages/Logout.tsx";
import ForgotPasswordPage from "../pages/ForgotPasswordPage.tsx";
import ResetPasswordPage from "../pages/ResetPasswordPage.tsx";
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
    name: "Events",
    renderOnNavbar: true,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.public.login,
    page: LoginPage,
    name: "Log in",
    renderOnNavbar: true,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.public.signIn,
    page: SigninPage,
    name: "Sign in",
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
    name: "Events",
    renderOnNavbar: true,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.participant.event,
    page: EventPage,
    name: "Event",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.participant.profile,
    page: ProfilePage,
    name: "Profile",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.participant.myEvents,
    page: MyEventsPage,
    name: "My events",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.manager.logout,
    page: Logout,
    name: "Logout",
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
    name: "Events",
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
    name: "Locations",
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
    name: "Speakers",
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
    name: "Profile",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.manager.myEvents,
    page: MyEventsPage,
    name: "My events",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.manager.logout,
    page: Logout,
    name: "Logout",
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
    name: "Profile",
    renderOnNavbar: false,
    renderOnDropdown: true,
  },
  {
    pathname: Pathnames.admin.users,
    page: UsersPage,
    name: "Users",
    renderOnNavbar: true,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.admin.user,
    page: UserPage,
    name: "User",
    renderOnNavbar: false,
    renderOnDropdown: false,
  },
  {
    pathname: Pathnames.manager.logout,
    page: Logout,
    name: "Logout",
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
