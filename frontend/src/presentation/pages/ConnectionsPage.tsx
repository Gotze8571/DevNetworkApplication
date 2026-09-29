import { Link } from 'react-router';
import type { Connection, ConnectionState } from '@/domain/connection/Connection';
import { ConnectionButtons } from '@/presentation/components/ConnectionButtons';
import { useConnections, type ConnectionActions } from '@/presentation/hooks/useConnections';
import ui from '@/presentation/components/ui.module.css';

const SECTIONS: { state: ConnectionState; title: string; empty: string }[] = [
  { state: 'INCOMING', title: 'Requests for you', empty: 'No requests waiting for you.' },
  { state: 'CONNECTED', title: 'Your connections', empty: 'No connections yet. Find people on the Members page.' },
  { state: 'OUTGOING', title: 'Sent requests', empty: 'No pending requests from you.' },
];

export function ConnectionsPage() {
  const { connections, loading, error, actions } = useConnections();

  return (
    <>
      <h1 className={ui.pageTitle}>Connections</h1>
      <p className={ui.muted}>Answer requests and manage the people you're connected with.</p>

      {loading && <p className={ui.muted}>Loading…</p>}
      {error && <p className={ui.error}>{error}</p>}
      {!loading &&
        !error &&
        SECTIONS.map((section) => {
          const items = connections.filter((c) => c.state === section.state);
          return (
            <section key={section.state} className={ui.section}>
              <h2 className={ui.sectionTitle}>
                {section.title} <span className={ui.hint}>({items.length})</span>
              </h2>
              {items.length === 0 ? (
                <p className={ui.muted}>{section.empty}</p>
              ) : (
                <ul className={ui.list}>
                  {items.map((connection) => (
                    <ConnectionItem key={connection.id} connection={connection} actions={actions} />
                  ))}
                </ul>
              )}
            </section>
          );
        })}
    </>
  );
}

function ConnectionItem({ connection, actions }: { connection: Connection; actions: ConnectionActions }) {
  const since = connection.state === 'CONNECTED' ? connection.respondedAt : connection.createdAt;
  return (
    <li className={ui.listItem}>
      <div>
        <Link to={`/members/${connection.member.id}`} className={ui.name}>
          {connection.member.displayName}
        </Link>
        <p className={ui.muted}>
          {[connection.member.headline, since && `since ${since.toLocaleDateString()}`].filter(Boolean).join(' · ')}
        </p>
      </div>
      <ConnectionButtons memberId={connection.member.id} connection={connection} actions={actions} />
    </li>
  );
}
