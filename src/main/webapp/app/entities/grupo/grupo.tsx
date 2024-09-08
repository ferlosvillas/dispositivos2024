import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './grupo.reducer';

export const Grupo = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const grupoList = useAppSelector(state => state.grupo.entities);
  const loading = useAppSelector(state => state.grupo.loading);

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
      <h2 id="grupo-heading" data-cy="GrupoHeading">
        <Translate contentKey="dispositivosApp.grupo.home.title">Grupos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dispositivosApp.grupo.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/grupo/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dispositivosApp.grupo.home.createLabel">Create new Grupo</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {grupoList && grupoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dispositivosApp.grupo.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('idGrupo')}>
                  <Translate contentKey="dispositivosApp.grupo.idGrupo">Id Grupo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('idGrupo')} />
                </th>
                <th className="hand" onClick={sort('nombres')}>
                  <Translate contentKey="dispositivosApp.grupo.nombres">Nombres</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nombres')} />
                </th>
                <th className="hand" onClick={sort('descripcion')}>
                  <Translate contentKey="dispositivosApp.grupo.descripcion">Descripcion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('descripcion')} />
                </th>
                <th>
                  <Translate contentKey="dispositivosApp.grupo.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {grupoList.map((grupo, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/grupo/${grupo.id}`} color="link" size="sm">
                      {grupo.id}
                    </Button>
                  </td>
                  <td>{grupo.idGrupo}</td>
                  <td>{grupo.nombres}</td>
                  <td>{grupo.descripcion}</td>
                  <td>{grupo.user ? grupo.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/grupo/${grupo.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/grupo/${grupo.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/grupo/${grupo.id}/delete`)}
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
              <Translate contentKey="dispositivosApp.grupo.home.notFound">No Grupos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Grupo;
