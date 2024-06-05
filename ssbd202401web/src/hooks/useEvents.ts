import { AxiosError } from "axios";
import { api } from "../axios/axios.config";
import { useEventsState } from "../context/EventsContext";
import useNotification from "./useNotification";
import { useTranslation } from "react-i18next";

export const useEvents = () => {
  const sendNotification = useNotification();
  const { t } = useTranslation();
  const { events, isFetching, setEvents, setIsFetching } = useEventsState();

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

  return {
    events,
    isFetching,
    getEvents,
  };
};
