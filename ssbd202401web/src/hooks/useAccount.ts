import { useNavigate } from "react-router-dom";
import { useAccountState } from "../context/AccountContext";
import { AccountLoginType, AccountSingInType } from "../types/Account";
import { api } from "../axios/axios.config";
import { Pathnames } from "../router/Pathnames";
import useNotification from "./useNotification";
import { PersonalDataType } from "../types/PersonalData";
import { ForgotPasswordType } from "../types/ForgotPassword";
import { ResetPasswordType } from "../types/ResetPasswordType";
import { jwtDecode } from "jwt-decode";
import { AccountTypeEnum } from "../types/enums/AccountType.enum";
import { ChangeMyPasswordType } from "../types/ChangeMyPasswordType.ts";
import {ChangeMyEmailType} from "../types/ChangeMyEmailType.ts";

export const useAccount = () => {
  const sendNotification = useNotification();
  const navigate = useNavigate();
  const {
    account,
    parsedToken,
    setAccount,
    token,
    setToken,
    isLogging,
    setIsLogging,
    isFetching,
    setIsFetching,
    adminLayout,
    setAdminLayout,
  } = useAccountState();

  const isAuthenticated = !!token;
  const isManager = parsedToken?.role.includes(AccountTypeEnum.MANAGER);
  const isAdmin = parsedToken?.role.includes(AccountTypeEnum.ADMIN);
  const isParticipant = parsedToken?.role.includes(AccountTypeEnum.PARTICIPANT);

  const logIn = async (formData: AccountLoginType) => {
    try {
      setIsLogging(true);
      const { data } = await api.logIn(formData);
      setToken(data);
      localStorage.setItem("token", data);
      sendNotification({
        type: "success",
        description: "Successfully logged in! Welcome, " + jwtDecode(data).sub,
      });
      navigate(Pathnames.public.home);
      getMyAccount();
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Failed to log in :(",
      });
      return e;
    } finally {
      setIsLogging(false);
    }
  };

  const logOut = async () => {
    try {
      setIsFetching(true);
      sendNotification({
        type: "success",
        description: "Successfully logged out!",
      });
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Failed to log out :(",
      });
      return e;
    } finally {
      localStorage.removeItem("token");
      setAccount(null);
      setToken(null);
      navigate(Pathnames.public.home);
      setIsFetching(false);
    }
  };

  const signIn = async (formData: AccountSingInType) => {
    try {
      setIsFetching(true);
      const { data } = await api.singIn(formData);
      setToken(data);
      sendNotification({
        type: "success",
        description:
          "Successfully signed in! Check your e-mail box and verify your account!",
      });
      navigate(Pathnames.public.login);
    } catch (e) {
      console.error(e);
      sendNotification({
        description: "Signing in failed :(",
        type: "error",
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const verifyAccount = async (key: string) => {
    try {
      setIsFetching(true);
      await api.verifyAccount(key);
      sendNotification({
        type: "success",
        description: "Account has been verified!!",
      });
    } catch (e) {
      console.error(e);
      sendNotification({
        description: "Failed to verify an account :(",
        type: "error",
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const confirmPasswordUpdate = async (key: string) => {
    try {
      setIsFetching(true);
      await api.confirmPasswordUpdate(key);
      sendNotification({
        type: "success",
        description: "Account has been verified!!",
      });
    } catch (e) {
      console.error(e);
      sendNotification({
        description: "Failed to verify an account :(",
        type: "error",
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const confirmEmailUpdate = async (key: string) => {
    try {
      setIsFetching(true);
      await api.confirmEmailUpdate(key);
      sendNotification({
        type: "success",
        description: "Email has been verified!!",
      });
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Failed to verify email",
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
        type: "error",
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const updateMyPersonalData = async (data: PersonalDataType) => {
    try {
      setIsFetching(true);
      await api.updateMyPersonalData(data);
      getMyAccount();
      sendNotification({
        type: "success",
        description: "Account has been updated!!",
      });
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Account update failed :(",
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const updateMyPassword = async (data: ChangeMyPasswordType) => {
    try {
      setIsFetching(true);
      await api.changeMyPassword(data);
      sendNotification({
        type: "success",
        description: "Password has been changed successfully!",
      });
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Password change failed :(",
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const updateMyEmail = async (data: ChangeMyEmailType) => {
    try {
      setIsFetching(true);
      await api.changeMyEmail(data);
      sendNotification({
        type: "success",
        description: "Sent email! Confirm your email change"
      });
    } catch (e) {
      console.error(e);
      sendNotification({
        type:"error",
        description: "Email change failed",
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  }

  const requestPasswordReset = async (data: ForgotPasswordType) => {
    try {
      setIsFetching(true);
      await api.forgotMyPassword(data);
      sendNotification({
        type: "success",
        description: "Request sent successfully! Check your mailbox!",
      });
      navigate(Pathnames.public.login);
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Request failed :(",
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  const resetMyPassword = async (data: ResetPasswordType) => {
    try {
      setIsFetching(true);
      await api.resetMyPassword(data);
      sendNotification({
        type: "success",
        description: "Password has been reset successfully!",
      });
      navigate(Pathnames.public.login);
    } catch (e) {
      console.error(e);
      sendNotification({
        type: "error",
        description: "Password reset failed :(",
      });
      return e;
    } finally {
      setIsFetching(false);
    }
  };

  return {
    account,
    parsedToken,
    isLogging,
    isFetching,
    isAuthenticated,
    isAdmin,
    isParticipant,
    isManager,
    logIn,
    logOut,
    signIn,
    verifyAccount,
    confirmPasswordUpdate,
    confirmEmailUpdate,
    getMyAccount,
    updateMyPersonalData,
    updateMyPassword,
    updateMyEmail,
    requestPasswordReset,
    resetMyPassword,
    adminLayout,
    setAdminLayout,
  };
};
