import useNotification from "./useNotification.tsx";
import { useTranslation } from "react-i18next";
import { api } from "../axios/axios.config.ts";
import { AxiosError } from "axios";
import { useSpeakersState } from "../context/SpeakersContext.tsx";
import { PaginationRequestParams } from "../types/PaginationRequestParams.ts";
import { UpdateSpeakerDataType } from "../types/Speaker.ts";
import { CreateSpeaker } from "../types/Speaker.ts";

export const useSpeakers = () => {
    const sendNotification = useNotification();
    const { t } = useTranslation();
    const { speakers, isFetching, setSpeakers, setIsFetching } =
        useSpeakersState();

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

    const getSpeakerById = async (id: string) => {
        try {
            setIsFetching(true);
            const { data } = await api.getSpeaker(id);
            console.log(data);
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
            console.log(speaker);
            await api.addSpeaker(speaker);
            sendNotification({
                type: "success",
                description: t("addLocationSuccess"),
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
                    description: t("addLocationFail"),
                });
            }
        } finally {
            setIsFetching(false);
        }
    };
    const updateSpeakerById = async (
        id: string,
        location: UpdateSpeakerDataType,
    ) => {
        try {
            setIsFetching(true);
            const { data } = await api.updateSpeaker(id, location);
            sendNotification({
                type: "success",
                description: t("updateLocationSuccess"),
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
                    description: t("updateLocationByIdFail"),
                });
            }
            return null;
        } finally {
            setIsFetching(false);
        }
    };

    return {
        speakers,
        isFetching,
        getSpeakersWithPagination,
        getSpeakerById,
        updateSpeakerById,
        addSpeaker,
    };
};
