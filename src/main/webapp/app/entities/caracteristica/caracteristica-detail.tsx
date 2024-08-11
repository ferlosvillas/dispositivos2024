import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './caracteristica.reducer';

export const CaracteristicaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const caracteristicaEntity = useAppSelector(state => state.caracteristica.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="caracteristicaDetailsHeading">
          <Translate contentKey="dispositivosApp.caracteristica.detail.title">Caracteristica</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{caracteristicaEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="dispositivosApp.caracteristica.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{caracteristicaEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="dispositivosApp.caracteristica.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{caracteristicaEntity.descripcion}</dd>
          <dt>
            <Translate contentKey="dispositivosApp.caracteristica.dispositivo">Dispositivo</Translate>
          </dt>
          <dd>{caracteristicaEntity.dispositivo ? caracteristicaEntity.dispositivo.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/caracteristica" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/caracteristica/${caracteristicaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CaracteristicaDetail;
