package com.example.demo.reserva.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.propiedad.model.PropiedadVO;
import com.example.demo.usuario.model.UsuarioVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaVO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "propiedad_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PropiedadVO propiedad;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UsuarioVO usuario; // El inquilino que hace la reserva
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;
    
    @Column(name = "num_huespedes", nullable = false)
    private Integer numHuespedes;
    
    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;
    
    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;
    
    @Column(name = "fecha_confirmacion")
    private LocalDateTime fechaConfirmacion;
    
    @Column(name = "codigo_reserva", unique = true, length = 20)
    private String codigoReserva;
    
    @Column(columnDefinition = "TEXT")
    private String comentarios;
    
    /**
     * Override del método equals para usar solo el ID de la entidad
     * Esto evita problemas con relaciones bidireccionales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservaVO)) return false;
        ReservaVO that = (ReservaVO) o;
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
}