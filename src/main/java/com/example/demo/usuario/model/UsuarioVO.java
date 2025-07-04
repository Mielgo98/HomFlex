package com.example.demo.usuario.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.example.demo.propiedad.model.PropiedadVO;
import com.example.demo.rol.model.RolVO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioVO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(nullable = false, length = 255)
    private String password;
    
    @Column(length = 50, nullable = false)
    private String nombre;
    
    @Column(length = 100, nullable = false)
    private String apellidos;
    
    @Column(length = 20, unique = true, nullable = false)
    private String telefono;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(
        name = "usuario_roles",  // o "usuarios_roles" según tu BD
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<RolVO> roles = new HashSet<>();

    
    @Column(name = "foto_perfil", length = 255)
    private String fotoPerfil;
    
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
    
    @Column(name="verificado", nullable = false)
    private Boolean verificado;
    
    @Column(name="recordatorio")
    private Boolean recordatorio;
    
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;
    
    @Column(name = "account_non_expired", nullable = false)
    private Boolean accountNonExpired;
    
    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked;
    
    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean credentialsNonExpired;
    
    @Column(name = "token_verificacion", length = 100)
    private String tokenVerificacion;
    
    @Column(name = "token_expiration")
    private LocalDateTime tokenExpiration;
    
    // Método helper para añadir un rol
    public void addRol(RolVO rol) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(rol);
    }
    
    public boolean tieneRol(String nombreRol) {
        return roles.stream()
                    .anyMatch(r -> r.getNombre()
                                    .equalsIgnoreCase(nombreRol));
    }

    public boolean anadirRol(RolVO rol) {
        return this.roles.add(rol);         
    }
    
    public boolean anadirRol(String nombreRol) {
        Optional<RolVO> existente = roles.stream()
                                         .filter(r -> r.getNombre()
                                                       .equalsIgnoreCase(nombreRol))
                                         .findFirst();
        if (existente.isPresent()) {
            return false;                    
        }
        RolVO nuevo = RolVO.builder()
                           .nombre(nombreRol.toUpperCase())
                           .build();
        return roles.add(nuevo);
    }

    /**
     * Override del método equals para usar solo el ID de la entidad
     * Esto evita problemas con relaciones bidireccionales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioVO)) return false;
        UsuarioVO that = (UsuarioVO) o;
        return id != null && id.equals(that.getId());
    }

    /**
     * Override del método hashCode para usar solo el ID de la entidad
     * Esto evita problemas con relaciones bidireccionales
     */
    @Override
    public int hashCode() {
        // Se usa un valor constante si el ID es nulo
        return id != null ? id.hashCode() : 31;
    }
    
 // En UsuarioVO.java añade esta relación
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "usuario_propiedades_favoritas",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "propiedad_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PropiedadVO> propiedadesFavoritas = new HashSet<>();
}