package compteur.controller;

import compteur.dto.DeviceDTO;
import compteur.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<String> createDevice(@RequestBody DeviceDTO dto) {
        deviceService.createDevice(dto);
        return ResponseEntity.ok("Device enregistré localement et dans ChirpStack !");
    }

    @PutMapping("/{devEui}")
    public ResponseEntity<String> updateDevice(@PathVariable String devEui, @RequestBody DeviceDTO dto) {
        dto.setDevEui(devEui); // Assure que l'ID dans l'URL est pris en compte
        deviceService.updateDevice(dto);
        return ResponseEntity.ok("Device mis à jour avec succès !");
    }

    @DeleteMapping("/{devEui}")
    public ResponseEntity<String> deleteDevice(@PathVariable String devEui) {
        deviceService.deleteDevice(devEui);
        return ResponseEntity.ok("Device supprimé !");
    }

    @GetMapping("/{devEui}")
    public ResponseEntity<DeviceDTO> getDeviceById(@PathVariable String devEui) {
        DeviceDTO dto = deviceService.getDeviceById(devEui);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }
}
