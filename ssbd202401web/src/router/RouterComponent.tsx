import {Route, Routes} from "react-router-dom";
import {ParticipantRoutes, PublicRoutes} from "./Routes.ts";
import PublicLayout from "../layouts/PublicLayout.tsx";
import { ParticipantTheme, PublicTheme } from "../themes/themes.ts";
import { ThemeProvider } from "@mui/material";
import { useAccount } from "../hooks/useAccount.ts";
import ParticipantLayout from "../layouts/ParticipantLayout.tsx";
import { Pathnames } from "./Pathnames.ts";
import ConfirmSignInPage from "../pages/VerifyAccount.tsx";
import ResetPasswordPage from "../pages/ResetPasswordPage.tsx";

export default function RouterComponent() {
    const { isAuthenticated } = useAccount();
    return (
        <Routes>
            { !isAuthenticated && PublicRoutes.map((route, key) => {
                return <Route key={key} path={route.pathname}
                            element={
                                <ThemeProvider theme={PublicTheme}>
                                    <PublicLayout page={route.page}>
                                    </PublicLayout>
                                </ThemeProvider>}>
                </Route>
            })} 
            { isAuthenticated && ParticipantRoutes.map((route, key) => {
                return <Route key={key} path={route.pathname}
                            element={
                                <ThemeProvider theme={ParticipantTheme}>
                                    <ParticipantLayout page={route.page}>
                                    </ParticipantLayout>
                                </ThemeProvider>
                            }>

                </Route>
            })}
            <Route path={Pathnames.public.verifyAccount} element={
                <ThemeProvider theme={PublicTheme}>
                    <ConfirmSignInPage></ConfirmSignInPage>
                </ThemeProvider>
            }></Route>
            <Route path={Pathnames.public.resetPassword} element={
                <ThemeProvider theme={PublicTheme}>
                    <ResetPasswordPage></ResetPasswordPage>
                </ThemeProvider>
            }></Route>
        </Routes>
    )
}