import { IDispositivo } from 'app/shared/model/dispositivo.model';

export interface IPersonalizacion {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  dispositivos?: IDispositivo[] | null;
}

export const defaultValue: Readonly<IPersonalizacion> = {};
