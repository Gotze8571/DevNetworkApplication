import { useCallback, useEffect, useState } from 'react';
import { useContainer } from '@/app/ContainerContext';
import type { NewUser, User } from '@/domain/user/User';

/** Adapts the user use cases to React state. Components use this, never the repository directly. */
export function useUsers() {
  const { users: useCases } = useContainer();
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const refresh = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setUsers(await useCases.listUsers());
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Failed to load users');
    } finally {
      setLoading(false);
    }
  }, [useCases]);

  const register = useCallback(
    async (input: NewUser) => {
      const created = await useCases.registerUser(input);
      setUsers((current) => [...current, created]);
      return created;
    },
    [useCases],
  );

  useEffect(() => {
    void refresh();
  }, [refresh]);

  return { users, loading, error, refresh, register };
}
