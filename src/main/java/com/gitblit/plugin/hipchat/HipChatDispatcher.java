/*
 * Copyright 2014 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gitblit.plugin.hipchat;

import java.io.IOException;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import ro.fortsoft.pf4j.Extension;

import com.gitblit.manager.IRuntimeManager;
import com.gitblit.servlet.GitblitContext;
import com.gitblit.transport.ssh.commands.CommandMetaData;
import com.gitblit.transport.ssh.commands.DispatchCommand;
import com.gitblit.transport.ssh.commands.SshCommand;
import com.gitblit.transport.ssh.commands.UsageExample;
import com.gitblit.transport.ssh.commands.UsageExamples;
import com.gitblit.utils.StringUtils;

@Extension
@CommandMetaData(name = "hipchat", description = "HipChat commands")
public class HipChatDispatcher extends DispatchCommand {

	@Override
	protected void setup() {
		boolean canAdmin = getContext().getClient().getUser().canAdmin();
		if (canAdmin) {
			register(TestCommand.class);
			register(MessageCommand.class);
		}
	}

	@CommandMetaData(name = "test", description = "Post a test message")
	@UsageExamples(examples = {
			@UsageExample(syntax = "${cmd}", description = "Posts a test message to the default room"),
			@UsageExample(syntax = "${cmd} myRoom", description = "Posts a test message to myRoom")
	})
	public static class TestCommand extends SshCommand {

		@Argument(index = 0, metaVar = "ROOM", usage = "Destination Room for message")
		String room;

		@Argument(index = 1, metaVar = "TOKEN", usage = "Room Token for message")
		String token;

		/**
		 * Post a test message
		 */
		@Override
		public void run() throws Failure {
			RepoConfig config = new RepoConfig();
		    Payload payload = Payload.text("Test message sent from Gitblit");
		    config.setRoomName(room);
			config.setToken(token);
			payload.setConfig(config);

			try {
				IRuntimeManager runtimeManager = GitblitContext.getManager(IRuntimeManager.class);
				HipChatter.init(runtimeManager);
				HipChatter.instance().send(payload);
			} catch (IOException e) {
			    throw new Failure(1, e.getMessage(), e);
			}
		}
	}

	@CommandMetaData(name = "send", aliases = { "post" }, description = "Asynchronously post a message")
	@UsageExamples(examples = {
			@UsageExample(syntax = "${cmd} -m \"'this is a test'\"", description = "Asynchronously posts a message to the default room"),
			@UsageExample(syntax = "${cmd} myRoom -m \"'this is a test'\"", description = "Asynchronously posts a message to myRoom")
	})
	public static class MessageCommand extends SshCommand {

		@Argument(index = 0, metaVar = "ROOM", usage = "Destination Room for message")
		String room;

        @Argument(index = 1, metaVar = "TOKEN", usage = "Room Token for message")
        String token;

		@Option(name = "--message", aliases = {"-m" }, metaVar = "-|MESSAGE", required = true)
		String message;

		/**
		 * Post a message
		 */
		@Override
		public void run() throws Failure {
            RepoConfig config = new RepoConfig();
            Payload payload = Payload.text(message);
            config.setRoomName(room);
            config.setToken(token);
            payload.setConfig(config);

			IRuntimeManager runtimeManager = GitblitContext.getManager(IRuntimeManager.class);
			HipChatter.init(runtimeManager);
		    HipChatter.instance().sendAsync(payload);
		}
	}
}

