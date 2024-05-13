import { Box } from "@mui/material";
import { ReactNode } from "react";

export default function ContainerWithShadowComponent({children}: {children: ReactNode}) {
  return (
    <Box
      component={"div"}
      sx={{
        boxShadow: 6,
        padding: "3rem",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      {children}
    </Box>
  )
}