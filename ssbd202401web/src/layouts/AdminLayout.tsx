import PublicLayoutPropType from "../types/PublicLayoutPropType.ts";
import Navbar from "../components/NavbarComponent.tsx";
import { AdminRoutes } from "../router/Routes.ts";
import { Box, Container } from "@mui/material";
import React from "react";

export default function AdminLayout(props: PublicLayoutPropType) {
    const Page = props.page;
    return (
        <>
            <Navbar routes={AdminRoutes} color="#000000"></Navbar>
            <Container maxWidth={"xl"}>
                <Box sx={{ marginTop: 13}}>
                    <Page color="#000000"></Page>         
                </Box>
            </Container>
        </>
    )
}