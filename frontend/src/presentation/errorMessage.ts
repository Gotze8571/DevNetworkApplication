/** A user-facing message for anything thrown by a use case. */
export function errorMessage(error: unknown, fallback = 'Something went wrong'): string {
  return error instanceof Error && error.message ? error.message : fallback;
}
