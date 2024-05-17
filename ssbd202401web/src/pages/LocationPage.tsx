import { Typography } from "@mui/material";
import ContainerComponent from "../components/ContainerComponent";
import { useTranslation } from "react-i18next";

export default function LocationPage() {
  const { t } = useTranslation();

  return (
    <ContainerComponent>
      <Typography variant="h4">{t("locationHeading")}</Typography>;
    </ContainerComponent>
  );
}
