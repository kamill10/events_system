import PublicLayoutPropType from "../types/PublicLayoutPropType.ts";
import Navbar from "../components/NavbarComponent.tsx";
import { ManagerRoutes } from "../router/Routes.ts";
import { Box, Container } from "@mui/material";
import React from "react";

export default function ManagerLayout(props: PublicLayoutPropType) {
    const Page = props.page;
    return (
        <>
            <Navbar routes={ManagerRoutes} color="#001fa8"></Navbar>
            <Container maxWidth={"xl"}>
                <Box sx={{ marginTop: 13}}>
                     <Page color="#001fa8"></Page>
                </Box>
            </Container>
        </>
    )
}