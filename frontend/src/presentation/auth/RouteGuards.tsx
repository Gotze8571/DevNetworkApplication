import { Navigate, Outlet, useLocation } from 'react-router';
import { useAuth } from './AuthProvider';

function Checking() {
  return <p style={{ padding: 32, textAlign: 'center' }}>Loading…</p>;
}

/** Only renders its routes for signed-in users; everyone else is sent to the login page. */
export function RequireAuth() {
  const { user } = useAuth();
  const location = useLocation();
  if (user === undefined) return <Checking />;
  if (!user) return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  return <Outlet />;
}

/** Login and registration pages: signed-in users skip straight into the app. */
export function RedirectIfSignedIn() {
  const { user } = useAuth();
  if (user === undefined) return <Checking />;
  if (user) return <Navigate to="/members" replace />;
  return <Outlet />;
}
