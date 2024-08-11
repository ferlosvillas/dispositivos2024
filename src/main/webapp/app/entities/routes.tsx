import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Dispositivo from './dispositivo';
import Caracteristica from './caracteristica';
import Personalizacion from './personalizacion';
import Opcion from './opcion';
import Adicional from './adicional';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="dispositivo/*" element={<Dispositivo />} />
        <Route path="caracteristica/*" element={<Caracteristica />} />
        <Route path="personalizacion/*" element={<Personalizacion />} />
        <Route path="opcion/*" element={<Opcion />} />
        <Route path="adicional/*" element={<Adicional />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
