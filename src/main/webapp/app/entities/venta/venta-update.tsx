import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGrupo } from 'app/shared/model/grupo.model';
import { getEntities as getGrupos } from 'app/entities/grupo/grupo.reducer';
import { IVenta } from 'app/shared/model/venta.model';
import { getEntity, updateEntity, createEntity, reset } from './venta.reducer';

export const VentaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const grupos = useAppSelector(state => state.grupo.entities);
  const ventaEntity = useAppSelector(state => state.venta.entity);
  const loading = useAppSelector(state => state.venta.loading);
  const updating = useAppSelector(state => state.venta.updating);
  const updateSuccess = useAppSelector(state => state.venta.updateSuccess);

  const handleClose = () => {
    navigate('/venta');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getGrupos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.idVenta !== undefined && typeof values.idVenta !== 'number') {
      values.idVenta = Number(values.idVenta);
    }
    if (values.precio !== undefined && typeof values.precio !== 'number') {
      values.precio = Number(values.precio);
    }

    const entity = {
      ...ventaEntity,
      ...values,
      grupo: grupos.find(it => it.id.toString() === values.grupo?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...ventaEntity,
          grupo: ventaEntity?.grupo?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dispositivosApp.venta.home.createOrEditLabel" data-cy="VentaCreateUpdateHeading">
            <Translate contentKey="dispositivosApp.venta.home.createOrEditLabel">Create or edit a Venta</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="venta-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dispositivosApp.venta.idVenta')}
                id="venta-idVenta"
                name="idVenta"
                data-cy="idVenta"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.venta.codigo')}
                id="venta-codigo"
                name="codigo"
                data-cy="codigo"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.venta.nombre')}
                id="venta-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.venta.descripcion')}
                id="venta-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.venta.precio')}
                id="venta-precio"
                name="precio"
                data-cy="precio"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.venta.ventaPedidoJson')}
                id="venta-ventaPedidoJson"
                name="ventaPedidoJson"
                data-cy="ventaPedidoJson"
                type="textarea"
              />
              <ValidatedField
                label={translate('dispositivosApp.venta.ventaResultadoJson')}
                id="venta-ventaResultadoJson"
                name="ventaResultadoJson"
                data-cy="ventaResultadoJson"
                type="textarea"
              />
              <ValidatedField id="venta-grupo" name="grupo" data-cy="grupo" label={translate('dispositivosApp.venta.grupo')} type="select">
                <option value="" key="0" />
                {grupos
                  ? grupos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombres}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/venta" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default VentaUpdate;
