import axios from "axios";
import { ApiResponseType } from "../types/ApiResponse.ts";
import {
  AccountType,
  AccountLoginType,
  AccountSingInType,
} from "../types/Account.ts";
import { PersonalDataType } from "../types/PersonalData.ts";
import { ForgotPasswordType } from "../types/ForgotPassword.ts";
import { ResetPasswordType } from "../types/ResetPasswordType.ts";
import { ChangeMyPasswordType } from "../types/ChangeMyPasswordType.ts";
import { ChangeMyEmailType } from "../types/ChangeMyEmailType.ts";

const API_URL: string = "https://team-1.proj-sum.it.p.lodz.pl/api";
const TIMEOUT_MS: number = 30000;

const DEFAULT_HEADERS = {
  Accept: "application/json",
  "Content-Type": "application/json",
};

const ETAG_HEADERS = {
  Accept: "application/json",
  "Content-Type": "application/json",
};

export const LOGIN_HEADERS = {
  Accept: "application/json",
  "Content-type": "application/json",
};

const apiWithAuthToken = axios.create({
  baseURL: API_URL,
  timeout: TIMEOUT_MS,
  headers: DEFAULT_HEADERS,
});

const apiWithEtag = axios.create({
  baseURL: API_URL,
  timeout: TIMEOUT_MS,
  headers: ETAG_HEADERS,
});

const apiForAnon = axios.create({
  baseURL: API_URL,
  timeout: TIMEOUT_MS,
  headers: LOGIN_HEADERS,
});

apiWithAuthToken.interceptors.request.use(
  (config) => {
    // Modify the request config to include the Authorization header
    const token = localStorage.getItem("token");
    if (token) config.headers.Authorization = "Bearer " + token;
    return config;
  },
  (error) => {
    // Handle request error
    return Promise.reject(error);
  },
);

apiWithAuthToken.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Handle response error
    return Promise.reject(error);
  },
);

apiWithEtag.interceptors.request.use(
  (config) => {
    // Modify the request config to include the Authorization header
    const token = localStorage.getItem("token");
    if (token) config.headers.Authorization = "Bearer " + token;
    const etag = localStorage.getItem("etag");
    if (etag) config.headers["If-Match"] = etag;
    return config;
  },
  (error) => {
    // Handle request error
    return Promise.reject(error);
  },
);

apiWithEtag.interceptors.response.use(
  (response) => {
    const etag = response.headers.etag as string;
    if (etag) {
      localStorage.setItem(
        "etag",
        etag.substring(1, response.headers.etag.length - 1),
      );
    }
    return response;
  },
  (error) => {
    // Handle response error
    return Promise.reject(error);
  },
);

export const api = {
  getAccounts: (): ApiResponseType<Array<AccountType>> =>
    apiWithAuthToken.get("/accounts"),
  logIn: (formData: AccountLoginType): ApiResponseType<string> =>
    apiForAnon.post("/auth/authenticate", formData),
  singIn: (formData: AccountSingInType): ApiResponseType<string> =>
    apiForAnon.post("/auth/register", formData),
  verifyAccount: (key: string): ApiResponseType<void> =>
    apiForAnon.post(`/auth/verify-account/${key}`),
  updateMyPersonalData: (data: PersonalDataType): ApiResponseType<void> =>
    apiWithEtag.put("/me/user-data", data),
  getMyAccount: (): ApiResponseType<AccountType> => apiWithEtag.get("/me"),
  forgotMyPassword: (data: ForgotPasswordType) =>
    apiForAnon.post("/accounts/reset-password", data),
  resetMyPassword: (data: ResetPasswordType) =>
    apiForAnon.patch("/accounts/reset-password/token/" + data.token, {
      value: data.newPassword,
    }),
  changeMyPassword: (data: ChangeMyPasswordType) =>
    apiWithEtag.post("/me/change-password", {
      oldPassword: data.oldPassword,
      newPassword: data.newPassword,
    }),
  changeMyEmail: (data: ChangeMyEmailType): ApiResponseType<void> =>
    apiWithEtag.patch("/me/email", data),
  confirmPasswordUpdate: (key: string): ApiResponseType<void> =>
    apiForAnon.patch(`/me/change-password/token/${key}`),
  confirmEmailUpdate: (key: string): ApiResponseType<void> =>
    apiWithEtag.patch(`/me/email/token/${key}`),
};
