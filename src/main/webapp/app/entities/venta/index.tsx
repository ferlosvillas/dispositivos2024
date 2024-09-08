import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Venta from './venta';
import VentaDetail from './venta-detail';
import VentaUpdate from './venta-update';
import VentaDeleteDialog from './venta-delete-dialog';

const VentaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Venta />} />
    <Route path="new" element={<VentaUpdate />} />
    <Route path=":id">
      <Route index element={<VentaDetail />} />
      <Route path="edit" element={<VentaUpdate />} />
      <Route path="delete" element={<VentaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VentaRoutes;
