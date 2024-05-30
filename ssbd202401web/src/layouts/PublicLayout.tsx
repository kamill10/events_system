import { PublicLayoutPropType } from "../types/Components.ts";
import Navbar from "../components/NavbarComponent.tsx";
import { PublicRoutes } from "../router/Routes.ts";
import { Box, Container } from "@mui/material";
import FooterComponent from "../components/FooterComponent.tsx";

export default function PublicLayout(props: PublicLayoutPropType) {
  const Page = props.page;
  return (
    <>
      <Navbar routes={PublicRoutes}></Navbar>
      <Container maxWidth={"xl"}>
        <Box sx={{ marginTop: 13 }}>
          <Page></Page>
        </Box>
        <FooterComponent></FooterComponent>
      </Container>
    </>
  );
}
