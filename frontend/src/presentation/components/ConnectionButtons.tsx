import { useState } from 'react';
import type { Connection } from '@/domain/connection/Connection';
import type { ConnectionActions } from '@/presentation/hooks/useConnections';
import { errorMessage } from '@/presentation/errorMessage';
import ui from './ui.module.css';

interface Props {
  memberId: string;
  /** The current connection with this member, if any. */
  connection: Connection | undefined;
  actions: ConnectionActions;
}

/** The right connection action for a member, depending on where things stand with them. */
export function ConnectionButtons({ memberId, connection, actions }: Props) {
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function run(action: () => Promise<void>) {
    setBusy(true);
    setError(null);
    try {
      await action();
    } catch (e) {
      setError(errorMessage(e));
    } finally {
      setBusy(false);
    }
  }

  let buttons;
  if (!connection) {
    buttons = (
      <button type="button" className={ui.button} disabled={busy} onClick={() => run(() => actions.request(memberId))}>
        Connect
      </button>
    );
  } else if (connection.state === 'INCOMING') {
    buttons = (
      <>
        <button type="button" className={ui.button} disabled={busy} onClick={() => run(() => actions.accept(connection.id))}>
          Accept
        </button>
        <button type="button" className={ui.secondary} disabled={busy} onClick={() => run(() => actions.remove(connection.id))}>
          Decline
        </button>
      </>
    );
  } else if (connection.state === 'OUTGOING') {
    buttons = (
      <>
        <span className={ui.muted}>Request sent</span>
        <button type="button" className={ui.secondary} disabled={busy} onClick={() => run(() => actions.remove(connection.id))}>
          Cancel
        </button>
      </>
    );
  } else {
    buttons = (
      <>
        <span className={ui.badge}>Connected</span>
        <button
          type="button"
          className={ui.danger}
          disabled={busy}
          onClick={() => {
            if (window.confirm(`Remove ${connection.member.displayName} from your connections?`)) {
              void run(() => actions.remove(connection.id));
            }
          }}
        >
          Remove
        </button>
      </>
    );
  }

  return (
    <div className={ui.row}>
      {buttons}
      {error && (
        <p className={ui.error} role="alert">
          {error}
        </p>
      )}
    </div>
  );
}
