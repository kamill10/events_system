import { Button, TableCell, TableRow } from "@mui/material";
import { AccountTypeEnum } from "../types/enums/AccountType.enum";
import { ReactNode } from "react";

export default function AccountRoleRow({
  role,
  callback,
  icon,
}: {
  role: AccountTypeEnum;
  callback: () => void;
  icon: ReactNode;
}) {
  return (
    <TableRow>
      <TableCell>{role}</TableCell>
      <TableCell>
        <Button onClick={callback}>{icon}</Button>
      </TableCell>
    </TableRow>
  );
}
