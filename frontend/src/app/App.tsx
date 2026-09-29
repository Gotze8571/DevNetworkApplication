import { UsersPage } from '@/presentation/pages/UsersPage';
import { createContainer } from './container';
import { ContainerProvider } from './ContainerContext';

const container = createContainer();

export function App() {
  return (
    <ContainerProvider container={container}>
      <UsersPage />
    </ContainerProvider>
  );
}
