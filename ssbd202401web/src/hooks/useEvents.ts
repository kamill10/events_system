import { AxiosError } from "axios";
import { api } from "../axios/axios.config";
import { useEventsState } from "../context/EventsContext";
import useNotification from "./useNotification";
import { useTranslation } from "react-i18next";
import { CreateEventDTOType, CreateEventType } from "../types/Event";
import { useLoadingScreen } from "./useLoadingScreen";

export const useEvents = () => {
  const sendNotification = useNotification();
  const { t } = useTranslation();
  const { events, setEvents } = useEventsState();
  const { setIsFetching } = useLoadingScreen();

  const getEvents = async () => {
    try {
      setIsFetching(true);
      const { data } = await api.getNonPastEvents();
      setEvents(data);
    } catch (e) {
      console.error(e);
      if (e instanceof AxiosError && t(e.response?.data) != e.response?.data) {
        sendNotification({
          type: "error",
          description: t(e.response?.data),
        });
      } else {
        sendNotification({
          type: "error",
          description: t("getAccountsFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const createEvent = async (data: CreateEventType) => {
    const dataToSend: CreateEventDTOType = {
      ...data,
      startDate: data.startDate?.toISOString() ?? "",
      endDate: data.endDate?.toISOString() ?? "",
    };
    try {
      setIsFetching(true);
      await api.createEvent(dataToSend);
      sendNotification({
        type: "success",
        description: t("createEventSucc"),
      });
    } catch (e) {
      console.error(e);
      if (e instanceof AxiosError && t(e.response?.data) != e.response?.data) {
        sendNotification({
          type: "error",
          description: t(e.response?.data),
        });
      } else {
        sendNotification({
          type: "error",
          description: t("createEventFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  return {
    events,
    getEvents,
    createEvent,
  };
};
