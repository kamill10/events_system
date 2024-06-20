import {
  Box,
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
import { useAccount } from "../hooks/useAccount";
import { useSessions } from "../hooks/useSessions";
import { SessionDetailedType } from "../types/SessionDetailed";
import AddIcon from "@mui/icons-material/Add";
import AddSessionModal from "../components/AddSessionModal";
import EventDetailsComponent from "../components/EventDetailsComponent";

export default function EventPage() {
  const { t } = useTranslation();
  const [page, setPage] = useState(0);
  const { getEventById } = useEvents();
  const { id } = useParams();
  const [open, setOpen] = useState(false);
  const { isManager } = useAccount();
  const [event, setEvent] = useState<Event | null>(null);
  const { getDetailedSessions } = useSessions();

  const [sessionsList, setSessionsList] = useState<
    SessionDetailedType[] | null
  >(null);

  async function getEvent() {
    const response = await getEventById(id ?? "");
    if (!(response instanceof AxiosError)) {
      setEvent(response as Event);
    }
  }

  async function getSessions() {
    const response = await getDetailedSessions(id ?? "");
    if (!(response instanceof AxiosError)) {
      setSessionsList(response as SessionDetailedType[]);
    }
  }

  const handleChange = (_: SyntheticEvent, newValue: number) => {
    setPage(newValue);
  };

  useEffect(() => {
    Promise.all([getEvent(), getSessions()]);
  }, []);

  return (
    <>
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
        <Box>
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
          {isManager && (
            <Button
              variant="contained"
              onClick={() => setOpen(true)}
              startIcon={<AddIcon></AddIcon>}
              sx={{
                margin: 2,
              }}
            >
              {t("addSession")}
            </Button>
          )}
        </Box>
        <Tabs value={page} onChange={handleChange}>
          <Tab label={t("eventLink")}></Tab>
          {isManager && <Tab label={t("changeEvent")}></Tab>}
        </Tabs>
        <Divider></Divider>

        {page === 0 && (
          <EventDetailsComponent
            sessionsList={sessionsList}
          ></EventDetailsComponent>
        )}
        {page === 1 && isManager && (
          <ChangeEventDetailsComponent
            event={event}
            getEvent={getEvent}
          ></ChangeEventDetailsComponent>
        )}
      </ContainerComponent>
      <AddSessionModal
        onClose={() => setOpen(false)}
        open={open}
        eventId={id ?? ""}
        getSessions={getSessions}
      ></AddSessionModal>
    </>
  );
}
