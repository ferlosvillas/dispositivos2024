import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './dispositivo.reducer';

export const Dispositivo = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const dispositivoList = useAppSelector(state => state.dispositivo.entities);
  const loading = useAppSelector(state => state.dispositivo.loading);

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
      <h2 id="dispositivo-heading" data-cy="DispositivoHeading">
        <Translate contentKey="dispositivosApp.dispositivo.home.title">Dispositivos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dispositivosApp.dispositivo.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/dispositivo/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dispositivosApp.dispositivo.home.createLabel">Create new Dispositivo</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {dispositivoList && dispositivoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dispositivosApp.dispositivo.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('codigo')}>
                  <Translate contentKey="dispositivosApp.dispositivo.codigo">Codigo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('codigo')} />
                </th>
                <th className="hand" onClick={sort('nombre')}>
                  <Translate contentKey="dispositivosApp.dispositivo.nombre">Nombre</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nombre')} />
                </th>
                <th className="hand" onClick={sort('descripcion')}>
                  <Translate contentKey="dispositivosApp.dispositivo.descripcion">Descripcion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('descripcion')} />
                </th>
                <th className="hand" onClick={sort('precioBase')}>
                  <Translate contentKey="dispositivosApp.dispositivo.precioBase">Precio Base</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('precioBase')} />
                </th>
                <th className="hand" onClick={sort('moneda')}>
                  <Translate contentKey="dispositivosApp.dispositivo.moneda">Moneda</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('moneda')} />
                </th>
                <th>
                  <Translate contentKey="dispositivosApp.dispositivo.personalizaciones">Personalizaciones</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="dispositivosApp.dispositivo.adicionales">Adicionales</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {dispositivoList.map((dispositivo, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/dispositivo/${dispositivo.id}`} color="link" size="sm">
                      {dispositivo.id}
                    </Button>
                  </td>
                  <td>{dispositivo.codigo}</td>
                  <td>{dispositivo.nombre}</td>
                  <td>{dispositivo.descripcion}</td>
                  <td>{dispositivo.precioBase}</td>
                  <td>{dispositivo.moneda}</td>
                  <td>
                    {dispositivo.personalizaciones
                      ? dispositivo.personalizaciones.map((val, j) => (
                          <span key={j}>
                            <Link to={`/personalizacion/${val.id}`}>{val.id}</Link>
                            {j === dispositivo.personalizaciones.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {dispositivo.adicionales
                      ? dispositivo.adicionales.map((val, j) => (
                          <span key={j}>
                            <Link to={`/adicional/${val.id}`}>{val.id}</Link>
                            {j === dispositivo.adicionales.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/dispositivo/${dispositivo.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/dispositivo/${dispositivo.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/dispositivo/${dispositivo.id}/delete`)}
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
              <Translate contentKey="dispositivosApp.dispositivo.home.notFound">No Dispositivos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Dispositivo;
