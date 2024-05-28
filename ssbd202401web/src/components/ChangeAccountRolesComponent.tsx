import {
  Box,
  Typography,
  Button,
  TableContainer,
  TableHead,
  TableCell,
  TableBody,
} from "@mui/material";
import { GetPersonalAccountType } from "../types/Account";
import AccountRoleRow from "./AccountRoleRow";
import { useState } from "react";
import ModalComponent from "./ModalComponent";
import { useManageAccounts } from "../hooks/useManageAccounts";
import { AccountTypeEnum } from "../types/enums/AccountType.enum";
import DisabledByDefaultIcon from "@mui/icons-material/DisabledByDefault";
import AddBoxIcon from "@mui/icons-material/AddBox";
import { useTranslation } from "react-i18next";

export default function ChangeAccountRolesComponent({
  account,
  fetchAccount,
}: {
  account: GetPersonalAccountType | null;
  fetchAccount: () => void;
}) {
  const { t } = useTranslation();
  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const { removeRole, addRole } = useManageAccounts();

  async function callRemoveRole(role: AccountTypeEnum, id: string) {
    handleClose();
    const err = await removeRole(role, id);
    if (!err) {
      fetchAccount();
    }
  }

  async function callAddRole(role: AccountTypeEnum, id: string) {
    handleClose();
    const err = await addRole(role, id);
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
      <Typography variant="h4">{t("changeAccRole")}</Typography>
      <Typography variant="body1">{t("assignRemoveRoles")}</Typography>
      <Button
        variant="contained"
        sx={{
          marginY: 2,
        }}
        onClick={handleOpen}
      >
        {t("addRole")}
      </Button>
      <ModalComponent open={open} onClose={handleClose}>
        <>
          <Typography variant="h4">{t("addRole")}</Typography>
          <Typography variant="body1">{t("chooseOneOfRoles")}</Typography>
          <TableContainer>
            <TableHead>
              <TableCell>{t("role")}</TableCell>
              <TableCell>{t("action")}</TableCell>
            </TableHead>
            <TableBody>
              {Object.values(AccountTypeEnum).map((val, key) => {
                if (account?.roles.includes(AccountTypeEnum.PARTICIPANT))
                  return;
                if (
                  account?.roles.includes(AccountTypeEnum.MANAGER) &&
                  val !== AccountTypeEnum.ADMIN
                )
                  return;
                if (
                  account?.roles.includes(AccountTypeEnum.ADMIN) &&
                  val !== AccountTypeEnum.MANAGER
                )
                  return;
                return (
                  <AccountRoleRow
                    key={key}
                    role={t(val)}
                    callback={() => callAddRole(val, t(account?.id ?? ""))}
                    icon={<AddBoxIcon />}
                  ></AccountRoleRow>
                );
              })}
            </TableBody>
          </TableContainer>
        </>
      </ModalComponent>
      <Typography variant="body1">{t("rolesAssignedToAcc")}</Typography>
      <TableContainer>
        <TableHead>
          <TableCell>{t("role")}</TableCell>
          <TableCell>{t("action")}</TableCell>
        </TableHead>
        <TableBody>
          {account?.roles.map((val, key) => {
            return (
              <AccountRoleRow
                key={key}
                role={t(val)}
                callback={() => callRemoveRole(val, t(account.id))}
                icon={<DisabledByDefaultIcon />}
              ></AccountRoleRow>
            );
          })}
        </TableBody>
      </TableContainer>
    </Box>
  );
}
