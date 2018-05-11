package com.palmjoys.yf1b.act.gm.service;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;
import org.springframework.stereotype.Service;
import org.treediagram.nina.network.handler.JettyRequestHandler;

import com.palmjoys.yf1b.act.gm.model.GMDefine;

@Service
public class GmImp_charge implements JettyRequestHandler{

	@Override
	public String getPath() {
		return GMDefine.GM_CMD_CHARGE;
	}

	@Override
	public byte[] handleRequest(String target, Request baseRequest, HttpServletRequest request) {
		return null;
	}

}
