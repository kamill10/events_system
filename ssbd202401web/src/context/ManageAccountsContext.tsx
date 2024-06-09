import { ReactNode, createContext, useContext, useState } from "react";
import { AccountsWithNumberOfElements } from "../types/Account";

interface ManageAccounts {
  accounts: AccountsWithNumberOfElements | null;
  setAccounts: (accounts: AccountsWithNumberOfElements) => void;
}

const ManageAccountsStateContext = createContext<ManageAccounts | null>(null);

export const ManageAccountsStateContextProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  const [accounts, setAccounts] = useState<AccountsWithNumberOfElements | null>(
    null,
  );

  return (
    <ManageAccountsStateContext.Provider
      value={{
        accounts,
        setAccounts,
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
