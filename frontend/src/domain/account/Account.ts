export interface Account {
  id: string;
  email: string;
  displayName: string;
  createdAt: Date;
  headline: string | null;
  bio: string | null;
  location: string | null;
  githubUrl: string | null;
  linkedinUrl: string | null;
  websiteUrl: string | null;
  updatedAt: Date | null;
}

/** The editable part of an account. Empty strings clear a field. */
export interface AccountUpdate {
  displayName: string;
  headline: string;
  bio: string;
  location: string;
  githubUrl: string;
  linkedinUrl: string;
  websiteUrl: string;
}

export const ACCOUNT_LIMITS = {
  displayName: 100,
  headline: 150,
  bio: 2000,
  location: 100,
  url: 500,
} as const;

const URL_FIELDS = [
  ['githubUrl', 'GitHub URL'],
  ['linkedinUrl', 'LinkedIn URL'],
  ['websiteUrl', 'Website URL'],
] as const;

function isHttpUrl(value: string): boolean {
  try {
    const url = new URL(value);
    return url.protocol === 'http:' || url.protocol === 'https:';
  } catch {
    return false;
  }
}

export function validateAccountUpdate(input: AccountUpdate): string[] {
  const errors: string[] = [];
  const name = input.displayName.trim();
  if (!name) errors.push('Display name is required');
  if (name.length > ACCOUNT_LIMITS.displayName) {
    errors.push(`Display name must be at most ${ACCOUNT_LIMITS.displayName} characters`);
  }
  if (input.headline.trim().length > ACCOUNT_LIMITS.headline) {
    errors.push(`Headline must be at most ${ACCOUNT_LIMITS.headline} characters`);
  }
  if (input.bio.trim().length > ACCOUNT_LIMITS.bio) {
    errors.push(`Bio must be at most ${ACCOUNT_LIMITS.bio} characters`);
  }
  if (input.location.trim().length > ACCOUNT_LIMITS.location) {
    errors.push(`Location must be at most ${ACCOUNT_LIMITS.location} characters`);
  }
  for (const [field, label] of URL_FIELDS) {
    const value = input[field].trim();
    if (value && !isHttpUrl(value)) errors.push(`${label} must be a valid http(s) link`);
  }
  return errors;
}
