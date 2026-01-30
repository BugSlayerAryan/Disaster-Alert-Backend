package com.DisasterAlert.service;

import com.DisasterAlert.model.Disaster;
import com.DisasterAlert.repository.DisasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisasterService {
    private final DisasterRepository disasterRepository;

    public DisasterService(DisasterRepository disasterRepository) {
        this.disasterRepository = disasterRepository;
    }

    public Disaster addDisaster(Disaster disaster) {
        disaster.setStatus("ACTIVE");
        return disasterRepository.save(disaster);
    }

    public List<Disaster> getActiveDisasters() {
        return disasterRepository.findAll().stream()
                .filter(d -> d.getStatus().equals("ACTIVE"))
                .toList();
    }

    public Disaster updateDisasterStatus(Long id, String status) {
        Disaster d = disasterRepository.findById(id).orElseThrow(() -> new RuntimeException("Disaster not found"));
        d.setStatus(status);
        return disasterRepository.save(d);
    }
}
