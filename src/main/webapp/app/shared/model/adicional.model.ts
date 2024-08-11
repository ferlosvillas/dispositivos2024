import { IDispositivo } from 'app/shared/model/dispositivo.model';

export interface IAdicional {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  precio?: number | null;
  precioGratis?: number | null;
  dispositivos?: IDispositivo[] | null;
}

export const defaultValue: Readonly<IAdicional> = {};
