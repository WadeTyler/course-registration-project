import axios from "axios";

// Determine the base URL - in production it should come from env var, in development from hardcoded value
const baseURL = import.meta.env.VITE_API_URL || "http://localhost:8484/api";

console.log("API URL:", baseURL); // Helpful for debugging

export const axiosInstance = axios.create({
  baseURL,
  withCredentials: true
});
