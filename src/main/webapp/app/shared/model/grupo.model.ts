import { IUser } from 'app/shared/model/user.model';

export interface IGrupo {
  id?: number;
  idGrupo?: string | null;
  nombres?: string | null;
  descripcion?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IGrupo> = {};
