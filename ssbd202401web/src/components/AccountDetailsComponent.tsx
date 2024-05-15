import {
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import { GetDetailedAccountType } from "../types/Account";

export default function AccountDetailsComponent({
  account,
}: {
  account: GetDetailedAccountType | null;
}) {
  const data = [
    { ID: account?.id },
    { Username: account?.username },
    { Roles: account?.roles },
    { "E-mail": account?.email },
    { "First name": account?.firstName },
    { "Last name": account?.lastName },
    { Gender: account?.gender },
    { "Is active": account?.active ? "Yes" : "No" },
    { "Is verified": account?.verified ? "Yes" : "No" },
    { "Is unlocked": account?.nonLocked ? "Yes" : "No" },
    { "Language preference": account?.language },
    {
      "Last successful login": account?.lastSuccessfulLogin
        ? account.lastSuccessfulLogin
        : "Never",
    },
    {
      "Last failed login": account?.lastFailedLogin
        ? account?.lastFailedLogin
        : "Never",
    },
    {
      "Locked until": account?.lockedUntil ? account.lockedUntil : "Not locked",
    },
  ];
  return (
    <TableContainer>
      <TableHead>
        <TableCell
          sx={{
            fontWeight: "bold",
            fontSize: "18px",
          }}
        >
          Key
        </TableCell>
        <TableCell
          sx={{
            fontWeight: "bold",
            fontSize: "18px",
          }}
        >
          Value
        </TableCell>
      </TableHead>
      <TableBody>
        {data.map((_, value) => {
          return (
            <TableRow hover>
              <TableCell>{Object.keys(data[value])}</TableCell>
              <TableCell>{Object.values(data[value])}</TableCell>
            </TableRow>
          );
        })}
      </TableBody>
    </TableContainer>
  );
}
