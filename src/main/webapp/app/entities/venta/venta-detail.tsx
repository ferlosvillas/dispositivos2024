import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './venta.reducer';

export const VentaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ventaEntity = useAppSelector(state => state.venta.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ventaDetailsHeading">
          <Translate contentKey="dispositivosApp.venta.detail.title">Venta</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.id}</dd>
          <dt>
            <span id="idVenta">
              <Translate contentKey="dispositivosApp.venta.idVenta">Id Venta</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.idVenta}</dd>
          <dt>
            <span id="codigo">
              <Translate contentKey="dispositivosApp.venta.codigo">Codigo</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.codigo}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="dispositivosApp.venta.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="dispositivosApp.venta.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.descripcion}</dd>
          <dt>
            <span id="precio">
              <Translate contentKey="dispositivosApp.venta.precio">Precio</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.precio}</dd>
          <dt>
            <span id="ventaPedidoJson">
              <Translate contentKey="dispositivosApp.venta.ventaPedidoJson">Venta Pedido Json</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.ventaPedidoJson}</dd>
          <dt>
            <span id="ventaResultadoJson">
              <Translate contentKey="dispositivosApp.venta.ventaResultadoJson">Venta Resultado Json</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.ventaResultadoJson}</dd>
          <dt>
            <Translate contentKey="dispositivosApp.venta.grupo">Grupo</Translate>
          </dt>
          <dd>{ventaEntity.grupo ? ventaEntity.grupo.nombres : ''}</dd>
        </dl>
        <Button tag={Link} to="/venta" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/venta/${ventaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VentaDetail;
