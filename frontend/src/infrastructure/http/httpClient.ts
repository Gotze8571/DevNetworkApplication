export class HttpError extends Error {
  constructor(
    readonly status: number,
    message: string,
  ) {
    super(message);
    this.name = 'HttpError';
  }
}

export interface HttpClient {
  get<T>(path: string): Promise<T>;
  post<T>(path: string, body?: unknown): Promise<T>;
  put<T>(path: string, body: unknown): Promise<T>;
  delete(path: string): Promise<void>;
  /** Called on every 401 response. Returns an unsubscribe function. */
  onUnauthorized(listener: () => void): () => void;
}

// The backend sets this readable cookie; state-changing requests must echo it in the header (CSRF protection).
const CSRF_COOKIE = 'XSRF-TOKEN';
const CSRF_HEADER = 'X-XSRF-TOKEN';

function readCookie(name: string): string | undefined {
  const entry = document.cookie.split('; ').find((cookie) => cookie.startsWith(`${name}=`));
  return entry ? decodeURIComponent(entry.slice(name.length + 1)) : undefined;
}

/**
 * JSON client for the backend. Auth is a same-origin HttpOnly session cookie, which the browser sends
 * automatically, so this client never sees or stores credentials itself.
 */
export function createHttpClient(baseUrl: string): HttpClient {
  const unauthorizedListeners = new Set<() => void>();

  async function csrfToken(): Promise<string | undefined> {
    // The cookie is cleared on logout; fetch a fresh one before the next state-changing request.
    if (!readCookie(CSRF_COOKIE)) {
      await fetch(`${baseUrl}/auth/csrf`, { credentials: 'same-origin' });
    }
    return readCookie(CSRF_COOKIE);
  }

  async function request<T>(method: string, path: string, body?: unknown): Promise<T> {
    const headers: Record<string, string> = { Accept: 'application/json' };
    if (body !== undefined) headers['Content-Type'] = 'application/json';
    if (method !== 'GET') {
      const token = await csrfToken();
      if (token) headers[CSRF_HEADER] = token;
    }

    const response = await fetch(`${baseUrl}${path}`, {
      method,
      headers,
      credentials: 'same-origin',
      body: body === undefined ? undefined : JSON.stringify(body),
    });

    if (!response.ok) {
      // The backend returns RFC 7807 problem details with a "detail" field.
      const problem = await response.json().catch(() => null);
      if (response.status === 401) unauthorizedListeners.forEach((listener) => listener());
      throw new HttpError(response.status, problem?.detail ?? response.statusText);
    }
    if (response.status === 204) return undefined as T;
    return response.json() as Promise<T>;
  }

  return {
    get: (path) => request('GET', path),
    post: (path, body) => request('POST', path, body),
    put: (path, body) => request('PUT', path, body),
    delete: (path) => request('DELETE', path),
    onUnauthorized(listener) {
      unauthorizedListeners.add(listener);
      return () => unauthorizedListeners.delete(listener);
    },
  };
}
