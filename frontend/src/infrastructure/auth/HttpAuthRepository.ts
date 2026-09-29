import type { CurrentUser } from '@/domain/auth/Auth';
import type { AuthRepository } from '@/domain/auth/AuthRepository';
import { HttpError, type HttpClient } from '@/infrastructure/http/httpClient';

interface CurrentUserDto {
  id: string;
  email: string;
  displayName: string;
  createdAt: string;
}

const toDomain = (dto: CurrentUserDto): CurrentUser => ({ ...dto, createdAt: new Date(dto.createdAt) });

export function createHttpAuthRepository(http: HttpClient): AuthRepository {
  return {
    async currentUser() {
      try {
        return toDomain(await http.get<CurrentUserDto>('/auth/me'));
      } catch (e) {
        if (e instanceof HttpError && e.status === 401) return null;
        throw e;
      }
    },
    async login(credentials) {
      return toDomain(await http.post<CurrentUserDto>('/auth/login', credentials));
    },
    async register(registration) {
      return toDomain(await http.post<CurrentUserDto>('/auth/register', registration));
    },
    logout() {
      return http.post<void>('/auth/logout');
    },
    onSessionExpired(listener) {
      return http.onUnauthorized(listener);
    },
  };
}
