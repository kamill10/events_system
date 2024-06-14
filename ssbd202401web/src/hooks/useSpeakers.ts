import useNotification from "./useNotification.tsx";
import { useTranslation } from "react-i18next";
import { api } from "../axios/axios.config.ts";
import { AxiosError } from "axios";
import { useSpeakersState } from "../context/SpeakersContext.tsx";
import { PaginationRequestParams } from "../types/PaginationRequestParams.ts";
import { UpdateSpeakerDataType } from "../types/Speaker.ts";
import { CreateSpeaker } from "../types/Speaker.ts";
import { useLoadingScreen } from "./useLoadingScreen.ts";

export const useSpeakers = () => {
  const sendNotification = useNotification();
  const { t } = useTranslation();
  const { speakers, setSpeakers } = useSpeakersState();

  const { setIsFetching } = useLoadingScreen();

  const getSpeakersWithPagination = async (
    requestParams: PaginationRequestParams,
  ) => {
    try {
      setIsFetching(true);
      const { data } = await api.getSpeakersWithPagination(requestParams);
      setSpeakers({
        speakers: data.content,
        totalElements: data.totalElements,
      });
      return data.content;
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
          description: t("getSpeakersFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const getAllSpeakers = async () => {
    try {
      setIsFetching(true);
      const { data } = await api.getAllSpeakers();
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
          description: t("getSpeakersFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const getSpeakerById = async (id: string) => {
    try {
      setIsFetching(true);
      const { data } = await api.getSpeaker(id);
      return data;
    } catch (e) {
      console.error(e);
      if (e instanceof AxiosError && t(e.response?.data) !== e.response?.data) {
        sendNotification({
          type: "error",
          description: t(e.response?.data),
        });
      } else {
        sendNotification({
          type: "error",
          description: t("getSpeakerByIdFail"),
        });
      }
      return null;
    } finally {
      setIsFetching(false);
    }
  };

  const addSpeaker = async (speaker: CreateSpeaker) => {
    try {
      setIsFetching(true);
      await api.addSpeaker(speaker);
      sendNotification({
        type: "success",
        description: t("addSpeakerSuccess"),
      });
    } catch (e) {
      console.error(e);
      if (e instanceof AxiosError && t(e.response?.data) !== e.response?.data) {
        sendNotification({
          type: "error",
          description: t(e.response?.data),
        });
      } else {
        sendNotification({
          type: "error",
          description: t("addSpeakerFail"),
        });
      }
    } finally {
      setIsFetching(false);
    }
  };
  const updateSpeakerById = async (
    id: string,
    speaker: UpdateSpeakerDataType,
  ) => {
    try {
      setIsFetching(true);
      const { data } = await api.updateSpeaker(id, speaker);
      sendNotification({
        type: "success",
        description: t("updateSpeakerSuccess"),
      });
      return data;
    } catch (e) {
      console.error(e);
      if (e instanceof AxiosError && t(e.response?.data) !== e.response?.data) {
        sendNotification({
          type: "error",
          description: t(e.response?.data),
        });
      } else {
        sendNotification({
          type: "error",
          description: t("updateSpeakerByIdFail"),
        });
      }
      return null;
    } finally {
      setIsFetching(false);
    }
  };

  return {
    speakers,
    getSpeakersWithPagination,
    getSpeakerById,
    updateSpeakerById,
    addSpeaker,
    getAllSpeakers,
  };
};
