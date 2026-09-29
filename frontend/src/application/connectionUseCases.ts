import type { Connection } from '@/domain/connection/Connection';
import type { ConnectionRepository } from '@/domain/connection/ConnectionRepository';

export function createConnectionUseCases(repository: ConnectionRepository) {
  return {
    listConnections(): Promise<Connection[]> {
      return repository.list();
    },

    requestConnection(memberId: string): Promise<Connection> {
      return repository.request(memberId);
    },

    acceptConnection(connectionId: string): Promise<Connection> {
      return repository.accept(connectionId);
    },

    removeConnection(connectionId: string): Promise<void> {
      return repository.remove(connectionId);
    },
  };
}

export type ConnectionUseCases = ReturnType<typeof createConnectionUseCases>;
