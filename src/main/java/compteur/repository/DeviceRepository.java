package compteur.repository;

import compteur.entite.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, String> {
}

