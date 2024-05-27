import axios, { AxiosError } from "axios";
import { ApiResponseType } from "../types/ApiResponse.ts";
import {
  ChangeEmailType,
  ChangeMyEmailType,
  ChangeMyPasswordType,
  GetAccountType,
  GetDetailedAccountType,
  GetPersonalAccountType, PaginationResponse,
  UpdatePersonalDataType,
} from "../types/Account.ts";
import {
  LoginCredentialsType,
  ForgotPasswordType,
  ResetPasswordType,
  SignInCredentialsType,
} from "../types/Authentication.ts";
import { AccountTypeEnum } from "../types/enums/AccountType.enum.ts";
import { Pathnames } from "../router/Pathnames.ts";
import { NavigateFunction } from "react-router-dom";
import {SortingRequestParams} from "../types/SortingRequestParams.ts";
import { AccountChangesType } from "../types/AccountChanges.ts";

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

export function setupInterceptors(navigate: NavigateFunction) {
  apiWithAuthToken.interceptors.request.use(
    (config) => {
      // Modify the request config to include the Authorization header
      const token = localStorage.getItem("token");
      if (token) config.headers.Authorization = "Bearer " + token;
      return config;
    },
    (error) => {
      if (error instanceof AxiosError && error.response?.status === 403) {
        navigate(Pathnames.participant.logout);
      }
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
      if (error instanceof AxiosError && error.response?.status === 403)
        navigate(Pathnames.participant.logout);
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
      if (error instanceof AxiosError && error.response?.status === 403) {
        navigate(Pathnames.participant.logout);
      }
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
      if (error instanceof AxiosError && error.response?.status === 403) {
        navigate(Pathnames.participant.logout);
      }
      return Promise.reject(error);
    },
  );
}

export const api = {
  getAllAccounts: (): ApiResponseType<GetAccountType[]> =>
    apiWithAuthToken.get("/accounts"),
  getAccountByUsername: (
    username: string,
  ): ApiResponseType<GetDetailedAccountType> =>
    apiWithEtag.get("/accounts/username/" + username),
  getAccountChanges: (
    username: string,
  ): ApiResponseType<AccountChangesType[]> =>
    apiWithEtag.get("/accounts/history/" + username),
  logIn: (formData: LoginCredentialsType): ApiResponseType<string> =>
    apiForAnon.post("/auth/authenticate", formData),
  logOut: () => apiWithAuthToken.post("/auth/logout"),
  singIn: (formData: SignInCredentialsType): ApiResponseType<string> =>
    apiForAnon.post("/auth/register", formData),
  verifyAccount: (key: string): ApiResponseType<void> =>
    apiForAnon.post(`/auth/verify-account/${key}`),
  updateMyPersonalData: (data: UpdatePersonalDataType): ApiResponseType<void> =>
    apiWithEtag.put("/me/user-data", data),
  getMyAccount: (): ApiResponseType<GetPersonalAccountType> =>
    apiWithEtag.get("/me"),
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
    apiWithEtag.post("/me/email", data),
  confirmPasswordUpdate: (key: string): ApiResponseType<void> =>
    apiForAnon.patch(`/me/change-password/token/${key}`),
  confirmEmailUpdate: (key: string): ApiResponseType<void> =>
    apiWithEtag.patch(`/me/change-email/token/${key}`),
  updateAccountData: (id: string, data: UpdatePersonalDataType) =>
    apiWithEtag.put("/accounts/" + id + "/user-data", data),
  updateAccountPassword: (data: ChangeEmailType) =>
    apiWithEtag.post("/accounts/change-password", data),
  updateAccountEmail: (id: string, data: ChangeEmailType) =>
    apiWithEtag.post("/accounts/change-email/" + id, data),
  setAccountActive: (id: string) =>
    apiWithEtag.patch("/accounts/" + id + "/set-active"),
  setAccountInactive: (id: string) =>
    apiWithEtag.patch("/accounts/" + id + "/set-inactive"),
  removeRole: (role: AccountTypeEnum, id: string) =>
    apiWithEtag.delete("/accounts/" + id + "/remove-role?roleName=" + role),
  addRole: (role: AccountTypeEnum, id: string) =>
    apiWithEtag.post("/accounts/" + id + "/add-role?roleName=" + role),
  switchActiveRoleToAdmin: () =>
    apiWithEtag.post(`/me/switch-role?role=${AccountTypeEnum.ADMIN}`),
  switchActiveRoleToManager: () =>
    apiWithEtag.post(`/me/switch-role?role=${AccountTypeEnum.MANAGER}`),
  getAccountsWithPagination: (params: SortingRequestParams): ApiResponseType<PaginationResponse> => {
    let url = "/accounts/page";
    let char = '?';
    if (params.phrase) {
      url += `?phrase=${params.phrase}`;
      char = '&';
    }
    if (params.page) {
      url += `${char}page=${params.page}`;
      char = '&';
    }
    if (params.size) {
      url += `${char}size=${params.size}`;
      char = '&';
    }
    if (params.direction) {
      url += `${char}direction=${params.direction}`;
      char = '&';
    }
    if (params.key) {
      url += `${char}key=${params.key}`;
    }
    return apiWithAuthToken.get(url);
  }
};
