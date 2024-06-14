import ContainerComponent from "../components/ContainerComponent";
import { Link, useParams } from "react-router-dom";
import { Pathnames } from "../router/Pathnames.ts";
import { Breadcrumbs, Divider, Tab, Tabs, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";
import { SyntheticEvent, useState } from "react";
import { useAccount } from "../hooks/useAccount.ts";
import { SessionDetailsComponent } from "../components/SessionDetailsComponent.tsx";
import { SessionParticipantsComponent } from "../components/SessionParticipantsComponent.tsx";
import UpdateSessionComponent from "../components/UpdateSessionComponent.tsx";

export default function SessionPage() {
  const { id, sid } = useParams();

  const { t } = useTranslation();
  const [page, setPage] = useState(0);
  const { isManager } = useAccount();

  const handleChange = (_: SyntheticEvent, newValue: number) => {
    setPage(newValue);
  };

  return (
    <ContainerComponent>
      <Breadcrumbs aria-label="breadcrumb" sx={{ marginBottom: 3 }}>
        <Link
          to={Pathnames.public.home}
          style={{ textDecoration: "none", color: "black" }}
        >
          {t("home")}
        </Link>
        <Link
          to={Pathnames.public.events}
          style={{
            textDecoration: "none",
            color: "black",
          }}
        >
          {t("eventsLink")}
        </Link>
        <Link
          to={Pathnames.public.events + "/" + id}
          style={{
            textDecoration: "none",
            color: "black",
          }}
        >
          {t("eventLink")}
        </Link>
        <Link
          to={Pathnames.public.events + "/" + id + "/sessions" + sid}
          style={{
            textDecoration: "none",
            color: "black",
            fontWeight: "bold",
          }}
        >
          {t("sessionLink")}
        </Link>
      </Breadcrumbs>
      <Typography variant="h4" component="h2">
        {t("sessionDetails")}
      </Typography>
      <Tabs value={page} onChange={handleChange}>
        <Tab label={t("sessionLink")}></Tab>
        {isManager && <Tab label={t("sessionParticipants")}></Tab>}
        {isManager && <Tab label={t("updateSession")}></Tab>}
      </Tabs>
      <Divider></Divider>
      {page === 0 && <SessionDetailsComponent></SessionDetailsComponent>}
      {page === 1 && isManager && (
        <SessionParticipantsComponent id={sid}></SessionParticipantsComponent>
      )}
      {page === 2 && isManager && (
        <UpdateSessionComponent id={sid ?? ""}></UpdateSessionComponent>
      )}
    </ContainerComponent>
  );
}
