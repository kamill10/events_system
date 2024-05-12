import {
  ReactNode,
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";
import { AccountType } from "../types/Account";
import { jwtDecode } from "jwt-decode";

interface AccountState {
  account: AccountType | null;
  setAccount: (account: AccountType | null) => void;
  token: string | null;
  setToken: (token: string | null) => void;
  parsedToken: TokenType | null;
  setParsedToken: (token: TokenType) => void;
  isLogging: boolean;
  setIsLogging: (state: boolean) => void;
  isFetching: boolean;
  setIsFetching: (state: boolean) => void;
}

export interface TokenType {
  exp: number;
  iat: number;
  jti: string;
  role: Array<string>;
  sub: string;
}

const AccountStateContext = createContext<AccountState | null>(null);

export const AccountStateContextProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  const [token, setToken] = useState<string | null>(
    localStorage.getItem("token"),
  );
  const [parsedToken, setParsedToken] = useState<TokenType | null>(null);
  const [account, setAccount] = useState<AccountType | null>(null);
  const [isLogging, setIsLogging] = useState(false);
  const [isFetching, setIsFetching] = useState(false);

  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token);
      setParsedToken(jwtDecode(token));
    }
  }, [token]);

  return (
    <AccountStateContext.Provider
      value={{
        account,
        setAccount,
        token,
        setToken,
        parsedToken,
        setParsedToken,
        isLogging,
        setIsLogging,
        isFetching,
        setIsFetching,
      }}
    >
      {children}
    </AccountStateContext.Provider>
  );
};

export const useAccountState = () => {
  const accountState = useContext(AccountStateContext);

  if (!accountState) {
    throw new Error("You forgot about AccountStateContextProvider!");
  }

  return accountState;
};
