import {RouteType} from "../types/RouteType.ts";
import HomePage from "../pages/HomePage.tsx";
import {Pathnames} from "./Pathnames.ts";
import EventsPage from "../pages/EventsPage.tsx";
import LoginPage from "../pages/LoginPage.tsx";
import SigninPage from "../pages/SigninPage.tsx";

export const PublicRoutes: RouteType[] = [
    {
        pathname: Pathnames.public.home,
        page: HomePage(),
        name: "Home"
    },
    {
        pathname: Pathnames.public.events,
        page: EventsPage(),
        name: "Events"
    },
    {
        pathname: Pathnames.public.login,
        page: LoginPage(),
        name: "Log in"
    },
    {
        pathname: Pathnames.public.signIn,
        page: SigninPage(),
        name: "Sign in"
    }
];