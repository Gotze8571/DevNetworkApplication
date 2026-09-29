import type { MemberProfile, MemberSummary } from '@/domain/member/Member';
import type { MemberRepository } from '@/domain/member/MemberRepository';

export function createMemberUseCases(repository: MemberRepository) {
  return {
    listMembers(): Promise<MemberSummary[]> {
      return repository.list();
    },

    getMember(id: string): Promise<MemberProfile> {
      return repository.get(id);
    },
  };
}

export type MemberUseCases = ReturnType<typeof createMemberUseCases>;
