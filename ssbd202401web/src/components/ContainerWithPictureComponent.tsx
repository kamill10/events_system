import { CssBaseline, Grid, Paper } from "@mui/material";
import { ReactNode } from "react";

export default function ContainerWithPictureComponent({
  children,
}: {
  children: ReactNode;
}) {
  return (
    <Grid container component="main" sx={{ height: "85vh" }}>
      <CssBaseline />
      <Grid
        item
        xs={false}
        sm={4}
        md={7}
        sx={{
          backgroundImage: "url(https://source.unsplash.com/random?wallpapers)",
          backgroundRepeat: "no-repeat",
          backgroundSize: "cover",
          backgroundPosition: "center",
        }}
      />
      <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
        {children}
      </Grid>
    </Grid>
  );
}
