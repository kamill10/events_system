import PublicLayoutPropType from "../types/PublicLayoutPropType.ts";
import Navbar from "../components/NavbarComponent.tsx";
import { AdminRoutes } from "../router/Routes.ts";
import { Box, Container } from "@mui/material";

export default function AdminLayout(props: PublicLayoutPropType) {
    return (
        <>
            <Navbar routes={AdminRoutes} color="#000000"></Navbar>
            <Container maxWidth={"xl"}>
                <Box sx={{ my: 10}}>
                    {props.page} 
                </Box>
            </Container>
        </>
    )
}