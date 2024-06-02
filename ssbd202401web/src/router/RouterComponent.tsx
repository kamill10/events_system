import { Route, Routes, useNavigate } from "react-router-dom";
import {
  AdminRoutes,
  AuthRoutes,
  ManagerRoutes,
  ParticipantRoutes,
  PublicRoutes,
  SinglePageRoutes,
  UnauthRoutes,
} from "./Routes.ts";
import { useAccount } from "../hooks/useAccount.ts";
import { useLayoutEffect, useMemo } from "react";
import { setupInterceptors } from "../axios/axios.config.ts";
import { RouteType } from "../types/Components.ts";
import DefaultLayout from "../layouts/DefaultLayout.tsx";
import RedirectPage from "../pages/RedirectPage.tsx";
import { CssBaseline } from "@mui/material";

export default function RouterComponent() {
  const { isAuthenticated, isAdmin, isParticipant, isManager, adminLayout } =
    useAccount();
  const navigate = useNavigate();

  useLayoutEffect(() => {
    setupInterceptors(navigate);
  }, []);

  function determineRoutes() {
    let routesToRender: RouteType[] = PublicRoutes;
    if (!isAuthenticated) {
      routesToRender = routesToRender.concat(UnauthRoutes);
    }
    if (isAuthenticated) {
      routesToRender = routesToRender.concat(AuthRoutes);
    }
    if (isAuthenticated && isParticipant) {
      routesToRender = routesToRender.concat(ParticipantRoutes);
    }
    if (
      (isAuthenticated && isAdmin && !isManager) ||
      (isAuthenticated && isAdmin && isManager && adminLayout)
    ) {
      routesToRender = routesToRender.concat(AdminRoutes);
    }
    if (
      (isAuthenticated && isManager && !isAdmin) ||
      (isAuthenticated && isAdmin && isManager && !adminLayout)
    ) {
      routesToRender = routesToRender.concat(ManagerRoutes);
    }
    return routesToRender;
  }

  const routesToRender = useMemo(() => {
    return determineRoutes();
  }, [isAdmin, isAuthenticated, isManager, isParticipant, adminLayout]);

  return (
    <Routes>
      {SinglePageRoutes.map((route) => {
        const Page = route.page;
        return (
          <Route
            key={route.name}
            path={route.pathname}
            element={
              <>
                <CssBaseline></CssBaseline>
                <Page></Page>
              </>
            }
          ></Route>
        );
      })}
      {routesToRender.map((route) => {
        const Page = route.page;
        return (
          <Route
            key={route.name}
            path={route.pathname}
            element={
              <DefaultLayout routes={routesToRender}>
                <Page></Page>
              </DefaultLayout>
            }
          />
        );
      })}
      <Route path={"*"} element={<RedirectPage></RedirectPage>}></Route>
    </Routes>
  );
}
