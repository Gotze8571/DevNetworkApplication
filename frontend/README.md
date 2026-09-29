# Frontend — React + TypeScript

Built with Vite, organised with Clean Architecture.

## Folder layout

```
src/
├── main.tsx                 # Entry point
├── app/                     # Composition root: wires infrastructure into use cases, app shell
│   ├── container.ts         #   The only file that picks concrete implementations
│   ├── ContainerContext.tsx #   Provides the container to React
│   └── App.tsx
├── config/                  # Environment variables
├── domain/                  # Entities, validation rules, repository interfaces. No React, no fetch.
│   └── auth/ account/ member/ connection/
├── application/             # Use cases. Depend only on domain.
├── infrastructure/          # I/O: HTTP client, API-backed repositories, DTO mapping
│   ├── http/                #   fetch wrapper: JSON, CSRF header, 401 notifications
│   └── auth/ account/ member/ connection/
└── presentation/            # React: pages, components, hooks
    ├── auth/                #   AuthProvider (session state) and route guards
    ├── pages/
    ├── components/
    └── hooks/
```

Routes (`app/App.tsx`): `/login` and `/register` are public. `/members`, `/members/:id`, `/connections` and `/account` require a session. `presentation/auth/AuthProvider` restores the session on load and signs the user out on any `401`.

**Dependency rule:** `presentation` → `application` → `domain` ← `infrastructure`. Components never call `fetch` or a repository directly; they go through a hook, which calls a use case from the container.

Import with the `@/` alias, e.g. `import type { Account } from '@/domain/account/Account'`.

### Adding a feature (e.g. `post`)

1. `domain/post/` — types, validation, `PostRepository` interface.
2. `application/postUseCases.ts` — `createPostUseCases(repository)`.
3. `infrastructure/post/` — `createHttpPostRepository(http)` with DTO mapping.
4. Register it in `app/container.ts`.
5. `presentation/` — a hook plus the pages/components that use it.

## Running locally

Requires Node 20+. Start the backend first (see `../backend/README.md`); the dev server proxies `/api` to `http://localhost:8080`.

```bash
cp .env.example .env
npm install
npm run dev        # http://localhost:5173
npm run build      # type-check + production build
```
