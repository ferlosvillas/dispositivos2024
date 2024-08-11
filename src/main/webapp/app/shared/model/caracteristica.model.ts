import { IDispositivo } from 'app/shared/model/dispositivo.model';

export interface ICaracteristica {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  dispositivo?: IDispositivo | null;
}

export const defaultValue: Readonly<ICaracteristica> = {};
