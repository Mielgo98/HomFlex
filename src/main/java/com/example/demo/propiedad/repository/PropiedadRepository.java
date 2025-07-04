package com.example.demo.propiedad.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.propiedad.model.PropiedadVO;
import com.example.demo.usuario.model.UsuarioVO;

@Repository
public interface PropiedadRepository extends JpaRepository<PropiedadVO, Long> {
     
	/**
	 * Encuentra todas las propiedades de un propietario por su ID
	 * @param propietarioId ID del propietario
	 * @return Lista de propiedades del propietario
	 */
	List<PropiedadVO> findByPropietarioId(Long propietarioId);

	
    // Buscar propiedades de un propietario
    List<PropiedadVO> findByPropietario(UsuarioVO propietario);
    
    // Buscar propiedades de un propietario paginadas
    List<PropiedadVO> findByPropietarioUsername(String username, Pageable p);
    
    // Buscar propiedades por estado de activación
    List<PropiedadVO> findByPropietarioAndActivo(UsuarioVO propietario, Boolean activo);
    
    // Buscar propiedades de un propietario por estado de activación paginadas
    Page<PropiedadVO> findByPropietarioAndActivo(UsuarioVO propietario, Boolean activo, Pageable pageable);
    
    // Buscar propiedades por ciudad
    List<PropiedadVO> findByCiudadContainingIgnoreCaseAndActivoTrue(String ciudad);
    
    // Buscar propiedades por país
    List<PropiedadVO> findByPaisContainingIgnoreCaseAndActivoTrue(String pais);
    
    // Buscar propiedades con capacidad igual o superior
    List<PropiedadVO> findByCapacidadGreaterThanEqualAndActivoTrue(Integer capacidad);
    
    // Consulta optimizada para cargar fotos junto con propiedades (JOIN FETCH)
    @Query("SELECT DISTINCT p FROM PropiedadVO p LEFT JOIN FETCH p.fotos WHERE p.activo = true")
    List<PropiedadVO> findByActivoTrueWithFotos();
    
    // Consulta optimizada para cargar fotos junto con propiedades paginadas
    @Query(value = "SELECT DISTINCT p FROM PropiedadVO p LEFT JOIN FETCH p.fotos WHERE p.activo = true",
           countQuery = "SELECT COUNT(p) FROM PropiedadVO p WHERE p.activo = true")
    Page<PropiedadVO> findByActivoTrueWithFotos(Pageable pageable);
    
    // Mantener los métodos originales por compatibilidad
    List<PropiedadVO> findByActivoTrue();
    
    Page<PropiedadVO> findByActivoTrue(Pageable pageable);
    
    Page<PropiedadVO> findByPropietarioIdAndTituloContainingIgnoreCaseAndActivo(
            Long propietarioId,
            String tituloFiltro,
            boolean activo,
            Pageable pageable
        );
    
    // Búsqueda avanzada
    @Query("SELECT p FROM PropiedadVO p WHERE " +
           "(:ciudad IS NULL OR LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))) AND " +
           "(:pais IS NULL OR LOWER(p.pais) LIKE LOWER(CONCAT('%', :pais, '%'))) AND " +
           "(:capacidad IS NULL OR p.capacidad >= :capacidad) AND " +
           "(:dormitorios IS NULL OR p.dormitorios >= :dormitorios) AND " +
           "(:banos IS NULL OR p.banos >= :banos) AND " +
           "(p.activo = true)")
    Page<PropiedadVO> busquedaAvanzada(
            @Param("ciudad") String ciudad,
            @Param("pais") String pais,
            @Param("capacidad") Integer capacidad,
            @Param("dormitorios") Integer dormitorios,
            @Param("banos") Integer banos,
            Pageable pageable);
    
    // Búsqueda avanzada con rango de precios
    @Query("SELECT p FROM PropiedadVO p WHERE " +
           "(:ciudad IS NULL OR LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))) AND " +
           "(:pais IS NULL OR LOWER(p.pais) LIKE LOWER(CONCAT('%', :pais, '%'))) AND " +
           "(:capacidad IS NULL OR p.capacidad >= :capacidad) AND " +
           "(:dormitorios IS NULL OR p.dormitorios >= :dormitorios) AND " +
           "(:banos IS NULL OR p.banos >= :banos) AND " +
           "(:precioMin IS NULL OR p.precioDia >= :precioMin) AND " +
           "(:precioMax IS NULL OR p.precioDia <= :precioMax) AND " +
           "(p.activo = true)")
    Page<PropiedadVO> busquedaAvanzadaConPrecios(
            @Param("ciudad") String ciudad,
            @Param("pais") String pais,
            @Param("capacidad") Integer capacidad,
            @Param("dormitorios") Integer dormitorios,
            @Param("banos") Integer banos,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            Pageable pageable);
    
