import type { MemberProfile, MemberSummary } from '@/domain/member/Member';
import type { MemberRepository } from '@/domain/member/MemberRepository';
import type { HttpClient } from '@/infrastructure/http/httpClient';

interface MemberProfileDto extends Omit<MemberProfile, 'joinedAt'> {
  joinedAt: string;
}

export function createHttpMemberRepository(http: HttpClient): MemberRepository {
  return {
    list() {
      return http.get<MemberSummary[]>('/members');
    },
    async get(id) {
      const dto = await http.get<MemberProfileDto>(`/members/${encodeURIComponent(id)}`);
      return { ...dto, joinedAt: new Date(dto.joinedAt) };
    },
  };
}
