import { Route, Routes, useNavigate } from "react-router-dom";
import {
  AdminRoutes,
  AuthRoutes,
  ManagerRoutes,
  ParticipantRoutes,
  PublicRoutes,
  UnauthRoutes,
} from "./Routes.ts";
import { useAccount } from "../hooks/useAccount.ts";
import { useEffect, useMemo } from "react";
import { setupInterceptors } from "../axios/axios.config.ts";
import { RouteType } from "../types/Components.ts";
import DefaultLayout from "../layouts/DefaultLayout.tsx";
import RedirectPage from "../pages/RedirectPage.tsx";

export default function RouterComponent() {
  const { isAuthenticated, isAdmin, isParticipant, isManager } = useAccount();
  const navigate = useNavigate();

  useEffect(() => {
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
    if (isAuthenticated && isAdmin) {
      routesToRender = routesToRender.concat(AdminRoutes);
    }
    if (isAuthenticated && isManager) {
      routesToRender = routesToRender.concat(ManagerRoutes);
    }
    return routesToRender;
  }

  const routesToRender = useMemo(() => {
    return determineRoutes();
  }, [isAdmin, isAuthenticated, isManager, isParticipant]);

  return (
    <Routes>
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
