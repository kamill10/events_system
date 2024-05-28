import {
  ReactNode,
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";
import { GetPersonalAccountType } from "../types/Account";
import { jwtDecode } from "jwt-decode";

interface AccountState {
  account: GetPersonalAccountType | null;
  setAccount: (account: GetPersonalAccountType | null) => void;
  theme: string;
  setTheme: (theme: string) => void;
  token: string | null;
  setToken: (token: string | null) => void;
  parsedToken: TokenType | null;
  setParsedToken: (token: TokenType) => void;
  isLogging: boolean;
  setIsLogging: (state: boolean) => void;
  isFetching: boolean;
  setIsFetching: (state: boolean) => void;
  adminLayout: boolean;
  setAdminLayout: (state: boolean) => void;
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
  const [theme, setTheme] = useState<string>("LIGHT");
  const [parsedToken, setParsedToken] = useState<TokenType | null>(null);
  const [account, setAccount] = useState<GetPersonalAccountType | null>(null);
  const [isLogging, setIsLogging] = useState(false);
  const [isFetching, setIsFetching] = useState(false);
  const [adminLayout, setAdminLayout] = useState(true);

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
        theme,
        setTheme,
        token,
        setToken,
        parsedToken,
        setParsedToken,
        isLogging,
        setIsLogging,
        isFetching,
        setIsFetching,
        adminLayout,
        setAdminLayout,
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
