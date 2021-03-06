/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.radiology.web.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.module.radiology.RadiologyActivator;
import org.openmrs.module.radiology.Roles;
import org.openmrs.module.radiology.Utils;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ConfigController {
	
	static String typeRoles = "typeRoles";
	
	static String dummyUsers = "dummyUsers";
	
	@RequestMapping("/module/radiology/config.list")
	ModelAndView init(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/module/radiology/config");
		String command = request.getParameter("command");
		command = command == null ? "" : command;
		if (command.compareToIgnoreCase(typeRoles) == 0) {
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,
			    RadiologyActivator.typeAndRoles() ? "radiology.successConfig" : "radiology.failConfig");
		} else if (command.compareToIgnoreCase(dummyUsers) == 0) {
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,
			    createDummyUsers() ? "radiology.successConfig" : "radiology.failConfig");
		}
		
		populate(mav);
		
		return mav;
	}
	
	private void populate(ModelAndView mav) {
		try {
			mav.addObject("mwl", new File(Utils.mwlDir()).getCanonicalPath());
			mav.addObject("mpps", new File(Utils.mppsDir()).getCanonicalPath());
		}
		catch (IOException e) {
			// TODO handle
		}
	}
	
	boolean createDummyUsers() {
		try {
			Field[] roles = Roles.class.getDeclaredFields();
			for (Field role : roles) {
				Utils.createUser(Roles.value(role), "Admin123", Roles.value(role));
			}
		}
		catch (Throwable t) {
			return false;
		}
		return true;
	}
	
}
