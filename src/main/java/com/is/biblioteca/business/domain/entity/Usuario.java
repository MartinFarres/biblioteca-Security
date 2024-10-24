
package com.is.biblioteca.business.domain.entity;


import java.util.Arrays;
import java.util.Collection;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.mapping.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.is.biblioteca.business.domain.enumeration.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Usuario", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class Usuario implements UserDetails{
    @SuppressWarnings("deprecation")
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    @Column(nullable = false)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Rol rol;
    @OneToOne
    private Imagen imagen;
    private boolean eliminado;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(rol.name()));

    }

    @Override
    public String getUsername() {
       return this.email;
    }

}
