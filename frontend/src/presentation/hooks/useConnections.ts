import { useCallback, useMemo } from 'react';
import { useContainer } from '@/app/ContainerContext';
import type { Connection } from '@/domain/connection/Connection';
import { useAsync } from './useAsync';

export interface ConnectionActions {
  request(memberId: string): Promise<void>;
  accept(connectionId: string): Promise<void>;
  remove(connectionId: string): Promise<void>;
}

/** The signed-in user's connections, plus actions that keep the list in sync. */
export function useConnections() {
  const { connections: useCases } = useContainer();
  const { data, setData, loading, error, reload } = useAsync(() => useCases.listConnections(), [useCases]);

  const connections = useMemo(() => data ?? [], [data]);
  const byMemberId = useMemo(() => new Map(connections.map((c) => [c.member.id, c])), [connections]);

  const upsert = useCallback(
    (connection: Connection) =>
      setData((current = []) => [connection, ...current.filter((c) => c.id !== connection.id)]),
    [setData],
  );

  // If an action fails (e.g. the other member acted at the same time), reload so the UI matches the server.
  const withReloadOnError = useCallback(
    async (action: () => Promise<void>) => {
      try {
        await action();
      } catch (e) {
        void reload();
        throw e;
      }
    },
    [reload],
  );

  const actions: ConnectionActions = useMemo(
    () => ({
      request: (memberId) => withReloadOnError(async () => upsert(await useCases.requestConnection(memberId))),
      accept: (connectionId) => withReloadOnError(async () => upsert(await useCases.acceptConnection(connectionId))),
      remove: (connectionId) =>
        withReloadOnError(async () => {
          await useCases.removeConnection(connectionId);
          setData((current = []) => current.filter((c) => c.id !== connectionId));
        }),
    }),
    [useCases, upsert, withReloadOnError, setData],
  );

  return { connections, byMemberId, loading, error, reload, actions };
}
