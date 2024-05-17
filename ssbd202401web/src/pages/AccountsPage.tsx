import {
  Breadcrumbs,
  Button,
  Divider,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";
import { useEffect } from "react";
import { useManageAccounts } from "../hooks/useManageAccounts";
import AccountRowComponent from "../components/AccountRowComponent";
import ContainerComponent from "../components/ContainerComponent";
import { Link } from "react-router-dom";
import { Pathnames } from "../router/Pathnames";
import RefreshIcon from "@mui/icons-material/Refresh";
import { useAccount } from "../hooks/useAccount";
import { useTranslation } from "react-i18next";

export default function AccountsPage() {
  const { t } = useTranslation();
  const { accounts, getAllAccounts } = useManageAccounts();
  const { account } = useAccount();

  useEffect(() => {
    getAllAccounts();
  }, []);

  return (
    <ContainerComponent>
      <Breadcrumbs
        aria-label="breadcrumb"
        sx={{
          marginBottom: 3,
        }}
      >
        <Link
          to={Pathnames.public.home}
          style={{
            textDecoration: "none",
            color: "black",
          }}
        >
          {t("home")}
        </Link>
        <Link
          to={Pathnames.admin.accounts}
          style={{
            textDecoration: "none",
            color: "black",
            fontWeight: "bold",
          }}
        >
          {t("accounts")}
        </Link>
        <Typography color={"grey"}>{t("accountDetails")}</Typography>
      </Breadcrumbs>
      <Typography variant="h3">{t("manageAccounts")}</Typography>
      <Divider
        sx={{
          marginTop: "1rem",
          marginBottom: "2rem",
        }}
      ></Divider>
      <Button
        variant="contained"
        startIcon={<RefreshIcon></RefreshIcon>}
        onClick={getAllAccounts}
        sx={{
          margin: "1rem",
        }}
      >
        {t("refreshData")}
      </Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow hover>
              <TableCell
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                ID
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("userName")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("e-mail")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("roles")}
              </TableCell>
              <TableCell
                align="right"
                sx={{
                  fontWeight: "bold",
                  fontSize: "18px",
                }}
              >
                {t("isActive")}
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {accounts?.map((obj) => {
              if (obj.username == account?.username) return;
              return (
                <AccountRowComponent
                  key={obj.id}
                  account={obj}
                ></AccountRowComponent>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </ContainerComponent>
  );
}
