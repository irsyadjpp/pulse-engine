package com.irsyad.pulse.orchestrator.infrastructure.persistence;

import com.irsyad.pulse.orchestrator.domain.model.ProcessInstanceEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ProcessInstanceRepository {

    public ProcessInstanceEntity findById(String processId) {
        return ProcessInstanceEntity.find("processId", processId).firstResult();
    }

    public ProcessInstanceEntity findByProcessId(String processId) {
        return ProcessInstanceEntity.find("processId", processId).firstResult();
    }

    public List<ProcessInstanceEntity> findByStatus(String status) {
        return ProcessInstanceEntity.list("status", status);
    }

    public ProcessInstanceEntity findByBusinessKey(String businessKey) {
        return ProcessInstanceEntity.find("businessKey", businessKey).firstResult();
    }

    public ProcessInstanceEntity findByOrderId(String orderId) {
        return ProcessInstanceEntity.find("orderId", orderId).firstResult();
    }

    public List<ProcessInstanceEntity> findByCustomerId(String customerId) {
        return ProcessInstanceEntity.list("customerId", customerId);
    }

    public List<ProcessInstanceEntity> findByDecision(String decision) {
        return ProcessInstanceEntity.list("decision", decision);
    }

    public long countByStatus(String status) {
        return ProcessInstanceEntity.count("status", status);
    }
    
    public List<ProcessInstanceEntity> findAll() {
        return ProcessInstanceEntity.listAll();
    }
    
    @Transactional
    public void save(ProcessInstanceEntity entity) {
        // Use merge to handle both insert (new process) and update (existing process
        // being updated by event listeners)
        ProcessInstanceEntity.getEntityManager().merge(entity);
    }
}