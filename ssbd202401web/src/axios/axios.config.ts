import axios from "axios";
import {ApiResponseType} from "../types/ApiResponse.ts";
import {Account, AccountLogin} from "../types/Account.ts";

const API_URL: string = "https://team-1.proj-sum.it.p.lodz.pl/api";
const TIMEOUT_MS: number = 30000;

const DEFAULT_HEADERS = {
    Accept: "application/json",
    "Content-Type": "application/json"
};

const ETAG_HEADERS = {
    Accept: "application/json",
    "Content-Type": "application/json"
};

export const LOGIN_HEADERS = {
    Accept: 'application/json',
    'Content-type': 'application/json',
}

const apiWithAuthToken = axios.create({
    baseURL: API_URL,
    timeout: TIMEOUT_MS,
    headers: DEFAULT_HEADERS,
})

const apiWithEtag = axios.create({
    baseURL: API_URL,
    timeout: TIMEOUT_MS,
    headers: ETAG_HEADERS,
})

const apiForLogin = axios.create({
    baseURL: API_URL,
    timeout: TIMEOUT_MS,
    headers: LOGIN_HEADERS,
})

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
    }
);

apiWithAuthToken.interceptors.response.use(
    (response) => {
        localStorage.setItem("token", response.headers.authorization);
        return response;
    },
    (error) => {
        // Handle response error
        return Promise.reject(error);
    }
);

apiWithEtag.interceptors.request.use(
    (config) => {
        // Modify the request config to include the Authorization header
        const token = localStorage.getItem("token");
        if (token) config.headers.Authorization = "Bearer " + token;
        const etag = localStorage.getItem("etag");
        if (etag) config.headers['If-Match'] = etag;
        return config;
    },
    (error) => {
        // Handle request error
        return Promise.reject(error);
    }
);

apiWithEtag.interceptors.response.use(
    (response) => {
        localStorage.setItem("etag", response.headers.etag);
        return response;
    },
    (error) => {
        // Handle response error
        return Promise.reject(error);
    }
);

export const api = {
    getAccounts: (): ApiResponseType<Array<Account>> => apiWithAuthToken.get("/accounts"),
    logIn: (formData: AccountLogin): ApiResponseType<string> => apiForLogin.post("/auth/authenticate", formData),
}