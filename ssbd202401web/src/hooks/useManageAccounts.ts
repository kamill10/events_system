import { useTranslation } from "react-i18next";
import { api } from "../axios/axios.config";
import { useUsersState } from "../context/ManageAccountsContext";
import { ChangeEmailType, UpdatePersonalDataType } from "../types/Account";
import { AccountTypeEnum } from "../types/enums/AccountType.enum";
import useNotification from "./useNotification";
import { SortingRequestParams } from "../types/SortingRequestParams.ts";
import { AxiosError } from "axios";
import { useLoadingScreen } from "./useLoadingScreen.ts";

export const useManageAccounts = () => {
  const sendNotification = useNotification();
  const { t } = useTranslation();
  const { accounts, setAccounts } = useUsersState();
  const { setIsFetching } = useLoadingScreen();

  const getAccountByUsername = async (username: string) => {
    try {
      setIsFetching(true);
      const { data } = await api.getAccountByUsername(username);
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
          description: t("getAccountByUsernameFail"),
        });
      }
      return null;
    } finally {
      setIsFetching(false);
    }
  };

  const getAccountChanges = async (username: string) => {
    try {
      setIsFetching(true);
      const { data } = await api.getAccountChanges(username);
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
          description: t("getAccountChangesFail"),
        });
      }
      return null;
    } finally {
      setIsFetching(false);
    }
  };

  const updateAccountData = async (
    id: string,
    data: UpdatePersonalDataType,
  ) => {
    try {
      setIsFetching(true);
      await api.updateAccountData(id, data);
      sendNotification({
        type: "success",
        description: t("updateAccountDataSucc"),
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
          description: t("updateAccountDataFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const updateAccountPassword = async (data: ChangeEmailType) => {
    try {
      setIsFetching(true);
      await api.updateAccountPassword(data);
      sendNotification({
        type: "success",
        description: t("updateAccountPasswordSucc"),
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
          description: t("updateAccountPasswordFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const updateAccountEmail = async (id: string, data: ChangeEmailType) => {
    try {
      setIsFetching(true);
      await api.updateAccountEmail(id, data);
      sendNotification({
        type: "success",
        description: t("updateAccountEmailSucc"),
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
          description: t("updateAccountEmailFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const activateAccount = async (id: string) => {
    try {
      setIsFetching(true);
      await api.setAccountActive(id);
      sendNotification({
        type: "success",
        description: t("activateAccountSucc"),
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
          description: t("activateAccountFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const deactivateAccount = async (id: string) => {
    try {
      setIsFetching(true);
      await api.setAccountInactive(id);
      sendNotification({
        type: "success",
        description: t("activateDeaccountSucc"),
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
          description: t("activateDeaccountFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const addRole = async (role: AccountTypeEnum, id: string) => {
    try {
      setIsFetching(true);
      await api.addRole(role, id);
      sendNotification({
        type: "success",
        description: t("addRoleSucc"),
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
          description: t("addRoleFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const removeRole = async (role: AccountTypeEnum, id: string) => {
    try {
      setIsFetching(true);
      await api.removeRole(role, id);
      sendNotification({
        type: "success",
        description: t("removeRoleSucc"),
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
          description: t("removeRoleFail"),
        });
      }
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const getAccountsWithPagination = async (
    requestParams: SortingRequestParams,
  ) => {
    try {
      setIsFetching(true);
      const { data } = await api.getAccountsWithPagination(requestParams);
      setAccounts({
        accounts: data.content,
        totalElements: data.totalElements,
      });
      return data.content;
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

  return {
    accounts,
    setIsFetching,
    setAccounts,
    getAccountByUsername,
    getAccountChanges,
    updateAccountData,
    updateAccountPassword,
    updateAccountEmail,
    activateAccount,
    deactivateAccount,
    addRole,
    removeRole,
    getAccountsWithPagination,
  };
};
