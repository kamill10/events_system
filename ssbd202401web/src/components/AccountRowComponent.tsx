import { TableCell, TableRow } from "@mui/material";
import { AccountType } from "../types/Account";

export default function AccountRowComponent({account}: {account: AccountType}) {
  return (
    <TableRow hover >
      <TableCell>{account.id}</TableCell>
      <TableCell align="right">{account.username}</TableCell>
      <TableCell align="right">{account.firstName}</TableCell>
      <TableCell align="right">{account.lastName}</TableCell>
      <TableCell align="right">{account.email}</TableCell>
    </TableRow>
  )
}