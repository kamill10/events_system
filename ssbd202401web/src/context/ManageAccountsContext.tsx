import { ReactNode, createContext, useContext, useState } from "react";
import { GetAccountType } from "../types/Account";

interface ManageAccounts {
  accounts: GetAccountType[] | null;
  setAccounts: (accounts: GetAccountType[]) => void;
  isFetching: boolean;
  setIsFetching: (state: boolean) => void;
}

const ManageAccountsStateContext = createContext<ManageAccounts | null>(null);

export const ManageAccountsStateContextProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  const [accounts, setAccounts] = useState<GetAccountType[] | null>(null);
  const [isFetching, setIsFetching] = useState(false);

  return (
    <ManageAccountsStateContext.Provider
      value={{
        accounts,
        setAccounts,
        isFetching,
        setIsFetching,
      }}
    >
      {children}
    </ManageAccountsStateContext.Provider>
  );
};

export const useUsersState = () => {
  const usersState = useContext(ManageAccountsStateContext);

  if (!usersState) {
    throw new Error("You forgot about UsersStateContextProvider!");
  }

  return usersState;
};
