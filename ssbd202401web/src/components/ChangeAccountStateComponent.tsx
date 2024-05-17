import { Box, Typography, Switch, Stack } from "@mui/material";
import { GetPersonalAccountType } from "../types/Account";
import { useManageAccounts } from "../hooks/useManageAccounts";
import { useState } from "react";
import ConfirmChangeModal from "./ConfirmChangeModal";
import { useTranslation } from "react-i18next";

export default function ChangeAccountStateComponent({
  account,
  fetchAccount,
}: {
  account: GetPersonalAccountType | null;
  fetchAccount: () => void;
}) {
  const { t } = useTranslation();
  const { activateAccount, deactivateAccount } = useManageAccounts();
  const state = account?.active;
  const [open, setOpen] = useState(false);

  async function changeState() {
    const err = state
      ? await deactivateAccount(account?.id ?? "")
      : await activateAccount(account?.id ?? "");
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
        <Typography variant="h4">{t("changeAccStatus")}</Typography>
        <Typography variant="body1">{t("activeDeactiveAcc")}</Typography>
        <Stack direction={"row"} spacing={1} alignItems={"center"} margin={2}>
          <Typography>{t("inactive")}</Typography>
          <Switch
            onChange={() => setOpen(true)}
            checked={state}
            color="primary"
          ></Switch>
          <Typography>{t("active")}</Typography>
        </Stack>
      </Box>
      <ConfirmChangeModal
        callback={changeState}
        handleClose={() => setOpen(false)}
        open={open}
      ></ConfirmChangeModal>
    </>
  );
}
