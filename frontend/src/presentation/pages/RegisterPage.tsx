import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router';
import { MIN_PASSWORD_LENGTH } from '@/domain/auth/Auth';
import { useAuth } from '@/presentation/auth/AuthProvider';
import { errorMessage } from '@/presentation/errorMessage';
import ui from '@/presentation/components/ui.module.css';
import styles from './AuthPage.module.css';

export function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState('');
  const [displayName, setDisplayName] = useState('');
  const [password, setPassword] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    try {
      await register({ email, displayName, password });
      // New users start by filling in their profile.
      navigate('/account', { replace: true });
    } catch (e) {
      setError(errorMessage(e, 'Registration failed'));
      setSubmitting(false);
    }
  }

  return (
    <main className={styles.page}>
      <div className={styles.panel}>
        <h1 className={styles.brand}>DevNetwork</h1>
        <p className={styles.tagline}>Create your developer account.</p>

        <form className={`${ui.card} ${ui.stack}`} onSubmit={handleSubmit}>
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
            Display name
            <input
              className={ui.input}
              autoComplete="name"
              value={displayName}
              onChange={(e) => setDisplayName(e.target.value)}
              maxLength={100}
              required
            />
          </label>
          <label className={ui.field}>
            <span>
              Password <span className={ui.hint}>(at least {MIN_PASSWORD_LENGTH} characters)</span>
            </span>
            <input
              className={ui.input}
              type="password"
              autoComplete="new-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              minLength={MIN_PASSWORD_LENGTH}
              required
            />
          </label>
          {error && (
            <p className={ui.error} role="alert">
              {error}
            </p>
          )}
          <button className={ui.button} type="submit" disabled={submitting}>
            {submitting ? 'Creating account…' : 'Create account'}
          </button>
        </form>

        <p className={styles.switch}>
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </div>
    </main>
  );
}
