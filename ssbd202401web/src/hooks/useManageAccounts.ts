import { api } from "../axios/axios.config";
import { useUsersState } from "../context/ManageAccountsContext";
import useNotification from "./useNotification";

export const useManageAccounts = () => {
  const sendNotification = useNotification();
  const {
    accounts,
    setAccounts,
    isFetching,
    setIsFetching
  } = useUsersState();

  const getAllAccounts = async () => {
    try {
      setIsFetching(true);
      const { data } = await api.getAllAccounts();
      setAccounts(data);
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Failed to fetch all accounts :("
      });
    } finally {
      setIsFetching(false);
    }
  }

  return {
    accounts,
    isFetching,
    setIsFetching,
    setAccounts,
    getAllAccounts
  }
}