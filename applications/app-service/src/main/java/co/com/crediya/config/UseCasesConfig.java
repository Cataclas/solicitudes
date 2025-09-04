package co.com.crediya.config;

import co.com.crediya.model.estado.gateways.EstadoRepository;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.crediya.usecase.solicitud.RegistrarSolicitudUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {
    
    @Bean
    public RegistrarSolicitudUseCase registrarSolicitudUseCase(
            SolicitudRepository solicitudRepository,
            TipoPrestamoRepository tipoPrestamoRepository,
            EstadoRepository estadoRepository) {
        return new RegistrarSolicitudUseCase(solicitudRepository, tipoPrestamoRepository, estadoRepository);
    }
}
