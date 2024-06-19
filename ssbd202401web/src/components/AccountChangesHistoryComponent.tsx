import React from "react";
import {
  Paper,
  Table,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import { useTranslation } from "react-i18next";
import { AccountChangesType } from "../types/AccountChanges.ts";
import parseDate from "../validation/parseDate.ts";
import { useAccount } from "../hooks/useAccount.ts";

export function AccountChangesHistoryComponent({
  accountChanges,
}: {
  accountChanges: AccountChangesType[] | null;
}) {
  const { t } = useTranslation();
  const { account } = useAccount();

  const renderCell = (
    current: string | number | boolean,
    previous: string | number | boolean,
    renderValue: {
      (value: any): any;
      (value: any): any;
      (value: any): any;
      (value: any): string;
      (value: any): string;
      (value: any): string;
      (value: any): any;
      (value: any): any;
      (value: any): string;
      (value: any): any;
      (value: any): string;
      (value: any): string;
      (value: any): any;
      (value: any): any;
      (value: any): any;
      (value: any): string;
      (value: any): string;
      (value: any): string;
      (value: any): string;
      (value: any): string;
      (value: any): any;
      (value: any): any;
      (value: any): string;
      (
        arg0: any,
      ):
        | string
        | number
        | boolean
        | React.ReactElement<any, string | React.JSXElementConstructor<any>>
        | Iterable<React.ReactNode>
        | React.ReactPortal
        | null
        | undefined;
    },
  ) => {
    const isChanged = current !== previous;
    return (
      <TableCell
        style={{
          fontWeight: isChanged ? "bold" : "normal",
          fontStyle: isChanged ? "italic" : "normal",
          textDecoration: isChanged ? "underline" : "none",
        }}
      >
        {renderValue(current)}
      </TableCell>
    );
  };

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>{t("userName")}</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>{t("roles")}</TableCell>
            <TableCell>{t("isActive")}</TableCell>
            <TableCell>{t("isVerified")}</TableCell>
            <TableCell>{t("isUnlocked")}</TableCell>
            <TableCell>{t("failedLoginAttempts")}</TableCell>
            <TableCell>{t("lastSuccLoginIP")}</TableCell>
            <TableCell>{t("lastSuccLogin")}</TableCell>
            <TableCell>{t("lastFailedLoginIP")}</TableCell>
            <TableCell>{t("lastFailedLogin")}</TableCell>
            <TableCell>{t("lockedUntil")}</TableCell>
            <TableCell>{t("gender")}</TableCell>
            <TableCell>{t("firstName")}</TableCell>
            <TableCell>{t("lastName")}</TableCell>
            <TableCell>{t("languagePref")}</TableCell>
            <TableCell>{t("theme")}</TableCell>
            <TableCell>{t("timeZone")}</TableCell>
            <TableCell>{t("createdAt")}</TableCell>
            <TableCell>{t("updatedAt")}</TableCell>
            <TableCell>{t("createdBy")}</TableCell>
            <TableCell>{t("updatedBy")}</TableCell>
            <TableCell>{t("actionType")}</TableCell>
          </TableRow>
        </TableHead>
        <tbody>
          {accountChanges?.map((change, index) => {
            const previousChange = accountChanges[index - 1] || {};
            return (
              <TableRow key={change.id}>
                {renderCell(
                  change.username,
                  previousChange.username,
                  (value: any) => value,
                )}
                {renderCell(
                  change.email,
                  previousChange.email,
                  (value: any) => value,
                )}
                {renderCell(
                  change.roles.join(", "),
                  previousChange.roles?.join(", "),
                  (value: any) => value,
                )}
                {renderCell(
                  change.active,
                  previousChange.active,
                  (value: any) => (value ? t("yes") : t("no")),
                )}
                {renderCell(
                  change.verified,
                  previousChange.verified,
                  (value: any) => (value ? t("yes") : t("no")),
                )}
                {renderCell(
                  change.nonLocked,
                  previousChange.nonLocked,
                  (value: any) => (value ? t("yes") : t("no")),
                )}
                {renderCell(
                  change.failedLoginAttempts,
                  previousChange.failedLoginAttempts,
                  (value: any) => value,
                )}
                {renderCell(
                  change.lastSuccessfulLoginIp,
                  previousChange.lastSuccessfulLoginIp,
                  (value: any) => value,
                )}
                {renderCell(
                  change.lastSuccessfulLogin,
                  previousChange.lastSuccessfulLogin,
                  (value: string) =>
                    value
                      ? parseDate(value, account?.accountTimeZone)
                      : t("never"),
                )}
                {renderCell(
                  change.lastFailedLoginIp,
                  previousChange.lastFailedLoginIp,
                  (value: any) => value,
                )}
                {renderCell(
                  change.lastFailedLogin,
                  previousChange.lastFailedLogin,
                  (value: string) =>
                    value
                      ? parseDate(value, account?.accountTimeZone)
                      : t("never"),
                )}
                {renderCell(
                  change.lockedUntil,
                  previousChange.lockedUntil,
                  (value: string) =>
                    value ? parseDate(value, account?.accountTimeZone) : "",
                )}
                {renderCell(
                  change.gender,
                  previousChange.gender,
                  (value: any) => value,
                )}
                {renderCell(
                  change.firstName,
                  previousChange.firstName,
                  (value: any) => value,
                )}
                {renderCell(
                  change.lastName,
                  previousChange.lastName,
                  (value: any) => value,
                )}
                {renderCell(
                  change.language,
                  previousChange.language,
                  (value: any | string | string[]) => t(value),
                )}
                {renderCell(
                  change.accountTheme,
                  previousChange.accountTheme,
                  (value: any | string | string[]) => t(value),
                )}
                {renderCell(
                  change.accountTimeZone,
                  previousChange.accountTimeZone,
                  (value: any | string | string[]) => t(value),
                )}
                {renderCell(
                  change.createdAt,
                  previousChange.createdAt,
                  (value: string) =>
                    value ? parseDate(value, account?.accountTimeZone) : "",
                )}
                {renderCell(
                  change.updatedAt,
                  previousChange.updatedAt,
                  (value: string) =>
                    value
                      ? parseDate(value, account?.accountTimeZone)
                      : t("never"),
                )}
                {renderCell(
                  change.createdBy,
                  previousChange.createdBy,
                  (value: any) => value,
                )}
                {renderCell(
                  change.updatedBy,
                  previousChange.updatedBy,
                  (value: any) => value,
                )}
                {renderCell(
                  change.actionType,
                  previousChange.actionType,
                  (value: any | string | string[]) => t(value),
                )}
              </TableRow>
            );
          })}
        </tbody>
      </Table>
    </TableContainer>
  );
}
