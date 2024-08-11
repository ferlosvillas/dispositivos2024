import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Caracteristica from './caracteristica';
import CaracteristicaDetail from './caracteristica-detail';
import CaracteristicaUpdate from './caracteristica-update';
import CaracteristicaDeleteDialog from './caracteristica-delete-dialog';

const CaracteristicaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Caracteristica />} />
    <Route path="new" element={<CaracteristicaUpdate />} />
    <Route path=":id">
      <Route index element={<CaracteristicaDetail />} />
      <Route path="edit" element={<CaracteristicaUpdate />} />
      <Route path="delete" element={<CaracteristicaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CaracteristicaRoutes;
