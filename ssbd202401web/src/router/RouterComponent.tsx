import { Navigate, Route, Routes, useNavigate } from "react-router-dom";
import {
  AdminRoutes,
  ManagerRoutes,
  ParticipantRoutes,
  PublicRoutes,
} from "./Routes.ts";
import PublicLayout from "../layouts/PublicLayout.tsx";
import {
  AdminDarkTheme,
  AdminTheme,
  ManagerDarkTheme,
  ManagerTheme,
  ParticipantDarkTheme,
  ParticipantTheme,
  PublicDarkTheme,
  PublicTheme,
} from "../themes/themes.ts";
import { CssBaseline, ThemeProvider } from "@mui/material";
import { useAccount } from "../hooks/useAccount.ts";
import ParticipantLayout from "../layouts/ParticipantLayout.tsx";
import { Pathnames } from "./Pathnames.ts";
import ConfirmSignInPage from "../pages/VerifyAccountPage.tsx";
import ResetPasswordPage from "../pages/ResetPasswordPage.tsx";
import AdminLayout from "../layouts/AdminLayout.tsx";
import ManagerLayout from "../layouts/ManagerLayout.tsx";
import ConfirmPasswordUpdatePage from "../pages/ConfirmPasswordUpdatePage.tsx";
import { ConfirmEmailUpdatePage } from "../pages/ConfirmEmailUpdatePage.tsx";
import { useEffect, useState } from "react";
import { setupInterceptors } from "../axios/axios.config.ts";
import RedirectPage from "../pages/RedirectPage.tsx";

