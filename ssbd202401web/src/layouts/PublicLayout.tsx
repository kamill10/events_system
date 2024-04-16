import PublicLayoutPropType from "../types/PublicLayoutPropType.ts";
import Navbar from "../components/NavbarComponent.tsx";
import { PublicRoutes } from "../router/Routes.ts";
import { Box, Container } from "@mui/material";

export default function PublicLayout(props: PublicLayoutPropType) {
    return (
        <>
            <Navbar routes={PublicRoutes} color="#5800c4"></Navbar>
            <Container maxWidth={"xl"}>
                <Box sx={{ my: 10}}>
                    {props.page} 
                </Box>
            </Container>
        </>
    )
}