import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from 'react';
import { useContainer } from '@/app/ContainerContext';
import type { Credentials, CurrentUser, Registration } from '@/domain/auth/Auth';

interface AuthContextValue {
  /** undefined while the initial session check is running, null when signed out. */
  user: CurrentUser | null | undefined;
  /** True after the server reported the session gone while the user was signed in. */
  sessionExpired: boolean;
  login(credentials: Credentials): Promise<void>;
  register(registration: Registration): Promise<void>;
  logout(): Promise<void>;
  /** Keeps the header in sync after the user edits their account. */
  updateUser(changes: Partial<CurrentUser>): void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const { auth } = useContainer();
  const [user, setUser] = useState<CurrentUser | null | undefined>(undefined);
  const [sessionExpired, setSessionExpired] = useState(false);

  // Restore the session (if the cookie is still valid) when the app loads.
  useEffect(() => {
    let cancelled = false;
    auth
      .currentUser()
      .then((current) => !cancelled && setUser(current))
      .catch(() => !cancelled && setUser(null));
    return () => {
      cancelled = true;
    };
  }, [auth]);

  // Any 401 from the API means the session expired or was ended elsewhere.
  useEffect(
    () =>
      auth.onSessionExpired(() => {
        setUser((current) => {
          if (current) setSessionExpired(true);
          return null;
        });
      }),
    [auth],
  );

  const login = useCallback(
    async (credentials: Credentials) => {
      setUser(await auth.login(credentials));
      setSessionExpired(false);
    },
    [auth],
  );

  const register = useCallback(
    async (registration: Registration) => {
      setUser(await auth.register(registration));
      setSessionExpired(false);
    },
    [auth],
  );

  const logout = useCallback(async () => {
    try {
      await auth.logout();
    } finally {
      setUser(null);
      setSessionExpired(false);
    }
  }, [auth]);

  const updateUser = useCallback((changes: Partial<CurrentUser>) => {
    setUser((current) => (current ? { ...current, ...changes } : current));
  }, []);

  const value = useMemo(
    () => ({ user, sessionExpired, login, register, logout, updateUser }),
    [user, sessionExpired, login, register, logout, updateUser],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used inside <AuthProvider>');
  return context;
}
