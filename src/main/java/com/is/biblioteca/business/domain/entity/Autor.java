package com.is.biblioteca.business.domain.entity;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Autor {

    @Id
    private String id;
    private String nombre;
    private boolean eliminado;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

}
