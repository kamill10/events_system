import { Box, Paper } from "@mui/material";
import { ReactNode } from "react";

export default function ContainerComponent({
  children,
}: {
  children: ReactNode;
}) {
  return (
    <Box
      component={Paper}
      elevation={6}
      minHeight={"80vh"}
      maxHeight={"80vh"}
      overflow={"auto"}
      padding={2}
    >
      {children}
    </Box>
  );
}
