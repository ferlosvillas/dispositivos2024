import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './grupo.reducer';

export const GrupoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const grupoEntity = useAppSelector(state => state.grupo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="grupoDetailsHeading">
          <Translate contentKey="dispositivosApp.grupo.detail.title">Grupo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{grupoEntity.id}</dd>
          <dt>
            <span id="idGrupo">
              <Translate contentKey="dispositivosApp.grupo.idGrupo">Id Grupo</Translate>
            </span>
          </dt>
          <dd>{grupoEntity.idGrupo}</dd>
          <dt>
            <span id="nombres">
              <Translate contentKey="dispositivosApp.grupo.nombres">Nombres</Translate>
            </span>
          </dt>
          <dd>{grupoEntity.nombres}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="dispositivosApp.grupo.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{grupoEntity.descripcion}</dd>
          <dt>
            <Translate contentKey="dispositivosApp.grupo.user">User</Translate>
          </dt>
          <dd>{grupoEntity.user ? grupoEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/grupo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/grupo/${grupoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GrupoDetail;
