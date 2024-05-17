import { Button, TableCell, TableRow } from "@mui/material";
import { AccountTypeEnum } from "../types/enums/AccountType.enum";
import { ReactNode, useState } from "react";
import ConfirmChangeModal from "./ConfirmChangeModal";

export default function AccountRoleRow({
  role,
  callback,
  icon,
}: {
  role: AccountTypeEnum;
  callback: () => void;
  icon: ReactNode;
}) {
  const [open, setOpen] = useState(false);
  return (
    <>
      <TableRow>
        <TableCell>{role}</TableCell>
        <TableCell>
          <Button onClick={() => setOpen(true)}>{icon}</Button>
        </TableCell>
      </TableRow>
      <ConfirmChangeModal
        open={open}
        handleClose={() => setOpen(false)}
        callback={callback}
      ></ConfirmChangeModal>
    </>
  );
}
