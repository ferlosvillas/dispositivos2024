import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Dispositivo from './dispositivo';
import DispositivoDetail from './dispositivo-detail';
import DispositivoUpdate from './dispositivo-update';
import DispositivoDeleteDialog from './dispositivo-delete-dialog';

const DispositivoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Dispositivo />} />
    <Route path="new" element={<DispositivoUpdate />} />
    <Route path=":id">
      <Route index element={<DispositivoDetail />} />
      <Route path="edit" element={<DispositivoUpdate />} />
      <Route path="delete" element={<DispositivoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DispositivoRoutes;
