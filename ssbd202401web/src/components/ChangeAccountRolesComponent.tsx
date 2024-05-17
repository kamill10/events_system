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
import AddBoxIcon from '@mui/icons-material/AddBox';

export default function ChangeAccountRolesComponent({
  account,
  fetchAccount,
}: {
  account: GetPersonalAccountType | null;
  fetchAccount: () => void;
}) {
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
      <Typography variant="h4">Change account's roles</Typography>
      <Typography variant="body1">Assing or remove account's roles!</Typography>
      <Button
        variant="contained"
        sx={{
          marginY: 2,
        }}
        onClick={handleOpen}
      >
        Add role
      </Button>
      <ModalComponent open={open} onClose={handleClose}>
        <>
          <Typography variant="h4">Add role!</Typography>
          <Typography variant="body1">
            Choose one of the available roles to add!
          </Typography>
          <TableContainer>
            <TableHead>
              <TableCell>Role</TableCell>
              <TableCell>Action</TableCell>
            </TableHead>
            <TableBody>
              {Object.values(AccountTypeEnum).map((val, key) => {
                console.log(val);
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
                    role={val}
                    callback={() => callAddRole(val, account?.id ?? "")}
                    icon={<AddBoxIcon />}
                  ></AccountRoleRow>
                );
              })}
            </TableBody>
          </TableContainer>
        </>
      </ModalComponent>
      <Typography variant="body1">Roles assigned to the account</Typography>
      <TableContainer>
        <TableHead>
          <TableCell>Role</TableCell>
          <TableCell>Action</TableCell>
        </TableHead>
        <TableBody>
          {account?.roles.map((val, key) => {
            return (
              <AccountRoleRow
                key={key}
                role={val}
                callback={() => callRemoveRole(val, account.id)}
                icon={<DisabledByDefaultIcon />}
              ></AccountRoleRow>
            );
          })}
        </TableBody>
      </TableContainer>
    </Box>
  );
}
