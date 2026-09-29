import type { Connection } from '@/domain/connection/Connection';
import type { ConnectionRepository } from '@/domain/connection/ConnectionRepository';
import type { HttpClient } from '@/infrastructure/http/httpClient';

interface ConnectionDto extends Omit<Connection, 'createdAt' | 'respondedAt'> {
  createdAt: string;
  respondedAt: string | null;
}

const toDomain = (dto: ConnectionDto): Connection => ({
  ...dto,
  createdAt: new Date(dto.createdAt),
  respondedAt: dto.respondedAt ? new Date(dto.respondedAt) : null,
});

export function createHttpConnectionRepository(http: HttpClient): ConnectionRepository {
  return {
    async list() {
      return (await http.get<ConnectionDto[]>('/connections')).map(toDomain);
    },
    async request(memberId) {
      return toDomain(await http.post<ConnectionDto>('/connections', { userId: memberId }));
    },
    async accept(connectionId) {
      return toDomain(await http.post<ConnectionDto>(`/connections/${encodeURIComponent(connectionId)}/accept`));
    },
    remove(connectionId) {
      return http.delete(`/connections/${encodeURIComponent(connectionId)}`);
    },
  };
}
