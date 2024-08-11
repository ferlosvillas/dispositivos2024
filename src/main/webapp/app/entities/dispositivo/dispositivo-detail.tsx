import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dispositivo.reducer';

export const DispositivoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dispositivoEntity = useAppSelector(state => state.dispositivo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dispositivoDetailsHeading">
          <Translate contentKey="dispositivosApp.dispositivo.detail.title">Dispositivo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dispositivoEntity.id}</dd>
          <dt>
            <span id="codigo">
              <Translate contentKey="dispositivosApp.dispositivo.codigo">Codigo</Translate>
            </span>
          </dt>
          <dd>{dispositivoEntity.codigo}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="dispositivosApp.dispositivo.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{dispositivoEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="dispositivosApp.dispositivo.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{dispositivoEntity.descripcion}</dd>
          <dt>
            <span id="precioBase">
              <Translate contentKey="dispositivosApp.dispositivo.precioBase">Precio Base</Translate>
            </span>
          </dt>
          <dd>{dispositivoEntity.precioBase}</dd>
          <dt>
            <span id="moneda">
              <Translate contentKey="dispositivosApp.dispositivo.moneda">Moneda</Translate>
            </span>
          </dt>
          <dd>{dispositivoEntity.moneda}</dd>
          <dt>
            <Translate contentKey="dispositivosApp.dispositivo.personalizaciones">Personalizaciones</Translate>
          </dt>
          <dd>
            {dispositivoEntity.personalizaciones
              ? dispositivoEntity.personalizaciones.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {dispositivoEntity.personalizaciones && i === dispositivoEntity.personalizaciones.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="dispositivosApp.dispositivo.adicionales">Adicionales</Translate>
          </dt>
          <dd>
            {dispositivoEntity.adicionales
              ? dispositivoEntity.adicionales.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {dispositivoEntity.adicionales && i === dispositivoEntity.adicionales.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/dispositivo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dispositivo/${dispositivoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DispositivoDetail;
