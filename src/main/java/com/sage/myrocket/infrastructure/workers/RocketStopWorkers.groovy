package com.sage.myrocket.infrastructure.workers

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow
import com.amazonaws.services.simpleworkflow.model.ActivityTypeInfo
import com.amazonaws.services.simpleworkflow.model.ActivityTypeInfos
import com.amazonaws.services.simpleworkflow.model.DeprecateActivityTypeRequest
import com.amazonaws.services.simpleworkflow.model.DeprecateDomainRequest;
import com.amazonaws.services.simpleworkflow.model.DeprecateWorkflowTypeRequest
import com.amazonaws.services.simpleworkflow.model.ListActivityTypesRequest
import com.amazonaws.services.simpleworkflow.model.ListWorkflowTypesRequest
import com.amazonaws.services.simpleworkflow.model.RegistrationStatus
import com.amazonaws.services.simpleworkflow.model.WorkflowTypeInfo
import com.amazonaws.services.simpleworkflow.model.WorkflowTypeInfos

class RocketStopWorkers {
    
    static unregisterRocketLaunch (AmazonSimpleWorkflow simpleWorkflow, String domain){
        
        ListActivityTypesRequest latr = 
            new ListActivityTypesRequest().withDomain(domain).withRegistrationStatus(RegistrationStatus.REGISTERED)
        ListWorkflowTypesRequest lwtr = 
            new ListWorkflowTypesRequest().withDomain(domain).withRegistrationStatus(RegistrationStatus.REGISTERED)
        ActivityTypeInfos atis = simpleWorkflow.listActivityTypes(latr)
        for (ActivityTypeInfo ati : atis.getTypeInfos()){
            DeprecateActivityTypeRequest datr = new DeprecateActivityTypeRequest().withDomain(domain).withActivityType(ati.activityType);
            simpleWorkflow.deprecateActivityType(datr)
        }
        
        WorkflowTypeInfos wtis = simpleWorkflow.listWorkflowTypes(lwtr)
        for (WorkflowTypeInfo wti : wtis.getTypeInfos()){
            DeprecateWorkflowTypeRequest dwtr = new DeprecateWorkflowTypeRequest().withDomain(domain).withWorkflowType(wti.workflowType);
            simpleWorkflow.deprecateWorkflowType(dwtr)
        }
        
        DeprecateDomainRequest ddr = new DeprecateDomainRequest().withName(domain);
        simpleWorkflow.deprecateDomain(ddr);
    }
}
