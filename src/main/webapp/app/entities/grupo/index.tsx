import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Grupo from './grupo';
import GrupoDetail from './grupo-detail';
import GrupoUpdate from './grupo-update';
import GrupoDeleteDialog from './grupo-delete-dialog';

const GrupoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Grupo />} />
    <Route path="new" element={<GrupoUpdate />} />
    <Route path=":id">
      <Route index element={<GrupoDetail />} />
      <Route path="edit" element={<GrupoUpdate />} />
      <Route path="delete" element={<GrupoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GrupoRoutes;
