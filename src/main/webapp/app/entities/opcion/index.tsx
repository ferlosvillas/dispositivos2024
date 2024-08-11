import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Opcion from './opcion';
import OpcionDetail from './opcion-detail';
import OpcionUpdate from './opcion-update';
import OpcionDeleteDialog from './opcion-delete-dialog';

const OpcionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Opcion />} />
    <Route path="new" element={<OpcionUpdate />} />
    <Route path=":id">
      <Route index element={<OpcionDetail />} />
      <Route path="edit" element={<OpcionUpdate />} />
      <Route path="delete" element={<OpcionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OpcionRoutes;
