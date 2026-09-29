import { Link } from 'react-router';
import ui from '@/presentation/components/ui.module.css';

export function NotFoundPage() {
  return (
    <main style={{ padding: '64px 16px', textAlign: 'center' }}>
      <h1 className={ui.pageTitle}>Page not found</h1>
      <p className={ui.muted}>
        <Link to="/members">Go to the members list</Link>
      </p>
    </main>
  );
}
