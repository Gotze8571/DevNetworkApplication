import type { User } from '@/domain/user/User';
import styles from './UserList.module.css';

export function UserList({ users }: { users: User[] }) {
  if (users.length === 0) {
    return <p className={styles.empty}>No developers yet. Be the first to join.</p>;
  }

  return (
    <ul className={styles.list}>
      {users.map((user) => (
        <li key={user.id} className={styles.item}>
          <span className={styles.name}>{user.displayName}</span>
          <span className={styles.meta}>
            {user.email} · joined {user.createdAt.toLocaleDateString()}
          </span>
        </li>
      ))}
    </ul>
  );
}