    // Buscar por texto en título o descripción
    @Query("SELECT p FROM PropiedadVO p WHERE " +
           "(LOWER(p.titulo) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.pais) LIKE LOWER(CONCAT('%', :texto, '%'))) AND " +
           "(p.activo = true)")
    Page<PropiedadVO> buscarPorTexto(@Param("texto") String texto, Pageable pageable);
    
    // Buscar propiedades cercanas a un punto (aproximación simple por rangos)
    @Query("SELECT p FROM PropiedadVO p WHERE " +
           "p.latitud BETWEEN :latMin AND :latMax AND " +
           "p.longitud BETWEEN :longMin AND :longMax AND " +
           "p.activo = true")
    List<PropiedadVO> buscarPropiedadesCercanas(
            @Param("latMin") BigDecimal latMin,
            @Param("latMax") BigDecimal latMax,
            @Param("longMin") BigDecimal longMin,
            @Param("longMax") BigDecimal longMax);
    
    // Estadísticas: contar propiedades por propietario
    @Query("SELECT COUNT(p) FROM PropiedadVO p WHERE p.propietario.id = :propietarioId")
    Long contarPropiedadesPorPropietario(@Param("propietarioId") Long propietarioId);
    
    // Estadísticas: contar propiedades activas por propietario
    @Query("SELECT COUNT(p) FROM PropiedadVO p WHERE p.propietario.id = :propietarioId AND p.activo = true")
    Long contarPropiedadesActivasPorPropietario(@Param("propietarioId") Long propietarioId);
    
    // Estadísticas: agrupar propiedades por ciudad para un propietario
    @Query("SELECT p.ciudad, COUNT(p) FROM PropiedadVO p WHERE p.propietario.id = :propietarioId GROUP BY p.ciudad")
    List<Object[]> agruparPropiedadesPorCiudad(@Param("propietarioId") Long propietarioId);
    
    long countByActivoTrue();
    
    
    /**
     * Busca propiedades de un propietario paginadas
     */
    Page<PropiedadVO> findByPropietario(UsuarioVO propietario, Pageable pageable);

    /**
     * Busca propiedades de un propietario por texto de búsqueda
     */
    @Query("SELECT p FROM PropiedadVO p WHERE p.propietario = :propietario AND " +
           "(LOWER(p.titulo) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.pais) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    Page<PropiedadVO> findByPropietarioAndBusqueda(
            @Param("propietario") UsuarioVO propietario,
            @Param("busqueda") String busqueda,
            Pageable pageable);

    /**
     * Busca propiedades de un propietario por texto de búsqueda y estado de activación
     */
    @Query("SELECT p FROM PropiedadVO p WHERE p.propietario = :propietario AND " +
           "(LOWER(p.titulo) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.pais) LIKE LOWER(CONCAT('%', :busqueda, '%'))) AND " +
           "p.activo = :activo")
    Page<PropiedadVO> findByPropietarioAndBusquedaAndActivo(
            @Param("propietario") UsuarioVO propietario,
            @Param("busqueda") String busqueda,
            @Param("activo") Boolean activo,
            Pageable pageable);

    /**
     * Busca propiedades cercanas a un punto específico
     */
    @Query("SELECT p FROM PropiedadVO p WHERE " +
           "p.latitud BETWEEN (:latitud - :distanciaLatitud) AND (:latitud + :distanciaLatitud) AND " +
           "p.longitud BETWEEN (:longitud - :distanciaLongitud) AND (:longitud + :distanciaLongitud) AND " +
           "p.activo = true")
    List<PropiedadVO> findPropiedadesCercanas(
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("distanciaLatitud") Double distanciaLatitud,
            @Param("distanciaLongitud") Double distanciaLongitud);

    /**
     * Obtiene las ciudades más populares (con más propiedades)
     */
    @Query("SELECT p.ciudad, COUNT(p) FROM PropiedadVO p WHERE p.activo = true " +
           "GROUP BY p.ciudad ORDER BY COUNT(p) DESC")
    List<Object[]> findCiudadesPopulares(
            Pageable pageable);

