import { Breadcrumbs, Divider, Tab, Tabs } from "@mui/material";
import ContainerComponent from "../components/ContainerComponent";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import { Pathnames } from "../router/Pathnames";
import { useState, SyntheticEvent, useEffect } from "react";
import TicketHistoryComponent from "../components/TicketHistoryComponent";

export default function MyTicketsPage() {
  const [page, setPage] = useState(0);
  const { t } = useTranslation();

  useEffect(() => console.log(page), [page]);

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
        <Tab label={t("history")}></Tab>
      </Tabs>
      <Divider></Divider>
      {page === 1 && <TicketHistoryComponent></TicketHistoryComponent>}
    </ContainerComponent>
  );
}
