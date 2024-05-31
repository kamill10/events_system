import { IconButton, Menu } from "@mui/material";
import { t } from "i18next";
import AccountHeading from "./AccountHeading";
import LinkComponent from "./LinkComponent";
import MenuIcon from "@mui/icons-material/Menu";
import { MenuProps } from "../types/Components";
import { MouseEvent } from "react";
import { useAccount } from "../hooks/useAccount";
import { useWindowWidth } from "../hooks/useWindowWidth";
import { useNavigate } from "react-router-dom";

export default function MenuComponent({
  routes,
  anchorElUser,
  setAnchorElUser,
}: MenuProps) {
  const navigate = useNavigate();
  const width = useWindowWidth();
  const { isAuthenticated } = useAccount();

  function handleDropDownClose() {
    setAnchorElUser(null);
  }

  function handleDropDownOpen(event: MouseEvent<HTMLElement>) {
    setAnchorElUser(event.currentTarget);
  }

  return (
    <>
      <IconButton onClick={handleDropDownOpen}>
        <MenuIcon sx={{ color: "white", fontSize: 24 }}></MenuIcon>
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
        {isAuthenticated && <AccountHeading></AccountHeading>}
        {routes.map((route) => {
          if (width < 1020) {
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
    </>
  );
}
