import PublicLayoutPropType from "../types/PublicLayoutPropType.ts";
import Navbar from "../components/NavbarComponent.tsx";
import { ManagerRoutes } from "../router/Routes.ts";
import { Box, Container } from "@mui/material";

export default function ManagerLayout(props: PublicLayoutPropType) {
    return (
        <>
            <Navbar routes={ManagerRoutes} color="#001fa8"></Navbar>
            <Container maxWidth={"xl"}>
                <Box sx={{ my: 10}}>
                    {props.page} 
                </Box>
            </Container>
        </>
    )
}