export default function RouterComponent() {
  const {
    theme,
    isAuthenticated,
    isAdmin,
    isParticipant,
    isManager,
    adminLayout,
  } = useAccount();
  const navigate = useNavigate();
  const [isDarkMode, setIsDarkMode] = useState(theme === "Dark");
  useEffect(() => {
    setupInterceptors(navigate);
  }, [navigate]);

  useEffect(() => {
    setIsDarkMode(theme === "Dark");
  }, [theme]);

  if (isAuthenticated && isAdmin && isManager) {
    return (
      <Routes>
        {adminLayout &&
          AdminRoutes.map((route, key) => {
            return (
              <Route
                key={key}
                path={route.pathname}
                element={
                  <ThemeProvider
                    theme={isDarkMode ? AdminDarkTheme : AdminTheme}
                  >
                    <CssBaseline></CssBaseline>
                    <AdminLayout page={route.page}></AdminLayout>
                  </ThemeProvider>
                }
              ></Route>
            );
          })}
        {!adminLayout &&
          ManagerRoutes.map((route, key) => {
            return (
              <Route
                key={key}
                path={route.pathname}
                element={
                  <ThemeProvider
                    theme={isDarkMode ? ManagerDarkTheme : ManagerTheme}
                  >
                    <CssBaseline></CssBaseline>
                    <ManagerLayout page={route.page}></ManagerLayout>
                  </ThemeProvider>
                }
              ></Route>
            );
          })}
        <Route
          path={Pathnames.public.verifyAccount}
          element={
            <ThemeProvider theme={isDarkMode ? PublicDarkTheme : PublicTheme}>
              <CssBaseline></CssBaseline>
              <ConfirmSignInPage></ConfirmSignInPage>
            </ThemeProvider>
          }
        ></Route>
        <Route
          path={Pathnames.public.resetPassword}
          element={
            <ThemeProvider theme={isDarkMode ? PublicDarkTheme : PublicTheme}>
              <CssBaseline></CssBaseline>
              <ResetPasswordPage></ResetPasswordPage>
            </ThemeProvider>
          }
        ></Route>
        <Route
          path={Pathnames.public.confirmPasswordUpdate}
          element={
            <ThemeProvider theme={isDarkMode ? PublicDarkTheme : PublicTheme}>
              <CssBaseline></CssBaseline>
              <ConfirmPasswordUpdatePage></ConfirmPasswordUpdatePage>
            </ThemeProvider>
          }
        ></Route>
        <Route
          path={Pathnames.public.confirmEmailUpdate}
          element={
            <ThemeProvider theme={isDarkMode ? PublicDarkTheme : PublicTheme}>
              <CssBaseline></CssBaseline>
              <ConfirmEmailUpdatePage></ConfirmEmailUpdatePage>
            </ThemeProvider>
          }
        ></Route>
        <Route
          path="*"
          element={<Navigate to={Pathnames.public.home}></Navigate>}
        ></Route>
      </Routes>
    );
  }

  return (
    <>
      <Routes>
        {!isAuthenticated &&
          PublicRoutes.map((route, key) => {
            return (
              <Route
                key={key}
                path={route.pathname}
                element={
                  <ThemeProvider
                    theme={isDarkMode ? PublicDarkTheme : PublicTheme}
                  >
                    <CssBaseline></CssBaseline>
                    <PublicLayout page={route.page}></PublicLayout>
                  </ThemeProvider>
                }
              ></Route>
            );
          })}
        {isAuthenticated &&
          isParticipant &&
          ParticipantRoutes.map((route, key) => {
            return (
              <Route
                key={key}
                path={route.pathname}
                element={
                  <ThemeProvider
                    theme={isDarkMode ? ParticipantDarkTheme : ParticipantTheme}
                  >
                    <CssBaseline></CssBaseline>
                    <ParticipantLayout page={route.page}></ParticipantLayout>
                  </ThemeProvider>
                }
              ></Route>
            );
          })}
        {isAuthenticated &&
          isAdmin &&
          AdminRoutes.map((route, key) => {
            return (
              <Route
                key={key}
                path={route.pathname}
                element={
                  <ThemeProvider
                    theme={isDarkMode ? AdminDarkTheme : AdminTheme}
                  >
                    <CssBaseline></CssBaseline>
                    <AdminLayout page={route.page}></AdminLayout>
                  </ThemeProvider>
                }
              ></Route>
            );
          })}
        {isAuthenticated &&
          isManager &&
          ManagerRoutes.map((route, key) => {
            return (
              <Route
                key={key}
                path={route.pathname}
                element={
                  <ThemeProvider
                    theme={isDarkMode ? ManagerDarkTheme : ManagerTheme}
                  >
                    <CssBaseline></CssBaseline>
                    <ManagerLayout page={route.page}></ManagerLayout>
                  </ThemeProvider>
                }
              ></Route>
            );
          })}
        <Route
          path={Pathnames.public.verifyAccount}
          element={
            <ThemeProvider theme={isDarkMode ? PublicDarkTheme : PublicTheme}>
              <CssBaseline></CssBaseline>
              <ConfirmSignInPage></ConfirmSignInPage>
            </ThemeProvider>
          }
        ></Route>
        <Route
          path={Pathnames.public.resetPassword}
          element={
            <ThemeProvider theme={isDarkMode ? PublicDarkTheme : PublicTheme}>
              <CssBaseline></CssBaseline>
              <ResetPasswordPage></ResetPasswordPage>
            </ThemeProvider>
          }
        ></Route>
        <Route
          path={Pathnames.public.confirmPasswordUpdate}
          element={
            <ThemeProvider theme={isDarkMode ? PublicDarkTheme : PublicTheme}>
              <CssBaseline></CssBaseline>
              <ConfirmPasswordUpdatePage></ConfirmPasswordUpdatePage>
            </ThemeProvider>
          }
        ></Route>
        <Route
          path={Pathnames.public.confirmEmailUpdate}
          element={
            <ThemeProvider theme={isDarkMode ? PublicDarkTheme : PublicTheme}>
              <CssBaseline></CssBaseline>
              <ConfirmEmailUpdatePage></ConfirmEmailUpdatePage>
            </ThemeProvider>
          }
        ></Route>
        <Route path="*" element={<RedirectPage></RedirectPage>}></Route>
      </Routes>
    </>
  );
}
