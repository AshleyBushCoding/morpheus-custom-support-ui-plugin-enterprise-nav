package com.morpheusdata.enterpriseNav

import com.morpheusdata.model.ContentSecurityPolicy
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission
import com.morpheusdata.model.OptionType
import com.morpheusdata.views.HandlebarsRenderer

class EnterpriseNavUIPlugin extends Plugin {

	@Override
	String getCode() {
		return 'enterprise-nav-ui-integration'
	}

	@Override
	void initialize() {
		EnterpriseNavUIProvider enterpriseNavUIProvider = new EnterpriseNavUIProvider(this, morpheus)
		this.pluginProviders.put(enterpriseNavUIProvider.code, enterpriseNavUIProvider)
		this.setRenderer(new HandlebarsRenderer(this.classLoader))
		this.controllers.add(new EnterpriseNavUIController(this, morpheus))
		this.setName("Enterprise Nav UI Integration")
		this.setDescription("Enterprise Nav UI integration plugin")
		// probably not needed, delete later
		// this.settings << new OptionType(
		// 	name: 'Slack Token',
		// 	code: 'support-ui-plugin-slack-token',
		// 	fieldName: 'slackToken',
		// 	displayOrder: 0,
		// 	fieldLabel: 'Slack Token',
		// 	helpText: 'The Slack token used to send messages',
		// 	required: true,
		// 	inputType: OptionType.InputType.PASSWORD
		// )
		// this.settings << new OptionType(
		// 	name: 'Slack Channel',
		// 	code: 'support-ui-plugin-slack-channel',
		// 	fieldName: 'slackChannel',
		// 	displayOrder: 1,
		// 	fieldLabel: 'Slack Channel',
		// 	helpText: 'The Slack channel to send messages to',
		// 	required: true,
		// 	inputType: OptionType.InputType.TEXT
		// )
	}

	@Override
	void onDestroy() {
	}

	@Override
	public List<Permission> getPermissions() {
		Permission permission = new Permission('Enterprise Nav UI Integation', 'enterpriseNavUIPlugin', [Permission.AccessType.full])
		return [permission];
	}

}