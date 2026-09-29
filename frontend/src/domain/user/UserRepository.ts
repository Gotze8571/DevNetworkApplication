import type { NewUser, User } from './User';

/** Port: implemented in infrastructure (HTTP today, could be a mock or cache). */
export interface UserRepository {
  findAll(): Promise<User[]>;
  findById(id: string): Promise<User>;
  create(user: NewUser): Promise<User>;
}
