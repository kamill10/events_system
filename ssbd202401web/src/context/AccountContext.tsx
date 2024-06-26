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
  const [parsedToken, setParsedToken] = useState<TokenType | null>(null);
  const [account, setAccount] = useState<GetPersonalAccountType | null>(null);
  const [adminLayout, setAdminLayout] = useState(true);
  const [theme, setTheme] = useState(
    account?.accountTheme ?? localStorage.getItem("theme") ?? "Light",
  );

  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token);
      setParsedToken(jwtDecode(token));
    } else {
      setParsedToken(null);
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
