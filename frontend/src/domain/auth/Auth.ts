/** Domain types for authentication. Plain TypeScript: no React, fetch or framework imports. */
export interface CurrentUser {
  id: string;
  email: string;
  displayName: string;
  createdAt: Date;
}

export interface Credentials {
  email: string;
  password: string;
}

export interface Registration extends Credentials {
  displayName: string;
}

export const MIN_PASSWORD_LENGTH = 8;
export const MAX_PASSWORD_LENGTH = 72;
export const MAX_DISPLAY_NAME_LENGTH = 100;

/** Returns a list of validation errors; empty means valid. Mirrors the backend rules. */
export function validateRegistration(input: Registration): string[] {
  const errors: string[] = [];
  if (!input.email.includes('@')) errors.push('A valid email is required');
  const name = input.displayName.trim();
  if (!name) errors.push('Display name is required');
  if (name.length > MAX_DISPLAY_NAME_LENGTH) {
    errors.push(`Display name must be at most ${MAX_DISPLAY_NAME_LENGTH} characters`);
  }
  if (input.password.length < MIN_PASSWORD_LENGTH) {
    errors.push(`Password must be at least ${MIN_PASSWORD_LENGTH} characters`);
  }
  if (input.password.length > MAX_PASSWORD_LENGTH) {
    errors.push(`Password must be at most ${MAX_PASSWORD_LENGTH} characters`);
  }
  return errors;
}
