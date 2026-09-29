import { useEffect, useState, type FormEvent } from 'react';
import { useContainer } from '@/app/ContainerContext';
import { ACCOUNT_LIMITS, type Account, type AccountUpdate } from '@/domain/account/Account';
import { useAuth } from '@/presentation/auth/AuthProvider';
import { errorMessage } from '@/presentation/errorMessage';
import { useAsync } from '@/presentation/hooks/useAsync';
import ui from '@/presentation/components/ui.module.css';

const toForm = (account: Account): AccountUpdate => ({
  displayName: account.displayName,
  headline: account.headline ?? '',
  bio: account.bio ?? '',
  location: account.location ?? '',
  githubUrl: account.githubUrl ?? '',
  linkedinUrl: account.linkedinUrl ?? '',
  websiteUrl: account.websiteUrl ?? '',
});

export function AccountPage() {
  const { account: useCases } = useContainer();
  const { updateUser } = useAuth();
  const { data: account, setData: setAccount, loading, error: loadError } = useAsync(
    () => useCases.getAccount(),
    [useCases],
  );

  const [form, setForm] = useState<AccountUpdate | null>(null);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [saved, setSaved] = useState(false);

  useEffect(() => {
    if (account) setForm(toForm(account));
  }, [account]);

  function set<K extends keyof AccountUpdate>(field: K, value: string) {
    setForm((current) => (current ? { ...current, [field]: value } : current));
    setSaved(false);
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    if (!form) return;
    setSaving(true);
    setError(null);
    try {
      const updated = await useCases.updateAccount(form);
      setAccount(updated);
      updateUser({ displayName: updated.displayName });
      setSaved(true);
    } catch (e) {
      setError(errorMessage(e, 'Could not save your changes'));
    } finally {
      setSaving(false);
    }
  }

  return (
    <>
      <h1 className={ui.pageTitle}>My account</h1>
      <p className={ui.muted}>This is what other members see on your profile. Your email stays private.</p>

      {loading && <p className={ui.muted}>Loading…</p>}
      {loadError && <p className={ui.error}>{loadError}</p>}
      {account && form && (
        <form className={`${ui.card} ${ui.stack} ${ui.section}`} onSubmit={handleSubmit}>
          <p className={ui.muted}>
            Signed in as <strong>{account.email}</strong> · member since {account.createdAt.toLocaleDateString()}
          </p>

          <label className={ui.field}>
            Display name
            <input
              className={ui.input}
              value={form.displayName}
              onChange={(e) => set('displayName', e.target.value)}
              maxLength={ACCOUNT_LIMITS.displayName}
              required
            />
          </label>
          <label className={ui.field}>
            Headline
            <input
              className={ui.input}
              placeholder="e.g. Backend engineer working with Java and Postgres"
              value={form.headline}
              onChange={(e) => set('headline', e.target.value)}
              maxLength={ACCOUNT_LIMITS.headline}
            />
          </label>
          <label className={ui.field}>
            Location
            <input
              className={ui.input}
              value={form.location}
              onChange={(e) => set('location', e.target.value)}
              maxLength={ACCOUNT_LIMITS.location}
            />
          </label>
          <label className={ui.field}>
            <span>
              Bio <span className={ui.hint}>({form.bio.length}/{ACCOUNT_LIMITS.bio})</span>
            </span>
            <textarea
              className={ui.input}
              value={form.bio}
              onChange={(e) => set('bio', e.target.value)}
              maxLength={ACCOUNT_LIMITS.bio}
            />
          </label>
          <label className={ui.field}>
            GitHub
            <input
              className={ui.input}
              type="url"
              placeholder="https://github.com/you"
              value={form.githubUrl}
              onChange={(e) => set('githubUrl', e.target.value)}
              maxLength={ACCOUNT_LIMITS.url}
            />
          </label>
          <label className={ui.field}>
            LinkedIn
            <input
              className={ui.input}
              type="url"
              placeholder="https://www.linkedin.com/in/you"
              value={form.linkedinUrl}
              onChange={(e) => set('linkedinUrl', e.target.value)}
              maxLength={ACCOUNT_LIMITS.url}
            />
          </label>
          <label className={ui.field}>
            Website
            <input
              className={ui.input}
              type="url"
              placeholder="https://your-site.dev"
              value={form.websiteUrl}
              onChange={(e) => set('websiteUrl', e.target.value)}
              maxLength={ACCOUNT_LIMITS.url}
            />
          </label>

          {error && (
            <p className={ui.error} role="alert">
              {error}
            </p>
          )}
          <div className={ui.row}>
            <button className={ui.button} type="submit" disabled={saving}>
              {saving ? 'Saving…' : 'Save changes'}
            </button>
            {saved && (
              <p className={ui.success} role="status">
                Saved.
              </p>
            )}
          </div>
        </form>
      )}
    </>
  );
}
