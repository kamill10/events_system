import { TableCell, TableRow } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { GetAccountType } from "../types/Account";

export default function AccountRowComponent({
  account,
}: {
  account: GetAccountType;
}) {
  const navigate = useNavigate();
  return (
    <TableRow hover onClick={() => {
      navigate("/accounts/" + account.username)
    }}>
      <TableCell >{account.id}</TableCell>
      <TableCell align="right">{account.username}</TableCell>
      <TableCell align="right">{account.email}</TableCell>
      <TableCell align="right">{JSON.stringify(account.roles)}</TableCell>
      <TableCell align="right" sx={{
        color: account.active ? "green" : "red"
      }}>
        {account.active ? "Yes" : "No"}
      </TableCell>
    </TableRow>
  );
}
