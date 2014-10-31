package com.sage.myrocket.client;

import java.util.UUID;

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution
import com.netflix.glisten.InterfaceBasedWorkflowClient
import com.netflix.glisten.WorkflowClientFactory
import com.netflix.glisten.WorkflowDescriptionTemplate
import com.netflix.glisten.WorkflowTags
import com.sage.myrocket.model.rocket.RocketLaunchWorkflow
import com.sage.myrocket.model.rocket.RocketLaunchWorkflowDescriptionTemplate;

import com.sage.myrocket.infrastructure.service.printlnrocket.RocketLaunchWorkflowImpl;

class RocketLaunchClient {

	static void main (String[] args){
		ClientConfiguration config = new ClientConfiguration().withSocketTimeout (70 * 1000)
		String swfAccessId = System.getenv ("AWS_ACCESS_KEY_ID")
		String swfSecretKey = System.getenv ("AWS_SECRET_KEY")
		
		AWSCredentials awsCredentials = new BasicAWSCredentials (swfAccessId, swfSecretKey)
   
		AmazonSimpleWorkflow simpleWorkflow = new AmazonSimpleWorkflowClient(awsCredentials, config)
        simpleWorkflow.setEndpoint("swf.eu-west-1.amazonaws.com")
        
		// construct the AWS SDK object needed to use SWF
		def domain = "RocketLaunch" // probably get from your app config (however you do that in your app)
		def taskList = "RocketLaunchList" // BTW, domain and taskList are SWF concepts that basically namespace your workflow
		
		def workflowClientFactory = new WorkflowClientFactory(simpleWorkflow, domain, taskList)
		// Now you have a factory for making a workflow client. Yeah, very Java like. I'm sorry.
		
		def workflowDescriptionTemplate = new RocketLaunchWorkflowDescriptionTemplate ()
		// now you just need one of these for describing your workflow
		
		// Now you can get your client
		InterfaceBasedWorkflowClient<RocketLaunchWorkflow> client = workflowClientFactory.getNewWorkflowClient(RocketLaunchWorkflow, workflowDescriptionTemplate, new WorkflowTags ().withTags([]))
		
		// asWorkflow() allows you to execute your workflow by calling the @Execute method of your workflow interface
		client.asWorkflow().launchTheRocket (UUID.randomUUID ())
		
		// Perhaps you need the ids of the workflow execution. InterfaceBasedWorkflowClient is a WorkflowClientExternal.
		WorkflowExecution workflowExecution = client.workflowExecution
	}
}
