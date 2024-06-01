import { ThemeProvider } from "@emotion/react";
import {
  AdminDarkTheme,
  AdminTheme,
  ManagerDarkTheme,
  ManagerTheme,
  ParticipantDarkTheme,
  ParticipantTheme,
  PublicDarkTheme,
  PublicTheme,
} from "../themes/themes";
import { ReactNode, useEffect, useMemo, useState } from "react";
import NavbarComponent from "../components/NavbarComponent";
import FooterComponent from "../components/FooterComponent";
import { Container, Box, CssBaseline } from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { RouteType } from "../types/Components";

export default function DefaultLayout({
  children,
  routes,
}: {
  children: ReactNode;
  routes: RouteType[];
}) {
  const {
    isAdmin,
    isAuthenticated,
    isParticipant,
    isManager,
    theme,
    adminLayout,
  } = useAccount();
  const [isDarkMode, setIsDarkMode] = useState(theme === "Dark");

  useEffect(() => {
    setIsDarkMode(theme === "Dark");
  }, [theme]);

  function determineTheme() {
    if (!isAuthenticated) {
      return isDarkMode ? PublicDarkTheme : PublicTheme;
    } else if (isAuthenticated && isParticipant) {
      return isDarkMode ? ParticipantDarkTheme : ParticipantTheme;
    } else if (isAuthenticated && isAdmin && !isManager) {
      return isDarkMode ? AdminDarkTheme : AdminTheme;
    } else if (isAuthenticated && isManager && !isAdmin) {
      return isDarkMode ? ManagerDarkTheme : ManagerTheme;
    }
    return isDarkMode
      ? adminLayout
        ? AdminDarkTheme
        : ManagerDarkTheme
      : adminLayout
        ? AdminTheme
        : ManagerTheme;
  }

  const themeToRender = useMemo(() => {
    return determineTheme();
  }, [
    isAdmin,
    isAuthenticated,
    isManager,
    isParticipant,
    isDarkMode,
    adminLayout,
  ]);

  return (
    <ThemeProvider theme={themeToRender}>
      <CssBaseline></CssBaseline>
      <NavbarComponent routes={routes}></NavbarComponent>
      <Container maxWidth={"xl"}>
        <Box sx={{ marginTop: 13 }}>{children}</Box>
        <FooterComponent></FooterComponent>
      </Container>
    </ThemeProvider>
  );
}
