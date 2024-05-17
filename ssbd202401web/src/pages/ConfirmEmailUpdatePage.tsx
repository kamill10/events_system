import { Link, useSearchParams } from "react-router-dom";
import { useAccount } from "../hooks/useAccount.ts";
import { useState } from "react";
import { Button, Typography } from "@mui/material";
import CenteredContainerComponent from "../components/CenterdContainerComponent.tsx";
import ContainerWithShadowComponent from "../components/ContainerWithShadowComponent.tsx";
import { useTranslation } from "react-i18next";

export function ConfirmEmailUpdatePage() {
  const { t } = useTranslation();
  const [searchParams] = useSearchParams();
  const { confirmEmailUpdate } = useAccount();
  const [failed, setFailed] = useState(false);
  const [pressed, setPressed] = useState(false);

  async function verify() {
    setPressed(true);
    const key = searchParams.get("token");
    if (!key) {
      setFailed(true);
    } else {
      const err = await confirmEmailUpdate(key);
      if (err) {
        setFailed(true);
      }
    }
  }

  if (failed) {
    return (
      <CenteredContainerComponent>
        <ContainerWithShadowComponent>
          <Typography variant="h3" textAlign={"center"}>
            {t("somethingHappened")}
          </Typography>
          <Typography variant="h6" textAlign={"center"}>
            {t("errorConfPassword")}
          </Typography>
          <Typography variant="h6" textAlign={"center"}>
            <Link to={"/"}>{t("gotoHomePage")}</Link>
          </Typography>
        </ContainerWithShadowComponent>
      </CenteredContainerComponent>
    );
  }

  if (!pressed) {
    return (
      <CenteredContainerComponent>
        <ContainerWithShadowComponent>
          <Typography variant="h3" textAlign={"center"}>
            {t("confEmailChange")}
          </Typography>
          <Typography variant="h6" textAlign={"center"}>
            {t("clickToConfEmail")}
          </Typography>
          <Button
            onClick={verify}
            variant="contained"
            sx={{
              mt: 3,
              mb: 2,
            }}
          >
            {t("verify")}
          </Button>
          <Typography variant="h6" textAlign={"center"}>
            <Link to={"/"}>{t("gotoHomePage")}</Link>
          </Typography>
        </ContainerWithShadowComponent>
      </CenteredContainerComponent>
    );
  }

  return (
    <CenteredContainerComponent>
      <ContainerWithShadowComponent>
        <Typography variant="h3" textAlign={"center"}>
          {t("emailHasBeenChanged")}
        </Typography>
        <Typography variant="h6" textAlign={"center"}>
          <Link to={"/"}>{t("gotoHomePage")}</Link>
        </Typography>
      </ContainerWithShadowComponent>
    </CenteredContainerComponent>
  );
}
