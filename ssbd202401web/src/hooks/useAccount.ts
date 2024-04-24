import { useNavigate } from "react-router-dom"
import { useAccountState } from "../context/AccountContext";
import { AccountLoginType, AccountSingInType } from "../types/Account";
import { api } from "../axios/axios.config";
import { Pathnames } from "../router/Pathnames";

export const useAccount = () => {
    const navigate = useNavigate();
    const { account, setAccount, token, setToken, isLogging, setIsLogging, isFetching, setIsFetching } = useAccountState();

    const isAuthenticated = !!token;

    const logIn = async (formData: AccountLoginType) => {
        try {
            setIsLogging(true);
            const { data } = await api.logIn(formData);
            setToken(data);
            navigate(Pathnames.public.home);
        } catch (e) {
            console.error(e)
        } finally {
            setIsLogging(false);
        }
    }

    const logOut = async () => {
        try {
            setIsFetching(true);
        } catch (e) {
            console.error(e);
        } finally {
            localStorage.removeItem("token");
            setAccount(null);
            setToken(null);
            navigate(Pathnames.public.home);
            setIsFetching(false);
        }
    }

    const signIn = async (formData: AccountSingInType) => {
        try {
            setIsFetching(true);
            const { data } = await api.singIn(formData);
            setToken(data);
            navigate(Pathnames.public.home);
        } catch (e) {
            console.error(e);
        } finally {
            setIsFetching(false);
        }
    }

    return {
        account,
        isLogging,
        isFetching,
        isAuthenticated,
        logIn,
        logOut,
        signIn
    }
}