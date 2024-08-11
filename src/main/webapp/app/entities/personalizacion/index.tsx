import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Personalizacion from './personalizacion';
import PersonalizacionDetail from './personalizacion-detail';
import PersonalizacionUpdate from './personalizacion-update';
import PersonalizacionDeleteDialog from './personalizacion-delete-dialog';

const PersonalizacionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Personalizacion />} />
    <Route path="new" element={<PersonalizacionUpdate />} />
    <Route path=":id">
      <Route index element={<PersonalizacionDetail />} />
      <Route path="edit" element={<PersonalizacionUpdate />} />
      <Route path="delete" element={<PersonalizacionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PersonalizacionRoutes;
