import {
  Breadcrumbs,
  Button,
  Divider,
  Tab,
  Tabs,
  Typography,
} from "@mui/material";
import ContainerComponent from "../components/ContainerComponent";
import { useTranslation } from "react-i18next";
import { SyntheticEvent, useEffect, useState } from "react";
import { Pathnames } from "../router/Pathnames";
import { Link, useParams } from "react-router-dom";
import ChangeEventDetailsComponent from "../components/ChangeEventDetailsComponent";
import { useEvents } from "../hooks/useEvents";
import { AxiosError } from "axios";
import { Event } from "../types/Event";
import RefreshIcon from "@mui/icons-material/Refresh";

export default function EventPage() {
  const { t } = useTranslation();
  const [page, setPage] = useState(1);
  const { getEventById } = useEvents();
  const { id } = useParams();
  const [event, setEvent] = useState<Event | null>(null);

  async function getEvent() {
    const response = await getEventById(id ?? "");
    if (!(response instanceof AxiosError)) {
      setEvent(response as Event);
    }
  }

  const handleChange = (_: SyntheticEvent, newValue: number) => {
    setPage(newValue);
  };

  useEffect(() => {
    getEvent();
  }, []);

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
            fontWeight: "bold",
          }}
        >
          {t("eventLink")}
        </Link>
      </Breadcrumbs>
      <Typography variant="h4">
        {t("eventLink")}: {event?.name}
      </Typography>
      <Button
        variant="contained"
        onClick={getEvent}
        startIcon={<RefreshIcon></RefreshIcon>}
        sx={{
          margin: 2,
        }}
      >
        {t("refreshData")}
      </Button>
      <Tabs value={page} onChange={handleChange}>
        <Tab label={t("eventLink")}></Tab>
        <Tab label={t("changeEvent")}></Tab>
      </Tabs>
      <Divider></Divider>
      {page === 1 && (
        <ChangeEventDetailsComponent
          event={event}
          getEvent={getEvent}
        ></ChangeEventDetailsComponent>
      )}
    </ContainerComponent>
  );
}
