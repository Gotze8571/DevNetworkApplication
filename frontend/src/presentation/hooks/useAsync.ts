import { useCallback, useEffect, useState, type DependencyList, type Dispatch, type SetStateAction } from 'react';
import { errorMessage } from '@/presentation/errorMessage';

export interface AsyncState<T> {
  data: T | undefined;
  setData: Dispatch<SetStateAction<T | undefined>>;
  loading: boolean;
  error: string | null;
  reload: () => Promise<void>;
}

/** Runs a use case on mount (and when deps change) and tracks its loading and error state. */
export function useAsync<T>(load: () => Promise<T>, deps: DependencyList): AsyncState<T> {
  const [data, setData] = useState<T>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const run = useCallback(load, deps);

  const reload = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setData(await run());
    } catch (e) {
      setError(errorMessage(e, 'Failed to load'));
    } finally {
      setLoading(false);
    }
  }, [run]);

  useEffect(() => {
    void reload();
  }, [reload]);

  return { data, setData, loading, error, reload };
}
