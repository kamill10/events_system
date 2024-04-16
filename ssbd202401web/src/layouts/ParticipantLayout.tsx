import PublicLayoutPropType from "../types/PublicLayoutPropType.ts";
import Navbar from "../components/NavbarComponent.tsx";
import { ParticipantRoutes } from "../router/Routes.ts";
import { Box, Container } from "@mui/material";

export default function ParticipantLayout(props: PublicLayoutPropType) {
    return (
        <>
            <Navbar routes={ParticipantRoutes} color="#ba0c00" ></Navbar>
            <Container maxWidth={"xl"}>
                <Box sx={{ my: 10}}>
                    {props.page} 
                </Box>
            </Container>
        </>
    )
}