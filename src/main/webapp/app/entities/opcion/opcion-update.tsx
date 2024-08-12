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
import { IOpcion } from 'app/shared/model/opcion.model';
import { getEntity, updateEntity, createEntity, reset } from './opcion.reducer';

export const OpcionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const personalizacions = useAppSelector(state => state.personalizacion.entities);
  const opcionEntity = useAppSelector(state => state.opcion.entity);
  const loading = useAppSelector(state => state.opcion.loading);
  const updating = useAppSelector(state => state.opcion.updating);
  const updateSuccess = useAppSelector(state => state.opcion.updateSuccess);

  const handleClose = () => {
    navigate('/opcion');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPersonalizacions({}));
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
    if (values.precioAdicional !== undefined && typeof values.precioAdicional !== 'number') {
      values.precioAdicional = Number(values.precioAdicional);
    }

    const entity = {
      ...opcionEntity,
      ...values,
      personalizacion: personalizacions.find(it => it.id.toString() === values.personalizacion?.toString()),
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
          ...opcionEntity,
          personalizacion: opcionEntity?.personalizacion?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dispositivosApp.opcion.home.createOrEditLabel" data-cy="OpcionCreateUpdateHeading">
            <Translate contentKey="dispositivosApp.opcion.home.createOrEditLabel">Create or edit a Opcion</Translate>
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
                  id="opcion-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dispositivosApp.opcion.codigo')}
                id="opcion-codigo"
                name="codigo"
                data-cy="codigo"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.opcion.nombre')}
                id="opcion-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.opcion.descripcion')}
                id="opcion-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
              />
              <ValidatedField
                label={translate('dispositivosApp.opcion.precioAdicional')}
                id="opcion-precioAdicional"
                name="precioAdicional"
                data-cy="precioAdicional"
                type="text"
              />
              <ValidatedField
                id="opcion-personalizacion"
                name="personalizacion"
                data-cy="personalizacion"
                label={translate('dispositivosApp.opcion.personalizacion')}
                type="select"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/opcion" replace color="info">
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

export default OpcionUpdate;
