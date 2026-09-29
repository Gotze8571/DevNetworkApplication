import type { NewUser, User } from '@/domain/user/User';
import type { UserRepository } from '@/domain/user/UserRepository';
import type { HttpClient } from '@/infrastructure/http/httpClient';

/** Shape of the backend's UserResponse. Kept private so API details don't leak into the domain. */
interface UserDto {
  id: string;
  email: string;
  displayName: string;
  createdAt: string;
}

const toDomain = (dto: UserDto): User => ({ ...dto, createdAt: new Date(dto.createdAt) });

export function createHttpUserRepository(http: HttpClient): UserRepository {
  return {
    async findAll() {
      return (await http.get<UserDto[]>('/users')).map(toDomain);
    },
    async findById(id) {
      return toDomain(await http.get<UserDto>(`/users/${encodeURIComponent(id)}`));
    },
    async create(user: NewUser) {
      return toDomain(await http.post<UserDto>('/users', user));
    },
  };
}
