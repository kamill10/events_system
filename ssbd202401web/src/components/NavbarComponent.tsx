import {
  AppBar,
  Avatar,
  Box,
  Divider,
  IconButton,
  Menu,
  Slide,
  Switch,
  Toolbar,
  Typography,
  useScrollTrigger,
} from "@mui/material";
import NavbarPropType from "../types/Components";
import LinkComponent from "./LinkComponent";
import HeadingComponent from "./HeadingComponent";
import { MouseEvent, useState } from "react";
import MenuIcon from "@mui/icons-material/Menu";
import { Brightness4 } from "@mui/icons-material";
import { Brightness7 } from "@mui/icons-material";
import { useAccount } from "../hooks/useAccount";
import { Pathnames } from "../router/Pathnames";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { useWindowWidth } from "../hooks/useWindowWidth";
import { api } from "../axios/axios.config";
import { colors } from "../themes/themes";

export default function NavbarComponent(props: NavbarPropType) {
  const width = useWindowWidth();
  const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null);
  const {
    theme,
    setMyTheme,
    setTheme,
    isAuthenticated,
    adminLayout,
    setAdminLayout,
    isAdmin,
    isManager,
    account,
  } = useAccount();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const handleDarkModeToggle = () => {
    if (theme === "Dark") {
      if (isAuthenticated) {
        setMyTheme("Light");
      } else {
        setTheme("Light");
      }
    } else {
      if (isAuthenticated) {
        setMyTheme("Dark");
      }
      setTheme("Dark");
    }
  };

  const handleSwitchClick = () => {
    // We check for layout before switching cause hook might not resolve right after click
    // Therefore we may end with previous state and sent request with wrong role
    if (!adminLayout) {
      api.switchActiveRoleToAdmin();
    } else {
      api.switchActiveRoleToManager();
    }
    setAdminLayout(!adminLayout);
    navigate(Pathnames.public.home);
  };

  function handleDropDownOpen(event: MouseEvent<HTMLElement>) {
    setAnchorElUser(event.currentTarget);
  }

  function handleDropDownClose() {
    setAnchorElUser(null);
  }

  const scrollTrigger = useScrollTrigger({
    target: window ? window : undefined,
  });

  return (
    <div id={"navbar"}>
      <Slide appear={false} direction={"down"} in={!scrollTrigger}>
        <AppBar>
          <Toolbar>
            <Typography
              variant="h6"
              noWrap
              sx={{
                mr: 2,
                display: { xs: "none", md: "flex" },
                fontWeight: "600",
                fontSize: "24px",
              }}
            >
              <HeadingComponent></HeadingComponent>
            </Typography>
            <Box
              sx={{
                flexGrow: 1,
                display: {
                  xs: "none",
                  md: "flex",
                },
              }}
            >
              {props.routes.map((route) => {
                return (
                  route.renderOnNavbar && (
                    <LinkComponent
                      key={route.pathname}
                      handleClose={handleDropDownClose}
                      href={route.pathname}
                      name={t(route.name)}
                      onClick={() => navigate(route.pathname)}
                    ></LinkComponent>
                  )
                );
              })}
            </Box>

            <Box sx={{ flexGrow: 0, display: "flex", alignItems: "center" }}>
              {width >= 900 && isAdmin && isManager && (
                <Box sx={{ display: "flex", alignItems: "center", mr: 2 }}>
                  <Typography sx={{ mr: 1, color: colors.manager.secondary }}>
                    {t("ROLE_MANAGER")}
                  </Typography>
                  <Switch
                    color="secondary"
                    onChange={handleSwitchClick}
                    checked={adminLayout}
                    sx={{ mr: 1 }}
                  />
                  <Typography sx={{ mr: 1, color: colors.admin.secondary }}>
                    {t("ROLE_ADMIN")}
                  </Typography>
                </Box>
              )}
              <Box sx={{ flexGrow: 0, display: "flex", alignItems: "center" }}>
                <IconButton onClick={handleDarkModeToggle}>
                  {theme === "Dark" ? <Brightness4 /> : <Brightness7 />}
                </IconButton>
              </Box>
              <IconButton onClick={handleDropDownOpen}>
                {width < 900 ? (
                  <MenuIcon sx={{ color: "white" }}></MenuIcon>
                ) : (
                  <Avatar></Avatar>
                )}
              </IconButton>
              <Menu
                sx={{ mt: "45px" }}
                anchorEl={anchorElUser}
                anchorOrigin={{
                  vertical: "top",
                  horizontal: "right",
                }}
                keepMounted
                transformOrigin={{
                  vertical: "top",
                  horizontal: "right",
                }}
                open={Boolean(anchorElUser)}
                onClose={handleDropDownClose}
              >
                {isAuthenticated && (
                  <Box
                    sx={{
                      marginX: 2,
                    }}
                  >
                    <Typography variant="h6">
                      {t("welcome") + ", " + account?.username}
                    </Typography>
                    {account?.roles.length === 2 ? (
                      /*Nie wiem jak to naprawić, jak się komuś chce to zapraszam*/
                      <Typography variant="body2">
                        {t("roles")}:{" "}
                        {account?.roles.reduce(
                          (previous, current) =>
                            /*@ts-ignore*/
                            t(previous) + ", " + t(current),
                        )}
                      </Typography>
                    ) : (
                      <Typography variant="body2">
                        {/*@ts-ignore*/}
                        {t("roles")}: {t(account?.roles[0])}
                      </Typography>
                    )}

                    <Divider sx={{ marginTop: 2 }}></Divider>
                  </Box>
                )}
                {props.routes.map((route) => {
                  if (width < 900) {
                    return (
                      (route.renderOnDropdown || route.renderOnNavbar) && (
                        <LinkComponent
                          key={route.pathname}
                          handleClose={handleDropDownClose}
                          href={route.pathname}
                          name={t(route.name)}
                          onClick={() => navigate(route.pathname)}
                        ></LinkComponent>
                      )
                    );
                  }
                  return (
                    route.renderOnDropdown && (
                      <LinkComponent
                        key={route.pathname}
                        handleClose={handleDropDownClose}
                        href={route.pathname}
                        name={t(route.name)}
                        onClick={() => navigate(route.pathname)}
                      ></LinkComponent>
                    )
                  );
                })}
              </Menu>
              {width < 900 && isAdmin && isManager && (
                <Box sx={{ display: "flex", alignItems: "center", mr: 2 }}>
                  <Typography sx={{ mr: 1, color: colors.manager.secondary }}>
                    {t("ROLE_MANAGER")}
                  </Typography>
                  <Switch
                    color="secondary"
                    onChange={handleSwitchClick}
                    checked={adminLayout}
                    sx={{ mr: 1 }}
                  />
                  <Typography sx={{ mr: 1, color: colors.admin.secondary }}>
                    {t("ROLE_ADMIN")}
                  </Typography>
                </Box>
              )}
            </Box>
          </Toolbar>
        </AppBar>
      </Slide>
    </div>
  );
}
