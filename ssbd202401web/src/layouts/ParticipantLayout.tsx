import PublicLayoutPropType from "../types/PublicLayoutPropType.ts";
import Navbar from "../components/NavbarComponent.tsx";
import { ParticipantRoutes } from "../router/Routes.ts";
import { Box, Container } from "@mui/material";
import React from "react";

export default function ParticipantLayout(props: PublicLayoutPropType) {
    const Page = props.page;
    return (
        <>
            <Navbar routes={ParticipantRoutes} color="#ba0c00" ></Navbar>
            <Container maxWidth={"xl"}>
                <Box sx={{ marginTop: 13}}>
                    <Page color="#ba0c00"></Page>
                </Box>
            </Container>
        </>
    )
}