import useNotification from "./useNotification.tsx";
import { useTranslation } from "react-i18next";
import { api } from "../axios/axios.config.ts";
import { AxiosError } from "axios";
import { useLocationsState } from "../context/LocationsContext.tsx";
import { PaginationRequestParams } from "../types/PaginationRequestParams.ts";
import {UpdateLocationDataType} from "../types/Location.ts";

export const useLocations = () => {
  const sendNotification = useNotification();
  const { t } = useTranslation();
  const { locations, isFetching, setLocations, setIsFetching } =
    useLocationsState();

  const getLocationsWithPagination = async (
    requestParams: PaginationRequestParams,
  ) => {
    try {
      setIsFetching(true);
      const { data } = await api.getLocationsWithPagination(requestParams);
      setLocations({
        locations: data.content,
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
          description: t("getLocationsFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const getLocationById = async (id: string) => {
    try {
      setIsFetching(true);
      const { data } = await api.getLocation(id);
      console.log(data);
      return data;
    } catch (e) {
      console.error(e);
      if (e instanceof AxiosError && t(e.response?.data) !== e.response?.data) {
        sendNotification({
          type: 'error',
          description: t(e.response?.data),
        });
      } else {
        sendNotification({
          type: 'error',
          description: t('getLocationByIdFail'),
        });
      }
      return null;
    } finally {
      setIsFetching(false);
    }
  };

  const updateLocationById = async (id: string, location: UpdateLocationDataType) => {
    try {
      setIsFetching(true);
      const { data } = await api.updateLocation(id, location);
      return data;
    } catch (e) {
      console.error(e);
      if (e instanceof AxiosError && t(e.response?.data) !== e.response?.data) {
        sendNotification({
          type: 'error',
          description: t(e.response?.data),
        });
      } else {
        sendNotification({
          type: 'error',
          description: t('updateLocationByIdFail'),
        });
      }
      return null;
    } finally {
      setIsFetching(false);
    }
  }



  return {
    locations,
    isFetching,
    getLocationsWithPagination,
    getLocationById,
    updateLocationById
  };
};
