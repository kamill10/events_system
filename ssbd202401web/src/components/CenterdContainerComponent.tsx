import { Container } from "@mui/material";
import { ReactNode } from "react";

export default function CenteredContainerComponent({ children }: { children: ReactNode }) {
  return (
    <Container sx={{
      width: "100vw",
      height: "100vh",
      display: "flex",
      flexDirection: "column",
      justifyContent: "center",
      alignItems: "center",
    }}>
      {children}
    </Container>
  )
}