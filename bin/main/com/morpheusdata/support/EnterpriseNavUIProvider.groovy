package com.morpheusdata.enterpriseNav

import com.morpheusdata.core.AbstractGlobalUIComponentProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Account
import com.morpheusdata.model.TaskConfig
import com.morpheusdata.model.ContentSecurityPolicy
import com.morpheusdata.model.User
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel
import com.morpheusdata.model.UIScope
import com.morpheusdata.model.Permission
import java.util.logging.Logger

class EnterpriseNavUIProvider extends AbstractGlobalUIComponentProvider {
	Plugin plugin
	MorpheusContext morpheus

	EnterpriseNavUIProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheus = context
	}
		
	@Override
	MorpheusContext getMorpheus() {
		morpheusContext
	}

	@Override
	Plugin getPlugin() {
		plugin
	}

	@Override
	String getCode() {
		'enterprise-nav-ui'
	}
	
	String getProviderCode() {
		'enterprise-nav-ui'
	}

	@Override
	String getName() {
		'Enterprise Navigation UI'
	}
	
	String getProviderName() {
		'Enterprise Navigation UI'
	}

	@Override
	HTMLResponse renderTemplate(User user, Account account) {
		ViewModel<String> model = new ViewModel<String>()
		def nonse = morpheus.getWebRequest().getNonceToken()
		model.object = nonse.toString()
		getRenderer().renderTemplate("hbs/enterpriseNavUI", model)
	}

	@Override
	Boolean show(User user, Account account) {
		def show = true
		return show
	}

	ContentSecurityPolicy getContentSecurityPolicy() {
		def csp = new ContentSecurityPolicy()
		csp.scriptSrc = '*.lumen.com *.ctl.io'
		csp.frameSrc = '*.lumen.com *.ctl.io'
		csp.imgSrc = '*.lumen.com *.ctl.io'
		csp.styleSrc = 'https: *.lumen.com *.ctl.io'
		csp.connectSrc = 'https: *.lumen.com *.ctl.io'
		csp
	}

}