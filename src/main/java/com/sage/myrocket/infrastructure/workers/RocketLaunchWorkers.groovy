package com.sage.myrocket.infrastructure.workers

import java.net.Authenticator
import java.net.PasswordAuthentication
import java.util.UUID

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.model.DomainInfo
import com.amazonaws.services.simpleworkflow.model.ListDomainsRequest
import com.amazonaws.services.simpleworkflow.model.RegisterDomainRequest
import com.amazonaws.services.simpleworkflow.model.RegistrationStatus
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution
import com.netflix.glisten.InterfaceBasedWorkflowClient
import com.netflix.glisten.WorkflowClientFactory
import com.netflix.glisten.WorkflowDescriptionTemplate
import com.netflix.glisten.WorkflowTags
import com.sage.myrocket.model.rocket.RocketLaunchWorkflow
import com.sage.myrocket.model.rocket.RocketLaunchWorkflowDescriptionTemplate;
import com.sage.myrocket.infrastructure.service.printlnrocket.CloseTheDoorsImpl
import com.sage.myrocket.infrastructure.service.printlnrocket.EvacuatePeopleAroundImpl
import com.sage.myrocket.infrastructure.service.printlnrocket.FireTheEnginesImpl
import com.sage.myrocket.model.rocket.RocketLaunchWorkflowImpl
import java.util.concurrent.TimeUnit

class RocketLaunchWorkers {

    static void initDomain (AmazonSimpleWorkflow simpleWorkflow, String domain, String domainDescription){
        def listDomainsRequest = new ListDomainsRequest().withRegistrationStatus(RegistrationStatus.REGISTERED)
        def domainInfos = simpleWorkflow.listDomains(listDomainsRequest)
        def domainExists = domainInfos.getDomainInfos().find { 
            DomainInfo domainInfo ->
                domainInfo.getName() == domain
        } != null
        if (!domainExists) {
            // we need to register the domain because it doesn't exist
            def registerDomainRequest = new RegisterDomainRequest()
                .withName(domain)
                .withDescription(domainDescription)
                .withWorkflowExecutionRetentionPeriodInDays("1")
            simpleWorkflow.registerDomain(registerDomainRequest)
        }
    }
        
    static void initWorkers (AmazonSimpleWorkflow simpleWorkflow, String domain, String taskList){
        
        ActivityWorker aw1 = new ActivityWorker(simpleWorkflow, domain, taskList)
        aw1.addActivitiesImplementation(new FireTheEnginesImpl())
        aw1.shutdownAndAwaitTermination(10, TimeUnit.SECONDS)
        aw1.setDisableTypeRegistrationOnStart(false)
        aw1.start()

        ActivityWorker aw2 = new ActivityWorker(simpleWorkflow, domain, taskList)
        aw2.addActivitiesImplementation(new EvacuatePeopleAroundImpl())
        aw2.shutdownAndAwaitTermination(10, TimeUnit.SECONDS)
        aw1.setDisableTypeRegistrationOnStart(false)
        aw2.start()

        ActivityWorker aw3 = new ActivityWorker(simpleWorkflow, domain, taskList)
        aw3.addActivitiesImplementation(new CloseTheDoorsImpl())
        aw3.shutdownAndAwaitTermination(10, TimeUnit.SECONDS)
        aw1.setDisableTypeRegistrationOnStart(false)
        aw3.start()

        WorkflowWorker wfw = new WorkflowWorker(simpleWorkflow, domain, taskList)
        wfw.addWorkflowImplementationType(RocketLaunchWorkflowImpl)
        aw1.setDisableTypeRegistrationOnStart(false)
        wfw.start()
    }
    
    static AmazonSimpleWorkflow initAwsSwf (){
        
        ClientConfiguration config = new ClientConfiguration().withSocketTimeout (70 * 1000)
        
        def proxyRegex = /^http:\/\/([^:]+):([^@]+)@([^:]+):([0-9]+)$/
        def matcher = (System.getenv ("https_proxy") =~ proxyRegex)

        config.proxyHost = matcher [0] [3]
        config.proxyPort = Integer.parseInt(matcher [0] [4])
        config.proxyDomain = "SAGEFR"
        config.proxyUsername = matcher [0] [1]
        config.proxyPassword = matcher [0] [2]
        
        String swfAccessId = System.getenv ("AWS_ACCESS_KEY_ID")
        String swfSecretKey = System.getenv ("AWS_SECRET_KEY")
        
        AWSCredentials awsCredentials = new BasicAWSCredentials (swfAccessId, swfSecretKey)
   
        AmazonSimpleWorkflow simpleWorkflow = new AmazonSimpleWorkflowClient(awsCredentials, config)
        simpleWorkflow.setEndpoint("swf.eu-west-1.amazonaws.com")
        simpleWorkflow
    }
    
    static void addTrapSigInt (AmazonSimpleWorkflow simpleWorkflow, String domain){
        sun.misc.Signal.handle(new sun.misc.Signal("INT"), new sun.misc.SignalHandler () {
            public void handle(sun.misc.Signal sig) {
    
                printf "Received SIGINT signal. Will teardown.";
    
                RocketStopWorkers.unregisterRocketLaunch (simpleWorkflow, domain)
    
                // Force exit anyway
                System.exit(1);
            }
          });
    }
    
	static void main (String[] args){
        // construct the AWS SDK object needed to use SWF
        def domain = "RocketLaunchTest" + new java.util.Date ().getTime () // probably get from your app config (however you do that in your app)
        def domainDescription = "Rocket Launch Sandbox Test"
        def taskList = "RocketLaunchList" // BTW, domain and taskList are SWF concepts that basically namespace your workflow

        AmazonSimpleWorkflow simpleWorkflow = this.initAwsSwf ();
        RocketLaunchWorkers.initDomain (simpleWorkflow, domain, domainDescription)
        RocketLaunchWorkers.initWorkers (simpleWorkflow, domain, taskList)
        RocketLaunchWorkers.addTrapSigInt (simpleWorkflow, domain)		
	}
}
