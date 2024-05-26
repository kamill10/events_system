import {
  AppBar,
  Avatar,
  Box,
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
import { useAccount } from "../hooks/useAccount";
import { Pathnames } from "../router/Pathnames";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { useWindowWidth } from "../hooks/useWindowWidth";
import { api } from "../axios/axios.config";

export default function NavbarComponent(props: NavbarPropType) {
  const width = useWindowWidth();
  const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null);
  const { adminLayout, setAdminLayout, isAdmin, isManager } = useAccount();
  const navigate = useNavigate();

  const { t } = useTranslation();

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
              {isAdmin && isManager && (
                <Box sx={{ display: "flex", alignItems: "center", mr: 2 }}>
                  <Typography sx={{ mr: 1 }}>{t("ROLE_MANAGER")}</Typography>
                  <Switch
                    color="secondary"
                    onChange={handleSwitchClick}
                    checked={adminLayout}
                    sx={{ mr: 1 }}
                  />
                  <Typography>{t("ROLE_ADMIN")}</Typography>
                </Box>
              )}
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
                {props.routes.map((route) => {
                  if (width < 900) {
                    return (
                      (route.renderOnDropdown || route.renderOnNavbar) && (
                        <LinkComponent
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
                        handleClose={handleDropDownClose}
                        href={route.pathname}
                        name={t(route.name)}
                        onClick={() => navigate(route.pathname)}
                      ></LinkComponent>
                    )
                  );
                })}
              </Menu>
            </Box>
          </Toolbar>
        </AppBar>
      </Slide>
    </div>
  );
}
