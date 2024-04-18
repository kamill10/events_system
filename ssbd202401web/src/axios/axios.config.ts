import axios from "axios";
import {ApiResponseType} from "../types/ApiResponse.ts";
import {Account, AccountLogin} from "../types/Account.ts";
import {useEtag, useToken} from "../atoms/tokens.ts";

const API_URL: string = "https://team-1.proj-sum.it.p.lodz.pl/api";
const TIMEOUT_MS: number = 30000;

const getEtag = (): string => {
    // use hook from atoms/etags.ts
    const [etag, _] = useEtag();
    return etag ?? "";
};

const setEtag = (newEtag: string) => {
    // use hook from atoms/etags.ts
    const [_, setEtag] = useEtag();
    setEtag(newEtag);
}

const getAuthToken = () : string => {
    const [token, _] = useToken();
    return "Bearer " + (token ?? "");
};

const setToken = (newToken: string) => {
    const [_, setToken] = useToken();
    setToken(newToken);
}

const DEFAULT_HEADERS = {
    Accept: "application/json",
    "Content-Type": "application/json",
    Authorization: getAuthToken(),
};

const ETAG_HEADERS = {
    Accept: "application/json",
    "Content-Type": "application/json",
    Authorization: getAuthToken(),
    "If-Match": getEtag(),
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
        config.headers.Authorization = getAuthToken();
        return config;
    },
    (error) => {
        // Handle request error
        return Promise.reject(error);
    }
);

apiWithAuthToken.interceptors.response.use(
    (response) => {
        setToken(response.headers.authorization);
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
        config.headers.Authorization = getAuthToken();
        config.headers['If-Match'] = getEtag();
        return config;
    },
    (error) => {
        // Handle request error
        return Promise.reject(error);
    }
);

apiWithEtag.interceptors.response.use(
    (response) => {
        setEtag(response.headers.etag);
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