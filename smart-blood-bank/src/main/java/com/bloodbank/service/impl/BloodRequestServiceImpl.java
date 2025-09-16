package com.bloodbank.service.impl;
import com.bloodbank.entity.BloodRequest;
import com.bloodbank.repository.BloodRequestRepository;
import com.bloodbank.service.BloodRequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodRequestServiceImpl implements BloodRequestService {

    private final BloodRequestRepository repo;

    public BloodRequestServiceImpl(BloodRequestRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<BloodRequest> getAll() {
        return repo.findAll();
    }

    @Override
    public List<BloodRequest> getByRequester(String requesterName) {
        return repo.findByRequesterName(requesterName);
    }

    @Override
    public BloodRequest save(BloodRequest request) {
        return repo.save(request);
    }

    @Override
    public void updateStatus(Long id, BloodRequest.Status status) {
        BloodRequest req = repo.findById(id).orElseThrow();
        req.setStatus(status);
        repo.save(req);
    }
}
