import { createAccountUseCases } from '@/application/accountUseCases';
import { createAuthUseCases } from '@/application/authUseCases';
import { createConnectionUseCases } from '@/application/connectionUseCases';
import { createMemberUseCases } from '@/application/memberUseCases';
import { env } from '@/config/env';
import { createHttpAccountRepository } from '@/infrastructure/account/HttpAccountRepository';
import { createHttpAuthRepository } from '@/infrastructure/auth/HttpAuthRepository';
import { createHttpConnectionRepository } from '@/infrastructure/connection/HttpConnectionRepository';
import { createHttpClient } from '@/infrastructure/http/httpClient';
import { createHttpMemberRepository } from '@/infrastructure/member/HttpMemberRepository';

/**
 * Composition root: the only place that knows which infrastructure implements which port.
 * Swap implementations here (e.g. in tests or Storybook) without touching the UI.
 */
export function createContainer() {
  const http = createHttpClient(env.apiBaseUrl);

  return {
    auth: createAuthUseCases(createHttpAuthRepository(http)),
    account: createAccountUseCases(createHttpAccountRepository(http)),
    members: createMemberUseCases(createHttpMemberRepository(http)),
    connections: createConnectionUseCases(createHttpConnectionRepository(http)),
  };
}

export type Container = ReturnType<typeof createContainer>;
