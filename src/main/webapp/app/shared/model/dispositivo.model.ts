import { IPersonalizacion } from 'app/shared/model/personalizacion.model';
import { IAdicional } from 'app/shared/model/adicional.model';

export interface IDispositivo {
  id?: number;
  codigo?: string | null;
  nombre?: string | null;
  descripcion?: string | null;
  precioBase?: number | null;
  moneda?: string | null;
  personalizaciones?: IPersonalizacion[] | null;
  adicionales?: IAdicional[] | null;
}

export const defaultValue: Readonly<IDispositivo> = {};
