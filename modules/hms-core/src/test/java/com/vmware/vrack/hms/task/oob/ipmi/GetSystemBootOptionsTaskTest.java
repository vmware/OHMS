/* ********************************************************************************
 * GetSystemBootOptionsTaskTest.java
 *
 * Copyright © 2013 - 2016 VMware, Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * *******************************************************************************/

package com.vmware.vrack.hms.task.oob.ipmi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.vmware.vrack.hms.boardservice.BoardServiceProvider;
import com.vmware.vrack.hms.common.boardvendorservice.api.IBoardService;
import com.vmware.vrack.hms.common.exception.HmsException;
import com.vmware.vrack.hms.common.notification.TaskResponse;
import com.vmware.vrack.hms.common.servernodes.api.ServerNode;
import com.vmware.vrack.hp.ilo.boardservice.BoardService_iLO;
import com.vmware.vrack.intel.rmm.boardservice.BoardService_S2600GZ;

/**
* Test of GetSystemBootOptionsTask methods
* @author Yagnesh Chawda
*
*/
@Ignore
public class GetSystemBootOptionsTaskTest
{
	private static Logger logger = Logger.getLogger(GetSystemBootOptionsTaskTest.class);
	
	/**
	 * Test Case when Host is Valid, But there is No Board Service available
	 * @throws Exception
	 */
	@Test(expected = HmsException.class)
	public void test_executeTask_ValidHost_NoBoardMapping() throws Exception
	{
		logger.info("TS: GetSystemBootOptionsTaskTest: test_executeTask_ValidHost_NoBoardMapping");
		TaskResponse taskResponse = new TaskResponse(new ServerNode("N1", "10.28.197.202", "root", "root123"));
		GetSystemBootOptionsTask getSystemBootOptionsTask = new GetSystemBootOptionsTask(taskResponse);
		
		/*IBoardService boardService = new BoardService_S2600GZ();
		BoardServiceProvider.addBoardService(taskResponse.getNode().getServiceObject(), boardService, true);*/
		getSystemBootOptionsTask.executeTask();
		
	}
	
	/**
	 * Test Case when Host is Invalid, But there is Board Service for the node is available
	 * @throws Exception
	 */
	@Test(expected = HmsException.class)
	public void test_executeTask_InvalidHost_ValidBoardMapping() throws Exception
	{
		logger.info("TS: GetSystemBootOptionsTaskTest: test_executeTask_InvalidHost_ValidBoardMapping");
		TaskResponse taskResponse = new TaskResponse(new ServerNode("N1", "10.28.197.202", "root", "root123"));
		GetSystemBootOptionsTask getSystemBootOptionsTask = new GetSystemBootOptionsTask(taskResponse);
		
		IBoardService boardService = new BoardService_S2600GZ();
		BoardServiceProvider.addBoardService(taskResponse.getNode().getServiceObject(), boardService, true);
		getSystemBootOptionsTask.executeTask();
				
	}
	
	/**
	 * Test Case when Host is Invalid(with invalid OOB Credentials)
	 * @throws Exception
	 */
	@Test(expected = HmsException.class)
	public void test_executeTask_InvalidHostCreds() throws Exception
	{
		logger.info("TS: GetSystemBootOptionsTaskTest: test_executeTask_InvalidHostCreds");
		TaskResponse taskResponse = new TaskResponse(new ServerNode("N1", "10.28.197.202", "root", "root123"));
		GetSystemBootOptionsTask getSystemBootOptionsTask = new GetSystemBootOptionsTask(taskResponse);
		
		IBoardService boardService = new BoardService_S2600GZ();
		BoardServiceProvider.addBoardService(taskResponse.getNode().getServiceObject(), boardService, true);
		getSystemBootOptionsTask.executeTask();
				
	}
	
	/**
	 * Test Case when Host is Valid, and Board Mapping is also Valid
	 * @throws Exception
	 */
	@Test
	public void test_executeTask_ValidHost_ValidBoardMapping() throws Exception
	{
		logger.info("TS: GetSystemBootOptionsTaskTest: test_executeTask_ValidHost_ValidBoardMapping");
		TaskResponse taskResponse = new TaskResponse(new ServerNode("N1", "10.28.197.202", "root", "root123"));
		
		GetSystemBootOptionsTask getSystemBootOptionsTask = new GetSystemBootOptionsTask(taskResponse);
		
		IBoardService boardService = new BoardService_S2600GZ();
		BoardServiceProvider.addBoardService(taskResponse.getNode().getServiceObject(), boardService, true);
		getSystemBootOptionsTask.executeTask();
				
		ServerNode node = (ServerNode) taskResponse.getNode();
		assertNotNull(node.getSytemBootOptions());
		assertNotNull(node.getSytemBootOptions().getBiosBootType());
		assertNotNull(node.getSytemBootOptions().getBootDeviceInstanceNumber());
		assertNotNull(node.getSytemBootOptions().getBootDeviceSelector());
		assertNotNull(node.getSytemBootOptions().getBootDeviceType());
		assertNotNull(node.getSytemBootOptions().getBootOptionsValidity());
		
	}
	
	/**
	 * Test Case when Host is Valid, But there is Board Mapping for the node is Invalid
	 * @throws Exception
	 */
	@Test
	public void test_executeTask_ValidHost_InvalidBoardMapping() throws Exception
	{
		logger.info("TS: GetSystemBootOptionsTaskTest: test_executeTask_ValidHost_InvalidBoardMapping");
		TaskResponse taskResponse = new TaskResponse(new ServerNode("N1", "10.28.197.202", "root", "root123"));
		GetSystemBootOptionsTask getSystemBootOptionsTask = new GetSystemBootOptionsTask(taskResponse);
		
		IBoardService boardService = new BoardService_iLO();
		BoardServiceProvider.addBoardService(taskResponse.getNode().getServiceObject(), boardService, true);
		getSystemBootOptionsTask.executeTask();
				
		ServerNode node = (ServerNode) taskResponse.getNode();
		
		assertNotNull(node.getSytemBootOptions());
		assertNull(node.getSytemBootOptions().getBiosBootType());
		assertNull(node.getSytemBootOptions().getBootDeviceInstanceNumber());
		assertNull(node.getSytemBootOptions().getBootDeviceType());
		assertNull(node.getSytemBootOptions().getBootDeviceSelector());
		assertNull(node.getSytemBootOptions().getBootOptionsValidity());
	}
	
	/**
	 * Test Case TaskResponse Object is Null
	 * @throws Exception
	 */
	@Test(expected = NullPointerException.class)
	public void test_executeTask_InvalidTaskResponse() throws Exception
	{
		logger.info("TS: GetSystemBootOptionsTaskTest: test_executeTask_InvalidTaskResponse");
		TaskResponse taskResponse = null;
		ServerNode sNode = new ServerNode("N1", "10.28.197.202", "root", "root123");
		GetSystemBootOptionsTask getSystemBootOptionsTask = new GetSystemBootOptionsTask(taskResponse);
		
		IBoardService boardService = new BoardService_S2600GZ();
		BoardServiceProvider.addBoardService(sNode.getServiceObject(), boardService, true);
		getSystemBootOptionsTask.executeTask();
	}
	
}
