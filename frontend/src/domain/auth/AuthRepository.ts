import type { Credentials, CurrentUser, Registration } from './Auth';

/** Port: implemented in infrastructure against the session-based backend API. */
export interface AuthRepository {
  /** The signed-in user, or null if there is no valid session. */
  currentUser(): Promise<CurrentUser | null>;
  login(credentials: Credentials): Promise<CurrentUser>;
  register(registration: Registration): Promise<CurrentUser>;
  logout(): Promise<void>;
  /** Called whenever the server reports the session is gone. Returns an unsubscribe function. */
  onSessionExpired(listener: () => void): () => void;
}
