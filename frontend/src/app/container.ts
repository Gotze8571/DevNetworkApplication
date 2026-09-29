import { createUserUseCases } from '@/application/user/userUseCases';
import { env } from '@/config/env';
import { createHttpClient } from '@/infrastructure/http/httpClient';
import { createHttpUserRepository } from '@/infrastructure/user/HttpUserRepository';

/**
 * Composition root: the only place that knows which infrastructure implements which port.
 * Swap implementations here (e.g. in tests or Storybook) without touching the UI.
 */
export function createContainer() {
  const http = createHttpClient(env.apiBaseUrl);

  return {
    users: createUserUseCases(createHttpUserRepository(http)),
  };
}

export type Container = ReturnType<typeof createContainer>;
