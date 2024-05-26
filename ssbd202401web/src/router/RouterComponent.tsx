import { Navigate, Route, Routes, useNavigate } from "react-router-dom";
import {
  AdminRoutes,
  ManagerRoutes,
  ParticipantRoutes,
  PublicRoutes,
} from "./Routes.ts";
import PublicLayout from "../layouts/PublicLayout.tsx";
import {
  AdminTheme,
  ManagerTheme,
  ParticipantTheme,
  PublicTheme,
} from "../themes/themes.ts";
import { ThemeProvider } from "@mui/material";
import { useAccount } from "../hooks/useAccount.ts";
import ParticipantLayout from "../layouts/ParticipantLayout.tsx";
import { Pathnames } from "./Pathnames.ts";
import ConfirmSignInPage from "../pages/VerifyAccountPage.tsx";
import ResetPasswordPage from "../pages/ResetPasswordPage.tsx";
import AdminLayout from "../layouts/AdminLayout.tsx";
import ManagerLayout from "../layouts/ManagerLayout.tsx";
import ConfirmPasswordUpdatePage from "../pages/ConfirmPasswordUpdatePage.tsx";
import { ConfirmEmailUpdatePage } from "../pages/ConfirmEmailUpdatePage.tsx";
import { useEffect } from "react";
import { setupInterceptors } from "../axios/axios.config.ts";

export default function RouterComponent() {
  const { isAuthenticated, isAdmin, isParticipant, isManager, adminLayout } =
    useAccount();
  const navigate = useNavigate();

  useEffect(() => {
    setupInterceptors(navigate);
  }, [navigate]);

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
                  <ThemeProvider theme={AdminTheme}>
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
                  <ThemeProvider theme={ManagerTheme}>
                    <ManagerLayout page={route.page}></ManagerLayout>
                  </ThemeProvider>
                }
              ></Route>
            );
          })}
        <Route
          path={Pathnames.public.verifyAccount}
          element={
            <ThemeProvider theme={PublicTheme}>
              <ConfirmSignInPage></ConfirmSignInPage>
            </ThemeProvider>
          }
        ></Route>
        <Route
          path={Pathnames.public.resetPassword}
          element={
            <ThemeProvider theme={PublicTheme}>
              <ResetPasswordPage></ResetPasswordPage>
            </ThemeProvider>
          }
        ></Route>
        <Route
          path={Pathnames.public.confirmPasswordUpdate}
          element={
            <ThemeProvider theme={PublicTheme}>
              <ConfirmPasswordUpdatePage></ConfirmPasswordUpdatePage>
            </ThemeProvider>
          }
        ></Route>
        <Route
          path={Pathnames.public.confirmEmailUpdate}
          element={
            <ThemeProvider theme={PublicTheme}>
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
    <Routes>
      {!isAuthenticated &&
        PublicRoutes.map((route, key) => {
          return (
            <Route
              key={key}
              path={route.pathname}
              element={
                <ThemeProvider theme={PublicTheme}>
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
                <ThemeProvider theme={ParticipantTheme}>
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
                <ThemeProvider theme={AdminTheme}>
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
                <ThemeProvider theme={ManagerTheme}>
                  <ManagerLayout page={route.page}></ManagerLayout>
                </ThemeProvider>
              }
            ></Route>
          );
        })}
      <Route
        path={Pathnames.public.verifyAccount}
        element={
          <ThemeProvider theme={PublicTheme}>
            <ConfirmSignInPage></ConfirmSignInPage>
          </ThemeProvider>
        }
      ></Route>
      <Route
        path={Pathnames.public.resetPassword}
        element={
          <ThemeProvider theme={PublicTheme}>
            <ResetPasswordPage></ResetPasswordPage>
          </ThemeProvider>
        }
      ></Route>
      <Route
        path={Pathnames.public.confirmPasswordUpdate}
        element={
          <ThemeProvider theme={PublicTheme}>
            <ConfirmPasswordUpdatePage></ConfirmPasswordUpdatePage>
          </ThemeProvider>
        }
      ></Route>
      <Route
        path={Pathnames.public.confirmEmailUpdate}
        element={
          <ThemeProvider theme={PublicTheme}>
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
