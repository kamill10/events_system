import { Typography, Button, Box } from "@mui/material";
import { useManageAccounts } from "../hooks/useManageAccounts";
import { GetPersonalAccountType } from "../types/Account";
import { useState } from "react";
import ConfirmChangeModal from "./ConfirmChangeModal";

export default function ChangeAccountPasswordComponent({
  account,
  fetchAccount,
}: {
  account: GetPersonalAccountType | null;
  fetchAccount: () => void;
}) {
  const [open, setOpen] = useState(false);
  const { updateAccountPassword } = useManageAccounts();
  async function resetPassword() {
    const err = await updateAccountPassword({ email: account?.email ?? "" });
    if (!err) {
      fetchAccount();
    }
  }

  return (
    <>
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
        onClick={() => setOpen(true)}
      >
        Reset password
      </Button>
    </Box>
    <ConfirmChangeModal
      open={open}
      callback={resetPassword}
      handleClose={() => setOpen(false)}
    ></ConfirmChangeModal>
    </>
  );
}
