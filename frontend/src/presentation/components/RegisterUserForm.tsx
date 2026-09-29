import { useState, type FormEvent } from 'react';
import type { NewUser } from '@/domain/user/User';
import styles from './RegisterUserForm.module.css';

interface Props {
  onSubmit: (input: NewUser) => Promise<unknown>;
}

export function RegisterUserForm({ onSubmit }: Props) {
  const [email, setEmail] = useState('');
  const [displayName, setDisplayName] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    try {
      await onSubmit({ email, displayName });
      setEmail('');
      setDisplayName('');
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Registration failed');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form className={styles.form} onSubmit={handleSubmit}>
      <input
        className={styles.input}
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
      />
      <input
        className={styles.input}
        placeholder="Display name"
        value={displayName}
        onChange={(e) => setDisplayName(e.target.value)}
        required
      />
      <button className={styles.button} type="submit" disabled={submitting}>
        {submitting ? 'Joining…' : 'Join'}
      </button>
      {error && <p className={styles.error}>{error}</p>}
    </form>
  );
}
