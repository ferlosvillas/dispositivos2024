import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPersonalizacion } from 'app/shared/model/personalizacion.model';
import { getEntities as getPersonalizacions } from 'app/entities/personalizacion/personalizacion.reducer';
import { IAdicional } from 'app/shared/model/adicional.model';
import { getEntities as getAdicionals } from 'app/entities/adicional/adicional.reducer';
import { IDispositivo } from 'app/shared/model/dispositivo.model';
import { getEntity, updateEntity, createEntity, reset } from './dispositivo.reducer';

export const DispositivoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const personalizacions = useAppSelector(state => state.personalizacion.entities);
  const adicionals = useAppSelector(state => state.adicional.entities);
  const dispositivoEntity = useAppSelector(state => state.dispositivo.entity);
  const loading = useAppSelector(state => state.dispositivo.loading);
  const updating = useAppSelector(state => state.dispositivo.updating);
  const updateSuccess = useAppSelector(state => state.dispositivo.updateSuccess);

  const handleClose = () => {
    navigate('/dispositivo');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPersonalizacions({}));
    dispatch(getAdicionals({}));
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
    if (values.precioBase !== undefined && typeof values.precioBase !== 'number') {
      values.precioBase = Number(values.precioBase);
    }

    const entity = {
      ...dispositivoEntity,
      ...values,
      personalizaciones: mapIdList(values.personalizaciones),
      adicionales: mapIdList(values.adicionales),
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
          ...dispositivoEntity,
          personalizaciones: dispositivoEntity?.personalizaciones?.map(e => e.id.toString()),
          adicionales: dispositivoEntity?.adicionales?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dispositivosApp.dispositivo.home.createOrEditLabel" data-cy="DispositivoCreateUpdateHeading">
            <Translate contentKey="dispositivosApp.dispositivo.home.createOrEditLabel">Create or edit a Dispositivo</Translate>
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
                  id="dispositivo-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dispositivosApp.dispositivo.codigo')}
                id="dispositivo-codigo"
                name="codigo"
                data-cy="codigo"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.dispositivo.nombre')}
                id="dispositivo-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.dispositivo.descripcion')}
                id="dispositivo-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.dispositivo.precioBase')}
                id="dispositivo-precioBase"
                name="precioBase"
                data-cy="precioBase"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.dispositivo.moneda')}
                id="dispositivo-moneda"
                name="moneda"
                data-cy="moneda"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.dispositivo.personalizaciones')}
                id="dispositivo-personalizaciones"
                data-cy="personalizaciones"
                type="select"
                multiple
                name="personalizaciones"
              >
                <option value="" key="0" />
                {personalizacions
                  ? personalizacions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('dispositivosApp.dispositivo.adicionales')}
                id="dispositivo-adicionales"
                data-cy="adicionales"
                type="select"
                multiple
                name="adicionales"
              >
                <option value="" key="0" />
                {adicionals
                  ? adicionals.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dispositivo" replace color="info">
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

export default DispositivoUpdate;
