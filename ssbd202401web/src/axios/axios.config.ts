import axios from "axios";

const API_URL = "http://localhost:8080";
const TIMEOUT_MS = 30000;
const DEFAULT_HEADERS = {
    Accept: "application/json",
    "Content-Type": "application/json"
};

export const defaultApi = axios.create({
    baseURL: API_URL,
    timeout: TIMEOUT_MS,
    headers: DEFAULT_HEADERS
});