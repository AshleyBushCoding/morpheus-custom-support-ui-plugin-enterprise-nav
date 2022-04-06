package com.morpheusdata.support

import com.morpheusdata.model.Permission
import com.morpheusdata.views.JsonResponse
import com.morpheusdata.views.ViewModel
import com.morpheusdata.web.PluginController
import com.morpheusdata.web.Route
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.MorpheusContext
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import com.slack.api.Slack
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.Blocks.*;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.HeaderBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.*;
import com.slack.api.model.block.composition.MarkdownTextObject
import com.slack.api.model.block.composition.PlainTextObject
import com.slack.api.model.block.composition.BlockCompositions.*;
import com.slack.api.model.block.element.BlockElements.*;

class SupportUIController implements PluginController {

	MorpheusContext morpheusContext
	Plugin plugin

	public SupportUIController(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	@Override
	public String getCode() {
		return 'supportUIController'
	}

	@Override
	String getName() {
		return 'Support UI Controller'
	}

	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	List<Route> getRoutes() {
		[
			Route.build("/support/sendSlackMessage", "sendSlackMessage", [Permission.build("supportUIPlugin", "full")])
		]
	}

	def sendSlackMessage(ViewModel <Map> model){
        println "Testing if the plugin works"
		def subject = model.request.getParameter("subject")
		def description = model.request.getParameter("description")
		def priority = model.request.getParameter("priority")
		def settings = morpheus.getSettings(plugin)
		def settingsOutput = ""
		settings.subscribe(
			{ outData -> 
                 settingsOutput = outData
        	},
        	{ error ->
                 println error.printStackTrace()
        	}
		)

		// Parse the plugin settings payload. The settings will be available as
		// settingsJson.$optionTypeFieldName i.e. - settingsJson.ddApiKey to retrieve the DataDog API key setting
		JsonSlurper slurper = new JsonSlurper()
		def settingsJson = slurper.parseText(settingsOutput)

        Slack slack = Slack.getInstance();
        String slackChannel = settingsJson.slackChannel;
        String token = settingsJson.slackToken;

        if (token == " "){
            model.object.response = "missing slack token"
            return JsonResponse.of(model.object)
        }

        if (slackChannel == " "){
            model.object.response = "missing slack channel"
            return JsonResponse.of(model.object)
        }

        // Initialize an API Methods client with the given token
        MethodsClient methods = slack.methods(token);

        // Craft the message payload
		List<LayoutBlock> message = new ArrayList();
		message.add(HeaderBlock
			.builder()
			.text(PlainTextObject
			.builder()
			.text("New Issue: ${subject}")
			.build())
			.build());
		message.add(DividerBlock
			.builder()
			.build());
		message.add(SectionBlock
			.builder()
			.fields(Arrays.asList(
			MarkdownTextObject
				.builder()
				.text("*Subject:* ${subject}")
				.build(),
			MarkdownTextObject
				.builder()
				.text("*Priority:* ${priority}")
				.build(),
			MarkdownTextObject
				.builder()
				.text("*User:* ${model.user.firstName} ${model.user.lastName}")
				.build(),
			MarkdownTextObject
				.builder()
				.text("*Email:* ${model.user.email}")
				.build(),
			MarkdownTextObject
				.builder()
				.text("*Description:* ${description}")
				.build()
			))
			.build());

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
        .channel(slackChannel)
        //.text(slackMessage)
		.blocks(message)
        .asUser(true)
        .build();

        // Get a response as a Java object
        ChatPostMessageResponse response = methods.chatPostMessage(request);
        model.object.response = response
		return JsonResponse.of(model.object)
	}
}