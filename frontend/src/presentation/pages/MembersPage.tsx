import { Link } from 'react-router';
import { useContainer } from '@/app/ContainerContext';
import { ConnectionButtons } from '@/presentation/components/ConnectionButtons';
import { useAsync } from '@/presentation/hooks/useAsync';
import { useConnections } from '@/presentation/hooks/useConnections';
import ui from '@/presentation/components/ui.module.css';

export function MembersPage() {
  const { members: useCases } = useContainer();
  const members = useAsync(() => useCases.listMembers(), [useCases]);
  const { byMemberId, actions, loading: connectionsLoading } = useConnections();

  const loading = members.loading || connectionsLoading;

  return (
    <>
      <h1 className={ui.pageTitle}>Members</h1>
      <p className={ui.muted}>Find developers and send them a connection request.</p>

      <section className={ui.section}>
        {loading && <p className={ui.muted}>Loading…</p>}
        {members.error && <p className={ui.error}>{members.error}</p>}
        {!loading && !members.error && members.data?.length === 0 && (
          <p className={ui.muted}>No other members yet. Invite a colleague to sign up.</p>
        )}
        {!loading && !members.error && (
          <ul className={ui.list}>
            {members.data?.map((member) => (
              <li key={member.id} className={ui.listItem}>
                <div>
                  <Link to={`/members/${member.id}`} className={ui.name}>
                    {member.displayName}
                  </Link>
                  {member.headline && <p className={ui.muted}>{member.headline}</p>}
                </div>
                <ConnectionButtons memberId={member.id} connection={byMemberId.get(member.id)} actions={actions} />
              </li>
            ))}
          </ul>
        )}
      </section>
    </>
  );
}
