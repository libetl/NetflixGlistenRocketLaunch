package com.sage.myrocket.infrastructure.client;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.netflix.glisten.InterfaceBasedWorkflowClient
import com.netflix.glisten.WorkflowClientFactory
import com.netflix.glisten.WorkflowTags
import com.sage.myrocket.infrastructure.workers.RocketLaunchWorkers;
import com.sage.myrocket.model.rocket.RocketLaunchWorkflow
import com.sage.myrocket.model.rocket.RocketLaunchWorkflowDescriptionTemplate
import com.amazonaws.services.simpleworkflow.model.DomainInfos
import com.amazonaws.services.simpleworkflow.model.ListDomainsRequest
import com.amazonaws.services.simpleworkflow.model.RegistrationStatus

public class RocketLaunchClient {
    
    static String getOpenWorkflowDomain (AmazonSimpleWorkflow simpleWorkflow){
        ListDomainsRequest ldr = new ListDomainsRequest ().withRegistrationStatus(RegistrationStatus.REGISTERED)
        DomainInfos di = simpleWorkflow.listDomains (ldr)
        di.getDomainInfos().first().getName()
    }
    
    static void main (String[] args){
        // construct the AWS SDK object needed to use SWF
        def domainDescription = "Rocket Launch Sandbox Test"
        def taskList = "RocketLaunchList" // BTW, domain and taskList are SWF concepts that basically namespace your workflow

        AmazonSimpleWorkflow simpleWorkflow = RocketLaunchWorkers.initAwsSwf ();
        def domain = RocketLaunchClient.getOpenWorkflowDomain (simpleWorkflow) 
        
        def workflowClientFactory = new WorkflowClientFactory(simpleWorkflow, domain, taskList)
        // Now you have a factory for making a workflow client. Yeah, very Java like. I'm sorry.
        
        def workflowDescriptionTemplate = new RocketLaunchWorkflowDescriptionTemplate ()
        // now you just need one of these for describing your workflow
        
        // Now you can get your client
        InterfaceBasedWorkflowClient<RocketLaunchWorkflow> client = workflowClientFactory.getNewWorkflowClient(RocketLaunchWorkflow, workflowDescriptionTemplate, new WorkflowTags ().withTags([]))
        
        // asWorkflow() allows you to execute your workflow by calling the @Execute method of your workflow interface
        client.asWorkflow().launchTheRocket (UUID.randomUUID ())
    }
}
