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

export function AccountChangesHistoryComponent({
  accountChanges,
  accountTimeZone
}: {
  accountChanges: AccountChangesType[] | null,
  accountTimeZone?: string
}) {
  const { t } = useTranslation();

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
        {accountChanges?.map((change) => (
          <TableRow key={change.id}>
            <TableCell>{change.username}</TableCell>
            <TableCell>{change.email}</TableCell>
            <TableCell>
              {change.roles.map((role) => t(role)).join(", ")}
            </TableCell>
            <TableCell>{change.active ? t("yes") : t("no")}</TableCell>
            <TableCell>{change.verified ? t("yes") : t("no")}</TableCell>
            <TableCell>{change.nonLocked ? t("yes") : t("no")}</TableCell>
            <TableCell>{change.failedLoginAttempts}</TableCell>
            <TableCell>{change.lastSuccessfulLoginIp}</TableCell>
            <TableCell>
              {change.lastSuccessfulLogin
                ? parseDate(change.lastSuccessfulLogin, accountTimeZone)
                : t("never")}
            </TableCell>
            <TableCell>{change.lastFailedLoginIp}</TableCell>
            <TableCell>
              {change.lastFailedLogin
                ? parseDate(change.lastFailedLogin, accountTimeZone)
                : t("never")}
            </TableCell>
            <TableCell>
              {change.lockedUntil ? parseDate(change.lockedUntil, accountTimeZone) : ""}
            </TableCell>
            <TableCell>{change.gender}</TableCell>
            <TableCell>{change.firstName}</TableCell>
            <TableCell>{change.lastName}</TableCell>
            <TableCell>{t(change.language)}</TableCell>
            <TableCell>{change.accountTheme}</TableCell>
            <TableCell>{change.accountTimeZone}</TableCell>
            <TableCell>
              {change.createdAt ? parseDate(change.createdAt, accountTimeZone) : ""}
            </TableCell>
            <TableCell>
              {change.updatedAt ? parseDate(change.updatedAt, accountTimeZone) : t("never")}
            </TableCell>
            <TableCell>{change.createdBy}</TableCell>
            <TableCell>{change.updatedBy}</TableCell>
            <TableCell>{t(change.actionType)}</TableCell>
          </TableRow>
        ))}
      </Table>
    </TableContainer>
  );
}
