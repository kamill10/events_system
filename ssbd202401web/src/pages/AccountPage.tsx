import {
  Breadcrumbs,
  Button,
  Divider,
  Tab,
  Tabs,
  Typography,
} from "@mui/material";
import ContainerComponent from "../components/ContainerComponent";
import { Pathnames } from "../router/Pathnames";
import { Link, useParams } from "react-router-dom";
import { SyntheticEvent, useEffect, useState } from "react";
import { GetDetailedAccountType } from "../types/Account";
import { useManageAccounts } from "../hooks/useManageAccounts";
import useNotification from "../hooks/useNotification";
import AccountDetailsComponent from "../components/AccountDetailsComponent";
import ChangeAccountDetails from "../components/ChangeAccountDetails";
import RefreshIcon from "@mui/icons-material/Refresh";
import { useTranslation } from "react-i18next";

export default function AccountPage() {
  const { t } = useTranslation();
  const [account, setAccount] = useState<GetDetailedAccountType | null>(null);
  const sendNotification = useNotification();
  const { username } = useParams();
  const { getAccountByUsername } = useManageAccounts();

  const [page, setPage] = useState(0);

  const handleChange = (_: SyntheticEvent, newValue: number) => {
    setPage(newValue);
  };

  async function fetchAccount() {
    if (username) {
      setAccount(await getAccountByUsername(username));
    } else {
      sendNotification({
        type: "error",
        description: "Username in URL is not present",
      });
    }
  }

  useEffect(() => {
    fetchAccount();
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
          }}
        >
          {t("accounts")}
        </Link>
        <Typography
          color="text.primary"
          sx={{
            fontWeight: "bold",
          }}
        >
          {t("accountDetails")}
        </Typography>
      </Breadcrumbs>
      <Typography variant="h3">
        {t("accountDetails")}: {username}
      </Typography>
      <Button
        variant="contained"
        onClick={fetchAccount}
        startIcon={<RefreshIcon />}
        sx={{
          margin: 2,
        }}
      >
        {t("refreshData")}
      </Button>
      <Tabs value={page} onChange={handleChange}>
        <Tab label={t("profileDetails")}></Tab>
        <Tab label={t("changeProfileDetails")}></Tab>
      </Tabs>
      <Divider></Divider>
      {page == 0 && (
        <AccountDetailsComponent account={account}></AccountDetailsComponent>
      )}
      {page == 1 && (
        <ChangeAccountDetails
          account={account}
          fetchAccount={fetchAccount}
        ></ChangeAccountDetails>
      )}
    </ContainerComponent>
  );
}
