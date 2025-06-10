import axios from "axios";

export const axiosInstance = axios.create({
  baseURL: "http://localhost:8484/api", // Hardcoded as of now. Migrate to Env later.
  withCredentials: true
});
