import { Backdrop, CircularProgress } from "@mui/material";
import { useLoadingScreen } from "../hooks/useLoadingScreen";

export default function LoadingScreen() {
  const { isLoggingIn, isFetching } = useLoadingScreen();

  return (
    <>
      <Backdrop
        sx={{ color: "#fff", zIndex: 2137 }}
        open={isLoggingIn || isFetching}
      >
        <CircularProgress color="primary" />
      </Backdrop>
    </>
  );
}