    /**
     * Versión simplificada para obtener solo las top N ciudades
     */
    @Query(value = "SELECT p.ciudad FROM PropiedadVO p WHERE p.activo = true " +
           "GROUP BY p.ciudad ORDER BY COUNT(p) DESC",
           nativeQuery = false)
    List<String> findCiudadesPopulares(int limit);

    /**
     * Búsqueda avanzada incluyendo precios con parámetros Double
     */
    @Query("SELECT p FROM PropiedadVO p WHERE " +
           "(:ciudad IS NULL OR LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))) AND " +
           "(:pais IS NULL OR LOWER(p.pais) LIKE LOWER(CONCAT('%', :pais, '%'))) AND " +
           "(:capacidad IS NULL OR p.capacidad >= :capacidad) AND " +
           "(:dormitorios IS NULL OR p.dormitorios >= :dormitorios) AND " +
           "(:banos IS NULL OR p.banos >= :banos) AND " +
           "(:precioMin IS NULL OR p.precioDia >= :precioMin) AND " +
           "(:precioMax IS NULL OR p.precioDia <= :precioMax) AND " +
           "(p.activo = true)")
    Page<PropiedadVO> busquedaAvanzadaConPrecio(
            @Param("ciudad") String ciudad,
            @Param("pais") String pais,
            @Param("capacidad") Integer capacidad,
            @Param("dormitorios") Integer dormitorios,
            @Param("banos") Integer banos,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax,
            Pageable pageable);

    @Query("""
            SELECT p
            FROM   PropiedadVO p
            WHERE  ( LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))
                     OR :ciudad IS NULL )
              AND  ( LOWER(p.pais)   LIKE LOWER(CONCAT('%', :pais, '%'))
                     OR :pais IS NULL )
              AND  ( p.capacidad   >= :capacidad   OR :capacidad   IS NULL )
              AND  ( p.dormitorios >= :dormitorios OR :dormitorios IS NULL )
              AND  ( p.banos       >= :banos       OR :banos       IS NULL )
              AND  ( p.precioDia   >= :precioMin   OR :precioMin   IS NULL )
              AND  ( p.precioDia   <= :precioMax   OR :precioMax   IS NULL )
              AND  p.activo = true
            """)
        Page<PropiedadVO> buscarSinDisponibilidad(
                @Param("ciudad")      String ciudad,
                @Param("pais")        String pais,
                @Param("capacidad")   Integer capacidad,
                @Param("dormitorios") Integer dormitorios,
                @Param("banos")       Integer banos,
                @Param("precioMin")   BigDecimal precioMin,
                @Param("precioMax")   BigDecimal precioMax,
                Pageable pageable);

        @Query("""
            SELECT p
            FROM   PropiedadVO p
            WHERE  ( LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))
                     OR :ciudad IS NULL )
              AND  ( LOWER(p.pais)   LIKE LOWER(CONCAT('%', :pais, '%'))
                     OR :pais IS NULL )
              AND  ( p.capacidad   >= :capacidad   OR :capacidad   IS NULL )
              AND  ( p.dormitorios >= :dormitorios OR :dormitorios IS NULL )
              AND  ( p.banos       >= :banos       OR :banos       IS NULL )
              AND  ( p.precioDia   >= :precioMin   OR :precioMin   IS NULL )
              AND  ( p.precioDia   <= :precioMax   OR :precioMax   IS NULL )
              AND  p.activo = true
              AND  NOT EXISTS (
                     SELECT r
                     FROM   com.example.demo.reserva.model.ReservaVO r
                     WHERE  r.propiedad = p
                       AND  r.estado <> 'cancelada'
                       AND  r.fechaInicio <= :fechaFin
                       AND  r.fechaFin    >= :fechaInicio )
            """)
        Page<PropiedadVO> buscarConDisponibilidad(
                @Param("ciudad")      String ciudad,
                @Param("pais")        String pais,
                @Param("capacidad")   Integer capacidad,
                @Param("dormitorios") Integer dormitorios,
                @Param("banos")       Integer banos,
                @Param("precioMin")   BigDecimal precioMin,
                @Param("precioMax")   BigDecimal precioMax,
                @Param("fechaInicio") LocalDate fechaInicio,
                @Param("fechaFin")    LocalDate fechaFin,
                Pageable pageable);

}