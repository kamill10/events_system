import { AppBar, Box, Slide, Toolbar, useScrollTrigger } from "@mui/material";
import NavbarPropType from "../types/Components";
import LinkComponent from "./LinkComponent";
import HeadingComponent from "./HeadingComponent";
import { useAccount } from "../hooks/useAccount";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { useWindowWidth } from "../hooks/useWindowWidth";
import SwitchRoleComponent from "./SwitchRoleComponent";
import MenuComponent from "./MenuComponent";
import ChangeThemeButton from "./ChangeThemeButton";
import { useState } from "react";

export default function NavbarComponent(props: NavbarPropType) {
  const width = useWindowWidth();
  const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null);
  const { isAdmin, isManager } = useAccount();
  const navigate = useNavigate();
  const { t } = useTranslation();

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
          <Toolbar
            sx={{
              display: "flex",
              justifyContent: "space-between",
            }}
          >
            <HeadingComponent></HeadingComponent>
            {width >= 1020 && (
              <Box
                sx={{
                  flexGrow: 1,
                  display: "flex",
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
            )}
            <Box sx={{ flexGrow: 0, display: "flex", alignItems: "center" }}>
              {isAdmin && isManager && (
                <SwitchRoleComponent></SwitchRoleComponent>
              )}
              <ChangeThemeButton></ChangeThemeButton>
              <MenuComponent
                anchorElUser={anchorElUser}
                routes={props.routes}
                setAnchorElUser={setAnchorElUser}
              ></MenuComponent>
            </Box>
          </Toolbar>
        </AppBar>
      </Slide>
    </div>
  );
}
