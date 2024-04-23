import PublicLayoutPropType from "../types/PublicLayoutPropType.ts";
import Navbar from "../components/NavbarComponent.tsx";
import { ParticipantRoutes } from "../router/Routes.ts";
import { Box, Container } from "@mui/material";

export default function ParticipantLayout(props: PublicLayoutPropType) {
    const Page = props.page;
    return (
        <>
            <Navbar routes={ParticipantRoutes}></Navbar>
            <Container maxWidth={"xl"}>
                <Box sx={{ marginTop: 13}}>
                    <Page></Page>
                </Box>
            </Container>
        </>
    )
}