
// API Wrapper for all endpoint responses
export interface APIResponse<T> {
  isSuccess: boolean;
  message: string;
  data: T
}

// Page Response for pagination endpoints
export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}