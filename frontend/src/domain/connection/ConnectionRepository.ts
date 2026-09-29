import type { Connection } from './Connection';

export interface ConnectionRepository {
  list(): Promise<Connection[]>;
  /** Sends a request, or accepts theirs if they already sent one. */
  request(memberId: string): Promise<Connection>;
  accept(connectionId: string): Promise<Connection>;
  /** Declines, cancels or removes, depending on the connection's state. */
  remove(connectionId: string): Promise<void>;
}
