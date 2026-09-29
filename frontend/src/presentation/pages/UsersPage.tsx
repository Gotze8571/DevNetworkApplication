import { RegisterUserForm } from '@/presentation/components/RegisterUserForm';
import { UserList } from '@/presentation/components/UserList';
import { useUsers } from '@/presentation/hooks/useUsers';
import styles from './UsersPage.module.css';

export function UsersPage() {
  const { users, loading, error, register } = useUsers();

  return (
    <main className={styles.page}>
      <h1 className={styles.title}>DevNetwork</h1>
      <p className={styles.subtitle}>Connect with other developers.</p>

      <section className={styles.section}>
        <h2>Join the network</h2>
        <RegisterUserForm onSubmit={register} />
      </section>

      <section className={styles.section}>
        <h2>Developers</h2>
        {loading && <p>Loading…</p>}
        {error && <p className={styles.error}>{error}</p>}
        {!loading && !error && <UserList users={users} />}
      </section>
    </main>
  );
}
