import { AxiosError } from "axios";
import { api } from "../axios/axios.config";
import { useLoadingScreen } from "./useLoadingScreen";
import { useTranslation } from "react-i18next";
import useNotification from "./useNotification";
import {
  CreateSessionDTOType,
  CreateSessionType,
  UpdateSessionDTOType,
  UpdateSessionType,
} from "../types/Session";

export const useSessions = () => {
  const { setIsFetching } = useLoadingScreen();
  const { t } = useTranslation();
  const sendNotification = useNotification();

  const getDetailedSessions = async (id: string) => {
    try {
      setIsFetching(true);
      const { data } = await api.getEventDetailedSessions(id);
      return data;
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
          description: t("getDetailedSessionError"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const signOnSession = async (sessionId: string) => {
    try {
      setIsFetching(true);
      await api.signOnSession(sessionId);
      sendNotification({
        type: "success",
        description: t("signOnSessionSuccess"),
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
          description: t("signOnSessionError"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const getSession = async (id: string) => {
    try {
      setIsFetching(true);
      const { data } = await api.getSession(id);
      return data;
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
          description: t("getSessionError"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const getSessionParticipants = async (id: string) => {
    try {
      setIsFetching(true);
      const { data } = await api.getSessionParticipants(id);
      return data;
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
          description: t("getSessionParticipantsError"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const getSessionForManager = async (id: string) => {
    try {
      setIsFetching(true);
      const { data } = await api.getSessionForManager(id);
      return data;
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
          description: t("getSessionParticipantsError"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  async function createSession(session: CreateSessionType) {
    const dataToSend: CreateSessionDTOType = {
      ...session,
      startDate: session.startDate?.format("YYYY-MM-DD HH:mm:ss") ?? "",
      endDate: session.endDate?.format("YYYY-MM-DD HH:mm:ss") ?? "",
    };
    try {
      setIsFetching(true);
      await api.createSession(dataToSend);
      sendNotification({
        type: "success",
        description: t("createSessionSucc"),
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
          description: t("createSessionFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  }

  async function cancelSession(id: string) {
    try {
      setIsFetching(true);
      await api.cancelSession(id);
      sendNotification({
        type: "success",
        description: t("cancelSessionSucc"),
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
          description: t("cancelSessionFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  }

  async function updateSession(id: string, data: UpdateSessionType) {
    const dataToSend: UpdateSessionDTOType = {
      ...data,
      startDate: data.startDate?.format("YYYY-MM-DD HH:mm:ss") ?? "",
      endDate: data.endDate?.format("YYYY-MM-DD HH:mm:ss") ?? "",
    };
    try {
      setIsFetching(true);
      await api.updateSession(id, dataToSend);
      sendNotification({
        type: "success",
        description: t("updateSessionSucc"),
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
          description: t("updateSessionFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  }

  return {
    getDetailedSessions,
    signOnSession,
    getSession,
    getSessionParticipants,
    createSession,
    cancelSession,
    getSessionForManager,
    updateSession,
  };
};
