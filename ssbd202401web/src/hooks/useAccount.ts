import { useNavigate } from "react-router-dom"
import { useAccountState } from "../context/AccountContext";
import { AccountLoginType, AccountSingInType } from "../types/Account";
import { api } from "../axios/axios.config";
import { Pathnames } from "../router/Pathnames";
import useNotification from "./useNotification";
import { PersonalDataType } from "../types/PersonalData";

export const useAccount = () => {
    const sendNotification = useNotification();
    const navigate = useNavigate();
    const { account, parsedToken, setAccount, token, setToken, isLogging, setIsLogging, isFetching, setIsFetching } = useAccountState();

    const isAuthenticated = !!token;

    const logIn = async (formData: AccountLoginType) => {
        try {
            setIsLogging(true);
            const { data } = await api.logIn(formData);
            setToken(data);
            localStorage.setItem("token", data);
            navigate(Pathnames.public.home);
            await getMyAccount();
            sendNotification({
                type: "success",
                description: "Successfully logged in! Welcome, " + parsedToken?.sub
            });
        } catch (e) {
            console.error(e);
            sendNotification({
                type: "error",
                description: "Failed to log in :("
            });
            return e;
        } finally {
            setIsLogging(false);
        }
    }

    const logOut = async () => {
        try {
            setIsFetching(true);
            sendNotification({
                type: "success",
                description: "Successfully logged out!"
            });
        } catch (e) {
            console.error(e);
            sendNotification({
                type: "error",
                description: "Failed to log out :("
            });
            return e;
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
            sendNotification({
                type: "success",
                description: "Successfully signed in! Check your e-mail box and verify your account!"
            });
        } catch (e) {
            console.error(e);
            sendNotification({
                description: "Signing in failed :(",    
                type: "error"
            });
            return e;
        } finally {
            setIsFetching(false);
        } 
    }

    const verifyAccount = async (key: string) => {
        try {
            setIsFetching(true);
            await api.verifyAccount(key);
            sendNotification({
                type: "success",
                description: "Account has been confirmed!!"
            });
        } catch (e) {
            console.error(e);
            sendNotification({
                description: "Failed to confirm an account :(",    
                type: "error"
            });
            return e;
        } finally {
            setIsFetching(false);
        }
    }

    const getMyAccount = async () => {
        try {
            setIsFetching(true);
            const { data } = await api.getMyAccount();
            setAccount(data);
        } catch (e) {
            console.error(e);
            sendNotification({
                description: "Failed to fetch an account :(",    
                type: "error"
            });
        } finally {
            setIsFetching(false);
        }
    }

    const updateMyPersonalData = async(data: PersonalDataType) => {
        try {
            setIsFetching(true);
            await api.updateMyPersonalData(data);
            getMyAccount();
            sendNotification({
                type: "success",
                description: "Account has been updated!!"
            });
        } catch (e) {
            console.error(e);
            sendNotification({
                type: "error",
                description: "Account update failed :("
            });
        } finally {
            setIsFetching(false);
        }
    }

    return {
        account,
        parsedToken,
        isLogging,
        isFetching,
        isAuthenticated,
        logIn,
        logOut,
        signIn,
        verifyAccount,
        getMyAccount,
        updateMyPersonalData
    }
}