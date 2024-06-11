import { Breadcrumbs, Divider, Tab, Tabs } from "@mui/material";
import ContainerComponent from "../components/ContainerComponent";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { Pathnames } from "../router/Pathnames";
import { useState, SyntheticEvent } from "react";
import TicketHistoryComponent from "../components/TicketHistoryComponent";
import { MyTicketsComponent } from "../components/MyTicketsComponent.tsx";

export default function MyTicketsPage() {
  const [page, setPage] = useState(0);
  const { t } = useTranslation();

  const handleChange = (_: SyntheticEvent, newValue: number) => {
    setPage(newValue);
  };

  return (
    <ContainerComponent>
      <Breadcrumbs sx={{ marginBottom: 3 }}>
        <Link
          to={Pathnames.public.home}
          style={{ textDecoration: "none", color: "black" }}
        >
          {t("home")}
        </Link>
        <Link
          to={Pathnames.auth.profile}
          style={{
            textDecoration: "none",
            color: "black",
          }}
        >
          {t("profileLink")}
        </Link>
        <Link
          to={Pathnames.participant.myTickets}
          style={{
            textDecoration: "none",
            color: "black",
            fontWeight: "bold",
          }}
        >
          {t("myTicketsLink")}
        </Link>
      </Breadcrumbs>
      <Tabs value={page} onChange={handleChange}>
        <Tab label={t("upcomingSessions")}></Tab>
        <Tab label={t("historicalSessions")}></Tab>
          <Tab label={t("historicalEvents")}></Tab>
      </Tabs>
      <Divider></Divider>
      {page === 0 && <MyTicketsComponent></MyTicketsComponent>}
      {page === 1 && <TicketHistoryComponent></TicketHistoryComponent>}
        {page === 2 && <EventHistoryComponent></EventHistoryComponent>}
    </ContainerComponent>
  );
}
