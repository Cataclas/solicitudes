package co.com.crediya.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Respuesta paginada")
public class PagedResponseDTO<T> {
    
    @Schema(description = "Contenido de la página")
    private List<T> content;
    
    @Schema(description = "Número de página actual", example = "0")
    private Integer page;
    
    @Schema(description = "Tamaño de página", example = "10")
    private Integer size;
    
    @Schema(description = "Total de elementos", example = "25")
    private Long totalElements;
    
    @Schema(description = "Total de páginas", example = "3")
    private Integer totalPages;
    
    @Schema(description = "Es la primera página", example = "true")
    private Boolean first;
    
    @Schema(description = "Es la última página", example = "false")
    private Boolean last;
}