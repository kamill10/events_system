import {
  Box,
  Button,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { useEffect } from "react";
import { useTranslation } from "react-i18next";
import { AccountTypeEnum } from "../types/enums/AccountType.enum";
import RefreshIcon from "@mui/icons-material/Refresh";

export default function ProfileDetailsComponent() {
  const { t, i18n } = useTranslation();
  const { account, getMyAccount } = useAccount();
  const mapRolesToString = (rolesArray: AccountTypeEnum[]): string => {
    return rolesArray.map((role) => t(role)).join(", ");
  };

  useEffect(() => {
    getMyAccount();
    i18n.changeLanguage("ENGLISH");
  }, []);

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
    { [t("gender")]: account?.gender },
    { [t("isActive")]: account?.active ? [t("yes")] : [t("no")] },
    { [t("isVerified")]: account?.verified ? [t("yes")] : [t("no")] },
    { [t("isUnlocked")]: account?.nonLocked ? [t("yes")] : [t("no")] },
    { [t("languagePref")]: [t(account?.language ?? "")] },
  ];
  return (
    <Box
      sx={{
        marginTop: 4,
        marginLeft: 5,
      }}
    >
      <Typography variant="h4">{t("changePersonalData")}</Typography>
      <Button
        onClick={getMyAccount}
        variant="contained"
        startIcon={<RefreshIcon />}
        color="secondary"
        sx={{
          mt: 1,
          mb: 2,
          width: "fit-content",
          alignSelf: "center",
        }}
      >
        {t("refreshData")}
      </Button>
      <TableContainer>
        <TableHead>
          <TableCell
            sx={{
              fontWeight: "bold",
              fontSize: "18px",
            }}
          >
            {t("tableKey")}
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
              <TableRow hover>
                <TableCell>{Object.keys(data[value])}</TableCell>
                <TableCell>{Object.values(data[value])}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </TableContainer>
    </Box>
  );
}
