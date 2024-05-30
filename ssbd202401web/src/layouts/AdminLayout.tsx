import Navbar from "../components/NavbarComponent.tsx";
import { AdminRoutes } from "../router/Routes.ts";
import { Box, Container } from "@mui/material";
import { PublicLayoutPropType } from "../types/Components.ts";
import FooterComponent from "../components/FooterComponent.tsx";

export default function AdminLayout(props: PublicLayoutPropType) {
  const Page = props.page;
  return (
    <>
      <Navbar routes={AdminRoutes}></Navbar>
      <Container maxWidth={"xl"}>
        <Box sx={{ marginTop: 13 }}>
          <Page></Page>
        </Box>
        <FooterComponent></FooterComponent>
      </Container>
    </>
  );
}
