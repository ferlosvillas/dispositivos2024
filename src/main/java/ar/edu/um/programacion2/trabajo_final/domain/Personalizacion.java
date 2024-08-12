package ar.edu.um.programacion2.trabajo_final.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Personalizacion.
 */
@Entity
@Table(name = "personalizacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Personalizacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "personlaizacion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personlaizacion" }, allowSetters = true)
    private Set<Opcion> opciones = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "personalizaciones")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "caracteristicas", "personalizaciones", "adicionales" }, allowSetters = true)
    private Set<Dispositivo> dispositivos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Personalizacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Personalizacion nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Personalizacion descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Opcion> getOpciones() {
        return this.opciones;
    }

    public void setOpciones(Set<Opcion> opcions) {
        if (this.opciones != null) {
            this.opciones.forEach(i -> i.setPersonlaizacion(null));
        }
        if (opcions != null) {
            opcions.forEach(i -> i.setPersonlaizacion(this));
        }
        this.opciones = opcions;
    }

    public Personalizacion opciones(Set<Opcion> opcions) {
        this.setOpciones(opcions);
        return this;
    }

    public Personalizacion addOpciones(Opcion opcion) {
        this.opciones.add(opcion);
        opcion.setPersonlaizacion(this);
        return this;
    }

    public Personalizacion removeOpciones(Opcion opcion) {
        this.opciones.remove(opcion);
        opcion.setPersonlaizacion(null);
        return this;
    }

    public Set<Dispositivo> getDispositivos() {
        return this.dispositivos;
    }

    public void setDispositivos(Set<Dispositivo> dispositivos) {
        if (this.dispositivos != null) {
            this.dispositivos.forEach(i -> i.removePersonalizaciones(this));
        }
        if (dispositivos != null) {
            dispositivos.forEach(i -> i.addPersonalizaciones(this));
        }
        this.dispositivos = dispositivos;
    }

    public Personalizacion dispositivos(Set<Dispositivo> dispositivos) {
        this.setDispositivos(dispositivos);
        return this;
    }

    public Personalizacion addDispositivos(Dispositivo dispositivo) {
        this.dispositivos.add(dispositivo);
        dispositivo.getPersonalizaciones().add(this);
        return this;
    }

    public Personalizacion removeDispositivos(Dispositivo dispositivo) {
        this.dispositivos.remove(dispositivo);
        dispositivo.getPersonalizaciones().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personalizacion)) {
            return false;
        }
        return getId() != null && getId().equals(((Personalizacion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personalizacion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
