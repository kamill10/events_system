import { Typography, Button, Box } from "@mui/material";
import { useManageAccounts } from "../hooks/useManageAccounts";
import { GetPersonalAccountType } from "../types/Account";

export default function ChangeAccountPasswordComponent({
  account,
  fetchAccount,
}: {
  account: GetPersonalAccountType | null;
  fetchAccount: () => void;
}) {
  const { updateAccountPassword } = useManageAccounts();
  async function resetPassword() {
    const err = await updateAccountPassword({ email: account?.email ?? "" });
    if (!err) {
      fetchAccount();
    }
  }

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "start",
        marginY: 4,
        marginX: 8,
      }}
    >
      <Typography variant="h4">Change password</Typography>
      <Typography variant="body1">
        Click the button below to send an e-mail with password reset link!
      </Typography>
      <Button
        variant="contained"
        sx={{
          marginY: 2,
        }}
        onClick={resetPassword}
      >
        Reset password
      </Button>
    </Box>
  );
}
