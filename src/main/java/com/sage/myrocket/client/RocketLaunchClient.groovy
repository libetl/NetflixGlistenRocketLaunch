package com.sage.myrocket.client;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.UUID;

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient
import com.amazonaws.services.simpleworkflow.flow.ActivityWorker
import com.amazonaws.services.simpleworkflow.flow.WorkflowWorker
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
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

class RocketLaunchClient {

	static void main (String[] args){
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

        // construct the AWS SDK object needed to use SWF
        def domain = "RocketLaunchTest" // probably get from your app config (however you do that in your app)
        def taskList = "RocketLaunchList" // BTW, domain and taskList are SWF concepts that basically namespace your workflow

        
        ActivityWorker aw1 = new ActivityWorker(simpleWorkflow, domain, taskList);
        aw1.addActivitiesImplementation(new FireTheEnginesImpl());
        aw1.start();

        ActivityWorker aw2 = new ActivityWorker(simpleWorkflow, domain, taskList);
        aw2.addActivitiesImplementation(new EvacuatePeopleAroundImpl());
        aw2.start();

        ActivityWorker aw3 = new ActivityWorker(simpleWorkflow, domain, taskList);
        aw3.addActivitiesImplementation(new CloseTheDoorsImpl());
        aw3.start();

        WorkflowWorker wfw = new WorkflowWorker(simpleWorkflow, domain, taskList);
        wfw.addWorkflowImplementationType(RocketLaunchWorkflowImpl);
        wfw.start();
                
		
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
