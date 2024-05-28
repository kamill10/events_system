import {
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import { GetDetailedAccountType } from "../types/Account";
import { useTranslation } from "react-i18next";
import { AccountTypeEnum } from "../types/enums/AccountType.enum";
import { useAccount } from "../hooks/useAccount";
import parseDate from "../validation/parseDate";

export default function AccountDetailsComponent({
  account,
}: {
  account: GetDetailedAccountType | null;
}) {
  const { t } = useTranslation();
  const myAccount = useAccount();
  const mapRolesToString = (rolesArray: AccountTypeEnum[]): string => {
    return rolesArray.map((role) => t(role)).join(", ");
  };
  const data = [
    { ID: account?.id },
    { [t("userName")]: account?.username },
    {
      [t("roles")]: mapRolesToString(
        account?.roles ?? [AccountTypeEnum.PARTICIPANT],
      ),
    },
    { "E-mail": account?.email },
    { [t("firstName")]: account?.firstName },
    { [t("lastName")]: account?.lastName },
    { [t("gender")]: [t(account?.gender.toString() ?? "0")] },
    { [t("isActive")]: account?.active ? [t("yes")] : [t("no")] },
    { [t("isVerified")]: account?.verified ? [t("yes")] : [t("no")] },
    { [t("isUnlocked")]: account?.nonLocked ? [t("yes")] : [t("no")] },
    { [t("languagePref")]: [t(account?.language ?? "")] },
    {
      [t("lastSuccLogin")]: account?.lastSuccessfulLogin
        ? parseDate(
            account.lastSuccessfulLogin,
            myAccount.account?.accountTimeZone,
          )
        : [t("never")],
    },
    {
      [t("lastFailedLogin")]: account?.lastFailedLogin
        ? parseDate(account.lastFailedLogin, myAccount.account?.accountTimeZone)
        : [t("never")],
    },
    {
      [t("lockedUntil")]: account?.lockedUntil
        ? parseDate(account.lockedUntil, myAccount.account?.accountTimeZone)
        : [t("notLocked")],
    },
    {
      [t("timeZone")]: account?.accountTimeZone ?? t("notSpecified"),
    },
    {
      [t("theme")]: account?.accountTheme ?? t("notSpecified"),
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
          {[t("tableKey")]}
        </TableCell>
        <TableCell
          sx={{
            fontWeight: "bold",
            fontSize: "18px",
          }}
        >
          {[t("tableValue")]}
        </TableCell>
      </TableHead>
      <TableBody>
        {data.map((_, value) => {
          return (
            <TableRow hover key={value}>
              <TableCell>{Object.keys(data[value])}</TableCell>
              <TableCell>{Object.values(data[value])}</TableCell>
            </TableRow>
          );
        })}
      </TableBody>
    </TableContainer>
  );
}
