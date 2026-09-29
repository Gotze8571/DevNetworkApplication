/** How another member appears in lists. */
export interface MemberSummary {
  id: string;
  displayName: string;
  headline: string | null;
}

/** Another member's public profile. Never includes their email. */
export interface MemberProfile extends MemberSummary {
  joinedAt: Date;
  bio: string | null;
  location: string | null;
  githubUrl: string | null;
  linkedinUrl: string | null;
  websiteUrl: string | null;
}
