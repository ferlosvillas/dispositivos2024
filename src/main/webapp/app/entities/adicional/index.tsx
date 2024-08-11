import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Adicional from './adicional';
import AdicionalDetail from './adicional-detail';
import AdicionalUpdate from './adicional-update';
import AdicionalDeleteDialog from './adicional-delete-dialog';

const AdicionalRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Adicional />} />
    <Route path="new" element={<AdicionalUpdate />} />
    <Route path=":id">
      <Route index element={<AdicionalDetail />} />
      <Route path="edit" element={<AdicionalUpdate />} />
      <Route path="delete" element={<AdicionalDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AdicionalRoutes;
