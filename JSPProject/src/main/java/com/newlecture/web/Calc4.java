package com.newlecture.web;

import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/calc4")
public class Calc4 extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Cookie[] cookies = request.getCookies();
		// 사용자가 입력한 내용
		String value = request.getParameter("value");
		String operator = request.getParameter("operator");
		String dot = request.getParameter("dot");

		// 사용자가 입력한 내용을 표시(expression)
		String exp = "";
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals("exp")) {
					exp = c.getValue();
					break;
				}
			}
		}

		// 쿠키에서 읽어와서 사용자가 입력한 내용을 덧붙이는 부분
		if (operator != null && operator.equals("=")) {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			try {
				exp = String.valueOf(engine.eval(exp));
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else if(operator != null && operator.equals("C")) {
			exp = "";
		}
		else {
			exp += (value == null) ? "" : value;
			exp += (operator == null) ? "" : operator;
			exp += (dot == null) ? "" : dot;
		}

		
		// 그걸 쿠키로 저장하고 이걸 읽어서 CalcPage가 화면에 출력
		Cookie expCookie = new Cookie("exp", exp);
		
		if(operator != null && operator.equals("C"))
			expCookie.setMaxAge(0); // 쿠키 지우기
		
		response.addCookie(expCookie);
		response.sendRedirect("calcpage");

	}

}