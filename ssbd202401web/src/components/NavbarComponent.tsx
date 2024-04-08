import {AppBar, Avatar, Box, IconButton, Menu, MenuItem, Slide, Toolbar, Typography, useScrollTrigger} from "@mui/material";
import NavbarPropType from "../types/NavbarPropType";
import LinkComponent from "./LinkComponent";
import HeadingComponent from "./HeadingComponent";
import { MouseEvent, useState } from "react";



export default function NavbarComponent(props: NavbarPropType) {
    const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null);

    function handleDropDownOpen(event: MouseEvent<HTMLElement>) {
        setAnchorElUser(event.currentTarget);
    }

    function handleDropDownClose() {
        setAnchorElUser(null);
    }

    const scrollTrigger = useScrollTrigger({
        target: window ? window : undefined
    })

    return (
        <div id={"navbar"}>
            <Slide appear={false} direction={"down"} in={!scrollTrigger}>
                <AppBar>
                    <Toolbar sx={{
                        backgroundColor: "#ba0c00"
                    }}>
                        <Typography
                            variant="h6"
                            noWrap
                            sx={{
                                mr: 2,
                                display: { xs: 'none', md: 'flex' },
                                fontWeight: "600",
                                fontSize: "24px"
                            }}
                        >
                            <HeadingComponent></HeadingComponent>
                        </Typography>
                        <Box sx={{
                            flexGrow: 1,
                            display: {
                                xs: "none",
                                md: "flex"
                            }
                        }}>
                            {props.routes.map((route) => {
                                return route.name !== "Home" && <MenuItem key={route.name}>
                                    <Typography textAlign={"center"}>
                                        <LinkComponent href={route.pathname} name={route.name}></LinkComponent>
                                    </Typography>
                                </MenuItem>
                            })}
                        </Box>
                        <Box sx={{ flexGrow: 0 }}>
                            <IconButton onClick={handleDropDownOpen}>
                                <Avatar></Avatar>
                            </IconButton>
                            <Menu
                                sx={{ mt: '45px' }}
                                anchorEl={anchorElUser}
                                anchorOrigin={{
                                    vertical: "top",
                                    horizontal: "right"
                                }}
                                keepMounted
                                transformOrigin={{
                                    vertical: "top",
                                    horizontal: "right"
                                }}
                                open={Boolean(anchorElUser)}
                                onClose={handleDropDownClose}
                            >
                                { props.routes.map((route) => {
                                    return (route.name === "Log in" ||
                                        route.name === "Sign in") && 
                                        <MenuItem key={route.name} onClick={handleDropDownClose}>
                                            <Typography textAlign={"center"}>
                                                <LinkComponent href={route.pathname} name={route.name}></LinkComponent>
                                            </Typography>
                                        </MenuItem>
                                }) }
                            </Menu>
                        </Box>
                    </Toolbar>
                </AppBar>
            </Slide>
        </div>
    )
}