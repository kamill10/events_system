import { Backdrop, CircularProgress } from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { useManageAccounts } from "../hooks/useManageAccounts";

export default function LoadingScreen() {
  const accountState = useAccount();
  const manageAccountsState = useManageAccounts();
  return (
    <>
      <Backdrop
        sx={{ color: "#fff", zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={
          accountState.isFetching ||
          accountState.isLogging ||
          manageAccountsState.isFetching
        }
      >
        <CircularProgress color="primary" />
      </Backdrop>
    </>
  );
}
