import { NavLink, Outlet, useNavigate } from 'react-router';
import { useAuth } from '@/presentation/auth/AuthProvider';
import styles from './Layout.module.css';
import ui from './ui.module.css';

const navClass = ({ isActive }: { isActive: boolean }) => `${styles.link} ${isActive ? styles.active : ''}`;

/** App shell for signed-in pages. */
export function Layout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  async function handleLogout() {
    await logout();
    navigate('/login', { replace: true });
  }

  return (
    <>
      <header className={styles.header}>
        <div className={styles.bar}>
          <NavLink to="/members" className={styles.brand}>
            DevNetwork
          </NavLink>
          <nav className={styles.nav} aria-label="Main">
            <NavLink to="/members" className={navClass}>
              Members
            </NavLink>
            <NavLink to="/connections" className={navClass}>
              Connections
            </NavLink>
            <NavLink to="/account" className={navClass}>
              My account
            </NavLink>
          </nav>
          <div className={styles.user}>
            <span>{user?.displayName}</span>
            <button type="button" className={ui.secondary} onClick={handleLogout}>
              Sign out
            </button>
          </div>
        </div>
      </header>
      <main className={styles.main}>
        <Outlet />
      </main>
    </>
  );
}
