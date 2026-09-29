import { validateNewUser, type NewUser, type User } from '@/domain/user/User';
import type { UserRepository } from '@/domain/user/UserRepository';

export class ValidationError extends Error {
  constructor(readonly errors: string[]) {
    super(errors.join(', '));
    this.name = 'ValidationError';
  }
}

/** Use cases depend only on the domain; the repository is injected. */
export function createUserUseCases(repository: UserRepository) {
  return {
    listUsers(): Promise<User[]> {
      return repository.findAll();
    },

    getUser(id: string): Promise<User> {
      return repository.findById(id);
    },

    async registerUser(input: NewUser): Promise<User> {
      const errors = validateNewUser(input);
      if (errors.length > 0) throw new ValidationError(errors);
      return repository.create({ email: input.email.trim(), displayName: input.displayName.trim() });
    },
  };
}

export type UserUseCases = ReturnType<typeof createUserUseCases>;
