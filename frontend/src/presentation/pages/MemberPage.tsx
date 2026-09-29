import { Link, useParams } from 'react-router';
import { useContainer } from '@/app/ContainerContext';
import { ConnectionButtons } from '@/presentation/components/ConnectionButtons';
import { useAsync } from '@/presentation/hooks/useAsync';
import { useConnections } from '@/presentation/hooks/useConnections';
import ui from '@/presentation/components/ui.module.css';
import styles from './MemberPage.module.css';

export function MemberPage() {
  const { id = '' } = useParams();
  const { members: useCases } = useContainer();
  const { data: member, loading, error } = useAsync(() => useCases.getMember(id), [useCases, id]);
  const { byMemberId, actions } = useConnections();

  const links = member
    ? [
        ['GitHub', member.githubUrl],
        ['LinkedIn', member.linkedinUrl],
        ['Website', member.websiteUrl],
      ].filter((link): link is [string, string] => Boolean(link[1]))
    : [];

  return (
    <>
      <Link to="/members" className={styles.back}>
        ← All members
      </Link>
      {loading && <p className={ui.muted}>Loading…</p>}
      {error && <p className={ui.error}>{error}</p>}
      {member && (
        <article className={`${ui.card} ${ui.stack}`}>
          <div className={styles.header}>
            <div>
              <h1 className={ui.pageTitle}>{member.displayName}</h1>
              <p className={ui.muted}>
                {[member.location, `Joined ${member.joinedAt.toLocaleDateString()}`].filter(Boolean).join(' · ')}
              </p>
            </div>
            <ConnectionButtons memberId={member.id} connection={byMemberId.get(member.id)} actions={actions} />
          </div>
          {member.headline && <p className={styles.headline}>{member.headline}</p>}
          {member.bio ? (
            <p className={styles.bio}>{member.bio}</p>
          ) : (
            <p className={ui.muted}>{member.displayName} hasn't written a bio yet.</p>
          )}
          {links.length > 0 && (
            <ul className={styles.links}>
              {links.map(([label, url]) => (
                <li key={label}>
                  <a href={url} target="_blank" rel="noopener noreferrer">
                    {label}
                  </a>
                </li>
              ))}
            </ul>
          )}
        </article>
      )}
    </>
  );
}
