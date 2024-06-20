import { useTranslation } from "react-i18next";
import useNotification from "./useNotification.tsx";
import { api } from "../axios/axios.config.ts";
import { AxiosError } from "axios";
import { PaginationRequestParams } from "../types/PaginationRequestParams.ts";
import { useLoadingScreen } from "./useLoadingScreen.ts";

export const useMySessions = () => {
  const { t } = useTranslation();
  const sendNotification = useNotification();
  const { setIsFetching } = useLoadingScreen();

  const getMyHistoryTickets = async () => {
    try {
      setIsFetching(true);
      const { data } = await api.getMyHistoryTickets();
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
          description: t("getMyHistoryTicketsFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const getMyTickets = async (requestParams: PaginationRequestParams) => {
    try {
      setIsFetching(true);
      const { data } = await api.getMyTicketsWithPagination(requestParams);
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
          description: t("getMyTicketsFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const getTicket = async (id: string) => {
    try {
      setIsFetching(true);
      const { data } = await api.getTicket(id);
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
          description: t("getTicketFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const signOutOfSession = async (id: string) => {
    try {
      setIsFetching(true);
      await api.signOutOfSession(id);
    } catch (e) {
      if (e instanceof AxiosError && t(e.response?.data) != e.response?.data) {
        sendNotification({
          type: "error",
          description: t(e.response?.data),
        });
      } else {
        sendNotification({
          type: "error",
          description: t("singOutOfSessionFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  return {
    getMyTickets,
    getMyHistoryTickets,
    getTicket,
    signOutOfSession,
  };
};
