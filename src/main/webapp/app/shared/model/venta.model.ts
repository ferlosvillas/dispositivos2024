import { IGrupo } from 'app/shared/model/grupo.model';

export interface IVenta {
  id?: number;
  idVenta?: number | null;
  codigo?: string | null;
  nombre?: string | null;
  descripcion?: string | null;
  precio?: number | null;
  ventaPedidoJson?: string | null;
  ventaResultadoJson?: string | null;
  grupo?: IGrupo | null;
}

export const defaultValue: Readonly<IVenta> = {};
