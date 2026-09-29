import { useState, type FormEvent } from 'react';
import { Link, useLocation, useNavigate } from 'react-router';
import { useAuth } from '@/presentation/auth/AuthProvider';
import { errorMessage } from '@/presentation/errorMessage';
import ui from '@/presentation/components/ui.module.css';
import styles from './AuthPage.module.css';

export function LoginPage() {
  const { login, sessionExpired } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = (location.state as { from?: string } | null)?.from ?? '/members';

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    try {
      await login({ email, password });
      navigate(from, { replace: true });
    } catch (e) {
      setError(errorMessage(e, 'Sign in failed'));
      setSubmitting(false);
    }
  }

  return (
    <main className={styles.page}>
      <div className={styles.panel}>
        <h1 className={styles.brand}>DevNetwork</h1>
        <p className={styles.tagline}>Sign in to connect with other developers.</p>

        <form className={`${ui.card} ${ui.stack}`} onSubmit={handleSubmit}>
          {sessionExpired && <p className={styles.notice}>Your session has ended. Please sign in again.</p>}
          <label className={ui.field}>
            Email
            <input
              className={ui.input}
              type="email"
              autoComplete="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              autoFocus
            />
          </label>
          <label className={ui.field}>
            Password
            <input
              className={ui.input}
              type="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </label>
          {error && (
            <p className={ui.error} role="alert">
              {error}
            </p>
          )}
          <button className={ui.button} type="submit" disabled={submitting}>
            {submitting ? 'Signing in…' : 'Sign in'}
          </button>
        </form>

        <p className={styles.switch}>
          New here? <Link to="/register">Create an account</Link>
        </p>
      </div>
    </main>
  );
}
