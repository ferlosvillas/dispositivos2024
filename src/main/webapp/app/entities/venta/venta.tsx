import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './venta.reducer';

export const Venta = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const ventaList = useAppSelector(state => state.venta.entities);
  const loading = useAppSelector(state => state.venta.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="venta-heading" data-cy="VentaHeading">
        <Translate contentKey="dispositivosApp.venta.home.title">Ventas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dispositivosApp.venta.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/venta/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dispositivosApp.venta.home.createLabel">Create new Venta</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ventaList && ventaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dispositivosApp.venta.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('idVenta')}>
                  <Translate contentKey="dispositivosApp.venta.idVenta">Id Venta</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('idVenta')} />
                </th>
                <th className="hand" onClick={sort('codigo')}>
                  <Translate contentKey="dispositivosApp.venta.codigo">Codigo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('codigo')} />
                </th>
                <th className="hand" onClick={sort('nombre')}>
                  <Translate contentKey="dispositivosApp.venta.nombre">Nombre</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nombre')} />
                </th>
                <th className="hand" onClick={sort('descripcion')}>
                  <Translate contentKey="dispositivosApp.venta.descripcion">Descripcion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('descripcion')} />
                </th>
                <th className="hand" onClick={sort('precio')}>
                  <Translate contentKey="dispositivosApp.venta.precio">Precio</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('precio')} />
                </th>
                <th className="hand" onClick={sort('ventaPedidoJson')}>
                  <Translate contentKey="dispositivosApp.venta.ventaPedidoJson">Venta Pedido Json</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ventaPedidoJson')} />
                </th>
                <th className="hand" onClick={sort('ventaResultadoJson')}>
                  <Translate contentKey="dispositivosApp.venta.ventaResultadoJson">Venta Resultado Json</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ventaResultadoJson')} />
                </th>
                <th>
                  <Translate contentKey="dispositivosApp.venta.grupo">Grupo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ventaList.map((venta, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/venta/${venta.id}`} color="link" size="sm">
                      {venta.id}
                    </Button>
                  </td>
                  <td>{venta.idVenta}</td>
                  <td>{venta.codigo}</td>
                  <td>{venta.nombre}</td>
                  <td>{venta.descripcion}</td>
                  <td>{venta.precio}</td>
                  <td>{venta.ventaPedidoJson}</td>
                  <td>{venta.ventaResultadoJson}</td>
                  <td>{venta.grupo ? <Link to={`/grupo/${venta.grupo.id}`}>{venta.grupo.nombres}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/venta/${venta.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/venta/${venta.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/venta/${venta.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="dispositivosApp.venta.home.notFound">No Ventas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Venta;
