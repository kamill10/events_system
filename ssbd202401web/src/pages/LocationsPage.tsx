import { Typography } from "@mui/material";
import ContainerComponent from "../components/ContainerComponent";
import { useTranslation } from "react-i18next";

export default function LocationsPage() {
  const { t } = useTranslation();
  return (
    <ContainerComponent>
      <Typography variant="h4">{t("locationsHeading")}</Typography>;
    </ContainerComponent>
  );
}
