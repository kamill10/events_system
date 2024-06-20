import { Box, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";
import { Pathnames } from "../router/Pathnames";
import { Link } from "react-router-dom";
import ContainerComponent from "../components/ContainerComponent";

export default function ErrorPage() {
  const { t } = useTranslation();

  return (
    <ContainerComponent>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <Typography variant="h2">{t("errorPageTitle")}</Typography>
        <Typography variant="body1" marginBottom={3}>
          {t("errorPageBody")}
        </Typography>
        <Link
          to={Pathnames.public.events}
          style={{
            color: "black",
            fontWeight: "bold",
          }}
        >
          {t("errorPageLink")}
        </Link>
      </Box>
    </ContainerComponent>
  );
}
