/** Domain entity. Plain TypeScript: no React, fetch or framework imports. */
export interface User {
  id: string;
  email: string;
  displayName: string;
  createdAt: Date;
}

export interface NewUser {
  email: string;
  displayName: string;
}

export const MAX_DISPLAY_NAME_LENGTH = 100;

/** Returns a list of validation errors; empty means valid. */
export function validateNewUser(input: NewUser): string[] {
  const errors: string[] = [];
  if (!input.email.includes('@')) errors.push('A valid email is required');
  const name = input.displayName.trim();
  if (!name) errors.push('Display name is required');
  if (name.length > MAX_DISPLAY_NAME_LENGTH) {
    errors.push(`Display name must be at most ${MAX_DISPLAY_NAME_LENGTH} characters`);
  }
  return errors;
}
