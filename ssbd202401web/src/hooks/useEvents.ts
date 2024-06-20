import { AxiosError } from "axios";
import { api } from "../axios/axios.config";
import { useEventsState } from "../context/EventsContext";
import useNotification from "./useNotification";
import { useTranslation } from "react-i18next";
import {
  CreateEventDTOType,
  CreateEventType,
  UpdateEventDTOType,
  UpdateEventType,
} from "../types/Event";
import { useLoadingScreen } from "./useLoadingScreen";
import { PaginationRequestParams } from "../types/PaginationRequestParams.ts";

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
      startDate: data.startDate?.format("YYYY-MM-DD HH:mm:ss") ?? "",
      endDate: data.endDate?.format("YYYY-MM-DD HH:mm:ss") ?? "",
    };
    try {
      setIsFetching(true);
      const { data } = await api.createEvent(dataToSend);
      sendNotification({
        type: "success",
        description: t("createEventSucc"),
      });
      return data;
    } catch (e) {
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

  const getEventById = async (id: string) => {
    try {
      setIsFetching(true);
      const { data } = await api.getEventById(id);
      return data;
    } catch (e) {
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

  const updateEvent = async (id: string, data: UpdateEventType) => {
    const dataToSend: UpdateEventDTOType = {
      ...data,
      startDate: data.startDate?.format("YYYY-MM-DD HH:mm:ss") ?? "",
      endDate: data.endDate?.format("YYYY-MM-DD HH:mm:ss") ?? "",
    };
    try {
      setIsFetching(true);
      await api.updateEvent(id, dataToSend);
      sendNotification({
        type: "success",
        description: t("updateEventSucc"),
      });
    } catch (e) {
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

  const cancelEvent = async (id: string) => {
    try {
      setIsFetching(true);
      await api.cancelEvent(id);
      sendNotification({
        type: "success",
        description: t("cancelEventSucc"),
      });
    } catch (e) {
      if (e instanceof AxiosError && t(e.response?.data) != e.response?.data) {
        sendNotification({
          type: "error",
          description: t(e.response?.data),
        });
      } else {
        sendNotification({
          type: "error",
          description: t("cancelEventFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const getMyPastEvents = async (params: PaginationRequestParams) => {
    try {
      setIsFetching(true);
      const { data } = await api.getPastEventsPaginationResponse(params);
      return data;
    } catch (e) {
      if (e instanceof AxiosError && t(e.response?.data) != e.response?.data) {
        sendNotification({
          type: "error",
          description: t(e.response?.data),
        });
      } else {
        sendNotification({
          type: "error",
          description: t("getPastEventFail"),
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
    getEventById,
    updateEvent,
    cancelEvent,
    getMyPastEvents,
  };
};
