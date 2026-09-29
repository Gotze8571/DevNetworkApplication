import type { Account, AccountUpdate } from './Account';

export interface AccountRepository {
  get(): Promise<Account>;
  update(update: AccountUpdate): Promise<Account>;
}
