import { ReactNode, createContext, useContext, useEffect, useState } from "react";
import { AccountType } from "../types/Account";

interface AccountState {
    account: AccountType | null
    setAccount : (account: AccountType | null) => void
    token: string | null
    setToken: (token: string | null) => void
    isLogging: boolean
    setIsLogging: (state: boolean) => void
    isFetching: boolean
    setIsFetching: (state: boolean) => void
}

const AccountStateContext = createContext<AccountState | null>(null);

export const AccountStateContextProvider = ({ children }: { children: ReactNode}) => {
    const [token, setToken] = useState<string | null>(null);
    const [account, setAccount] = useState<AccountType | null>(null);
    const [isLogging, setIsLogging] = useState(false);
    const [isFetching, setIsFetching] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            setToken(token);
        }
    }, []);

    useEffect(() => {
        if (token) {
            localStorage.setItem("token", token);
        }
    }, [token])

    return (
        <AccountStateContext.Provider
            value={{account, setAccount, token, setToken, isLogging, setIsLogging, isFetching, setIsFetching}}
        >
            {children}    
        </AccountStateContext.Provider>
    )
    
}

export const useAccountState = () => {
    const accountState = useContext(AccountStateContext);

    if (!accountState) {
        throw new Error("You forgot about AccountStateContextProvider!");
    }

    return accountState;
}