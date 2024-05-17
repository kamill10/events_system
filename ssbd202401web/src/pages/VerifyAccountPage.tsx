import { Button, Typography } from "@mui/material";
import { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { useAccount } from "../hooks/useAccount";
import CenteredContainerComponent from "../components/CenterdContainerComponent";
import ContainerWithShadowComponent from "../components/ContainerWithShadowComponent";
import { useTranslation } from "react-i18next";

export default function VerifyAccountPage() {
  const {t} = useTranslation();
  const [searchParams] = useSearchParams();
  const { verifyAccount } = useAccount();
  const [failed, setFailed] = useState(false);
  const [pressed, setPressed] = useState(false);

  async function verify() {
    setPressed(true);
    const key = searchParams.get("token");
    if (!key) {
      setFailed(true);
    } else {
      const err = await verifyAccount(key);
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
            {t("errorVerifyAccount")}
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
            {t("verifyAccountHeading")}
          </Typography>
          <Typography variant="h6" textAlign={"center"}>
          {t("verifyAccountButton")}
          </Typography>
          <Button
            onClick={verify}
            variant="contained"
            sx={{
              mt: 3,
              mb: 2,
            }}
          >
            {t("verifyAccountHeading")}
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
          {t("verifiedAccount")}
        </Typography>
        <Typography variant="h6" textAlign={"center"}>
          {t("youCanGoToLogin")}
          <Link to={"/login"}>{t("clickHere")}</Link>
        </Typography>
      </ContainerWithShadowComponent>
    </CenteredContainerComponent>
  );
}
