import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/dispositivo">
        <Translate contentKey="global.menu.entities.dispositivo" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/caracteristica">
        <Translate contentKey="global.menu.entities.caracteristica" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/personalizacion">
        <Translate contentKey="global.menu.entities.personalizacion" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/opcion">
        <Translate contentKey="global.menu.entities.opcion" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/adicional">
        <Translate contentKey="global.menu.entities.adicional" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/grupo">
        <Translate contentKey="global.menu.entities.grupo" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/venta">
        <Translate contentKey="global.menu.entities.venta" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
