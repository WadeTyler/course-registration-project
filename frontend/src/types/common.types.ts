
/*
 * Response wrapper for API Errors.
 */
export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
}

/*
 * Page Response for pagination endpoints
 */
export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}

export interface Pageable {
  page?: number;
  size?: number;
  sort?: "asc" | "desc";
}