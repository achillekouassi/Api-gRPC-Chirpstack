package compteur.controller;

import compteur.dto.GatewayDTO;
import compteur.service.GatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gateways")
@RequiredArgsConstructor
public class GatewayController {

    private final GatewayService gatewayService;

    // Créer une gateway
    @PostMapping
    public ResponseEntity<Void> createGateway(@RequestBody GatewayDTO dto) {
        gatewayService.createGateway(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Récupérer toutes les gateways
    @GetMapping
    public ResponseEntity<List<GatewayDTO>> getAllGateways() {
        List<GatewayDTO> list = gatewayService.getAllGateways();
        return ResponseEntity.ok(list);
    }

    // Récupérer une gateway par son ID
    @GetMapping("/{gatewayId}")
    public ResponseEntity<GatewayDTO> getGatewayById(@PathVariable String gatewayId) {
        GatewayDTO dto = gatewayService.getGatewayById(gatewayId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    // Mettre à jour une gateway
    @PutMapping("/{gatewayId}")
    public ResponseEntity<Void> updateGateway(@PathVariable String gatewayId, @RequestBody GatewayDTO dto) {
        gatewayService.updateGateway(gatewayId, dto); // on ne compare plus les IDs
        return ResponseEntity.noContent().build();
    }


    // Supprimer une gateway
    @DeleteMapping("/{gatewayId}")
    public ResponseEntity<Void> deleteGateway(@PathVariable String gatewayId) {
        gatewayService.deleteGateway(gatewayId);
        return ResponseEntity.noContent().build();
    }
}

