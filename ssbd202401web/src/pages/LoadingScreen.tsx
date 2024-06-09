import { Backdrop, CircularProgress } from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { useManageAccounts } from "../hooks/useManageAccounts";
import { useEvents } from "../hooks/useEvents";

export default function LoadingScreen() {
  const accountState = useAccount();
  const manageAccountsState = useManageAccounts();
  const eventsState = useEvents();
  return (
    <>
      <Backdrop
        sx={{ color: "#fff", zIndex: 2137 }}
        open={
          accountState.isFetching ||
          accountState.isLogging ||
          manageAccountsState.isFetching ||
          eventsState.isFetching
        }
      >
        <CircularProgress color="primary" />
      </Backdrop>
    </>
  );
}
