package com.bloodbank.service;

import com.bloodbank.entity.BloodRequest;
import java.util.List;

public interface BloodRequestService {

    List<BloodRequest> getAll();
    List<BloodRequest> getByRequester(String requesterName);
    BloodRequest save(BloodRequest request);
    void updateStatus(Long id, BloodRequest.Status status);
}
