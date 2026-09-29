import type { Account } from '@/domain/account/Account';
import type { AccountRepository } from '@/domain/account/AccountRepository';
import type { HttpClient } from '@/infrastructure/http/httpClient';

interface AccountDto extends Omit<Account, 'createdAt' | 'updatedAt'> {
  createdAt: string;
  updatedAt: string | null;
}

const toDomain = (dto: AccountDto): Account => ({
  ...dto,
  createdAt: new Date(dto.createdAt),
  updatedAt: dto.updatedAt ? new Date(dto.updatedAt) : null,
});

export function createHttpAccountRepository(http: HttpClient): AccountRepository {
  return {
    async get() {
      return toDomain(await http.get<AccountDto>('/account'));
    },
    async update(update) {
      return toDomain(await http.put<AccountDto>('/account', update));
    },
  };
}
