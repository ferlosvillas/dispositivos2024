package ar.edu.um.programacion2.trabajo_final.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Venta.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_venta")
    private Long idVenta;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio", precision = 21, scale = 2)
    private BigDecimal precio;

    @Lob
    @Column(name = "venta_pedido_json")
    private String ventaPedidoJson;

    @Lob
    @Column(name = "venta_resultado_json")
    private String ventaResultadoJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Grupo grupo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdVenta() {
        return this.idVenta;
    }

    public Venta idVenta(Long idVenta) {
        this.setIdVenta(idVenta);
        return this;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Venta codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Venta nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Venta descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return this.precio;
    }

    public Venta precio(BigDecimal precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getVentaPedidoJson() {
        return this.ventaPedidoJson;
    }

    public Venta ventaPedidoJson(String ventaPedidoJson) {
        this.setVentaPedidoJson(ventaPedidoJson);
        return this;
    }

    public void setVentaPedidoJson(String ventaPedidoJson) {
        this.ventaPedidoJson = ventaPedidoJson;
    }

    public String getVentaResultadoJson() {
        return this.ventaResultadoJson;
    }

    public Venta ventaResultadoJson(String ventaResultadoJson) {
        this.setVentaResultadoJson(ventaResultadoJson);
        return this;
    }

    public void setVentaResultadoJson(String ventaResultadoJson) {
        this.ventaResultadoJson = ventaResultadoJson;
    }

    public Grupo getGrupo() {
        return this.grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Venta grupo(Grupo grupo) {
        this.setGrupo(grupo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return getId() != null && getId().equals(((Venta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", idVenta=" + getIdVenta() +
            ", codigo='" + getCodigo() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precio=" + getPrecio() +
            ", ventaPedidoJson='" + getVentaPedidoJson() + "'" +
            ", ventaResultadoJson='" + getVentaResultadoJson() + "'" +
            "}";
    }
}
