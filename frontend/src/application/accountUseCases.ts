import { validateAccountUpdate, type Account, type AccountUpdate } from '@/domain/account/Account';
import type { AccountRepository } from '@/domain/account/AccountRepository';
import { ValidationError } from './ValidationError';

export function createAccountUseCases(repository: AccountRepository) {
  return {
    getAccount(): Promise<Account> {
      return repository.get();
    },

    async updateAccount(input: AccountUpdate): Promise<Account> {
      const errors = validateAccountUpdate(input);
      if (errors.length > 0) throw new ValidationError(errors);
      return repository.update(input);
    },
  };
}

export type AccountUseCases = ReturnType<typeof createAccountUseCases>;
