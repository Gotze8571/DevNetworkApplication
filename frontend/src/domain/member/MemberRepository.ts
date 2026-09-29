import type { MemberProfile, MemberSummary } from './Member';

export interface MemberRepository {
  /** Everyone except the signed-in user. */
  list(): Promise<MemberSummary[]>;
  get(id: string): Promise<MemberProfile>;
}
