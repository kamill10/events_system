import { Backdrop, CircularProgress } from "@mui/material";
import { useAccount } from "../hooks/useAccount";

export default function LoadingScreen() {
  const { isFetching, isLogging } = useAccount();
  return (
    <>
      <Backdrop
        sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={isFetching || isLogging}
      >
        <CircularProgress color="primary" />
      </Backdrop>
    </>
  );
}
