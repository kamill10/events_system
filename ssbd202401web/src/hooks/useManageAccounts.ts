import { api } from "../axios/axios.config";
import { useUsersState } from "../context/ManageAccountsContext";
import { ChangeEmailType, UpdatePersonalDataType } from "../types/Account";
import useNotification from "./useNotification";

export const useManageAccounts = () => {
  const sendNotification = useNotification();
  const { accounts, setAccounts, isFetching, setIsFetching } = useUsersState();

  const getAllAccounts = async () => {
    try {
      setIsFetching(true);
      const { data } = await api.getAllAccounts();
      setAccounts(data);
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Failed to fetch all accounts :(",
      });
    } finally {
      setIsFetching(false);
    }
  };

  const getAccountByUsername = async(username: string) => {
    try {
      setIsFetching(true);
      const { data } = await api.getAccountByUsername(username);
      return data;
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Failed to fetch user details"
      });
      return null;
    } finally {
      setIsFetching(false);
    }
  }

  const updateAccountData = async(id: string, data: UpdatePersonalDataType) => {
    try {
      setIsFetching(true);
      await api.updateAccountData(id, data);
      sendNotification({
        type: "success",
        description: "Account's data updated successfully!"
      });
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Account's data update failed :("
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  }

  const updateAccountPassword = async(data: ChangeEmailType) => {
    try {
      setIsFetching(true);
      await api.updateAccountPassword(data);
      sendNotification({
        type: "success",
        description: "Account's password reset started successfully!"
      });
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Account's password reset failed :("
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  }

  const updateAccountEmail = async(id: string, data: ChangeEmailType) => {
    try {
      setIsFetching(true);
      await api.updateAccountEmail(id, data);
      sendNotification({
        type: "success",
        description: "Account's email updated successfully!"
      });
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Account email failed :("
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  }

  return {
    accounts,
    isFetching,
    setIsFetching,
    setAccounts,
    getAllAccounts,
    getAccountByUsername,
    updateAccountData,
    updateAccountPassword,
    updateAccountEmail
  };
};
