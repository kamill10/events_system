import { TableCell, TableRow } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { GetAccountType } from "../types/Account";
import { useTranslation } from "react-i18next";
import { AccountTypeEnum } from "../types/enums/AccountType.enum";

export default function AccountRowComponent({
  account,
}: {
  account: GetAccountType;
}) {
  const navigate = useNavigate();
  const {t} = useTranslation();
  const mapRolesToString = (rolesArray: AccountTypeEnum[]): string => {
    return rolesArray.map(role => t(role)).join(", ");
  };
  return (
    <TableRow
      hover
      onClick={() => {
        navigate("/accounts/" + account.username);
      }}
    >
      <TableCell>{account.id}</TableCell>
      <TableCell align="right">{account.username}</TableCell>
      <TableCell align="right">{account.email}</TableCell>
      <TableCell align="right">{mapRolesToString(account.roles)}</TableCell>
      <TableCell
        align="right"
        sx={{
          color: account.active ? "green" : "red",
        }}
      >
        {account.active ? [t("yes")] : [t("yes")]}
      </TableCell>
    </TableRow>
  );
}
