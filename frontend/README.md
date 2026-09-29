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
│   └── user/
├── application/             # Use cases. Depend only on domain.
│   └── user/
├── infrastructure/          # I/O: HTTP client, API-backed repositories, DTO mapping
│   ├── http/
│   └── user/
└── presentation/            # React: pages, components, hooks
    ├── pages/
    ├── components/
    └── hooks/
```

**Dependency rule:** `presentation` → `application` → `domain` ← `infrastructure`. Components never call `fetch` or a repository directly; they go through a hook, which calls a use case from the container.

Import with the `@/` alias, e.g. `import { User } from '@/domain/user/User'`.

### Adding a feature (e.g. `connection`)

1. `domain/connection/` — types, validation, `ConnectionRepository` interface.
2. `application/connection/` — `createConnectionUseCases(repository)`.
3. `infrastructure/connection/` — `createHttpConnectionRepository(http)` with DTO mapping.
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
