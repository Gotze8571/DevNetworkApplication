import { validateRegistration, type Credentials, type CurrentUser, type Registration } from '@/domain/auth/Auth';
import type { AuthRepository } from '@/domain/auth/AuthRepository';
import { ValidationError } from './ValidationError';

export function createAuthUseCases(repository: AuthRepository) {
  return {
    currentUser(): Promise<CurrentUser | null> {
      return repository.currentUser();
    },

    login(credentials: Credentials): Promise<CurrentUser> {
      if (!credentials.email.trim() || !credentials.password) {
        throw new ValidationError(['Email and password are required']);
      }
      return repository.login({ email: credentials.email.trim(), password: credentials.password });
    },

    register(input: Registration): Promise<CurrentUser> {
      const errors = validateRegistration(input);
      if (errors.length > 0) throw new ValidationError(errors);
      return repository.register({ ...input, email: input.email.trim(), displayName: input.displayName.trim() });
    },

    logout(): Promise<void> {
      return repository.logout();
    },

    onSessionExpired(listener: () => void): () => void {
      return repository.onSessionExpired(listener);
    },
  };
}

export type AuthUseCases = ReturnType<typeof createAuthUseCases>;
