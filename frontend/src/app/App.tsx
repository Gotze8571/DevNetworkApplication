import { BrowserRouter, Navigate, Route, Routes } from 'react-router';
import { AuthProvider } from '@/presentation/auth/AuthProvider';
import { RedirectIfSignedIn, RequireAuth } from '@/presentation/auth/RouteGuards';
import { Layout } from '@/presentation/components/Layout';
import { AccountPage } from '@/presentation/pages/AccountPage';
import { ConnectionsPage } from '@/presentation/pages/ConnectionsPage';
import { LoginPage } from '@/presentation/pages/LoginPage';
import { MemberPage } from '@/presentation/pages/MemberPage';
import { MembersPage } from '@/presentation/pages/MembersPage';
import { NotFoundPage } from '@/presentation/pages/NotFoundPage';
import { RegisterPage } from '@/presentation/pages/RegisterPage';
import { createContainer } from './container';
import { ContainerProvider } from './ContainerContext';

const container = createContainer();

export function App() {
  return (
    <ContainerProvider container={container}>
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            <Route element={<RedirectIfSignedIn />}>
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
            </Route>
            <Route element={<RequireAuth />}>
              <Route element={<Layout />}>
                <Route index element={<Navigate to="/members" replace />} />
                <Route path="/members" element={<MembersPage />} />
                <Route path="/members/:id" element={<MemberPage />} />
                <Route path="/connections" element={<ConnectionsPage />} />
                <Route path="/account" element={<AccountPage />} />
              </Route>
            </Route>
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </ContainerProvider>
  );
}
