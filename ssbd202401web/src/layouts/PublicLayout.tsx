import PublicLayoutPropType from "../types/PublicLayoutPropType.ts";
import Navbar from "../components/NavbarComponent.tsx";
import { PublicRoutes } from "../router/Routes.ts";
import { Box, Container } from "@mui/material";
import React from "react";

export default function PublicLayout(props: PublicLayoutPropType) {
    const Page = props.page;
    return (
        <>
            <Navbar routes={PublicRoutes} color="#5800c4"></Navbar>
            <Container maxWidth={"xl"}>
                <Box sx={{ marginTop: 13}}>
                    <Page color="#5800c4"></Page>
                </Box>
            </Container>
        </>
    )
}