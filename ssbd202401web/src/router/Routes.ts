import {RouteType} from "../types/RouteType.ts";
import HomePage from "../pages/HomePage.tsx";
import {Pathnames} from "./Pathnames.ts";
import EventsPage from "../pages/EventsPage.tsx";
import LoginPage from "../pages/LoginPage.tsx";
import SigninPage from "../pages/SigninPage.tsx";

export const PublicRoutes: RouteType[] = [
    {
        Pathname: Pathnames.public.home,
        Page: HomePage()
    },
    {
        Pathname: Pathnames.public.events,
        Page: EventsPage()
    },
    {
        Pathname: Pathnames.public.login,
        Page: LoginPage()
    },
    {
        Pathname: Pathnames.public.signIn,
        Page: SigninPage()
    }
];