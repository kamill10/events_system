import { Typography } from "@mui/material";
import { useAccount } from "../hooks/useAccount";
import { useEffect } from "react";
import { useTranslation } from "react-i18next";
import ContainerComponent from "../components/ContainerComponent";

export default function HomePage() {
  const { getMyAccount, isAuthenticated } = useAccount();
  const { t } = useTranslation();

  useEffect(() => {
    if (isAuthenticated) getMyAccount();
  }, []);

  return (
    <ContainerComponent>
      <Typography variant={"h4"}>{t("home")}</Typography>
    </ContainerComponent>
  );
}
