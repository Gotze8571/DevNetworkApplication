import type { MemberSummary } from '@/domain/member/Member';

/**
 * CONNECTED: both users are connected.
 * INCOMING: the other member asked to connect and is waiting for my answer.
 * OUTGOING: I asked to connect and am waiting for theirs.
 */
export type ConnectionState = 'CONNECTED' | 'INCOMING' | 'OUTGOING';

export interface Connection {
  id: string;
  member: MemberSummary;
  state: ConnectionState;
  createdAt: Date;
  respondedAt: Date | null;
}
