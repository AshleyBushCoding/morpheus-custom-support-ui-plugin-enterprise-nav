package com.morpheusdata.support

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission
import com.morpheusdata.model.OptionType
import com.morpheusdata.views.HandlebarsRenderer

class SupportUIPlugin extends Plugin {

	@Override
	String getCode() {
		return 'support-ui-integration'
	}

	@Override
	void initialize() {
		SupportUIProvider supportUIProvider = new SupportUIProvider(this, morpheus)
		this.pluginProviders.put(supportUIProvider.code, supportUIProvider)
		this.setRenderer(new HandlebarsRenderer(this.classLoader))
		this.controllers.add(new SupportUIController(this, morpheus))
		this.setName("Support UI Integration")
		this.setDescription("Support UI integration plugin")
		this.settings << new OptionType(
			name: 'Slack Token',
			code: 'support-ui-plugin-slack-token',
			fieldName: 'slackToken',
			displayOrder: 0,
			fieldLabel: 'Slack Token',
			helpText: 'The Slack token used to send messages',
			required: true,
			inputType: OptionType.InputType.PASSWORD
		)
		this.settings << new OptionType(
			name: 'Slack Channel',
			code: 'support-ui-plugin-slack-channel',
			fieldName: 'slackChannel',
			displayOrder: 1,
			fieldLabel: 'Slack Channel',
			helpText: 'The Slack channel to send messages to',
			required: true,
			inputType: OptionType.InputType.TEXT
		)
	}

	@Override
	void onDestroy() {
	}

	@Override
	public List<Permission> getPermissions() {
		Permission permission = new Permission('Support UI Integation', 'supportUIPlugin', [Permission.AccessType.full])
		return [permission];
	}
}