package com.chacha.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chacha.dao.BoardDAO;
import com.chacha.dao.ReplyDAO;
import com.chacha.dto.BoardDTO;
import com.chacha.dto.ReplyDTO;

public class BoardDetailAction implements Action{

	@Override
	public ActionForward excute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "board/board_detail.jsp";
		
		
		String bno = request.getParameter("bno");
		System.out.println("======> " + bno);
		
		
		BoardDAO bDao = BoardDAO.getInstance();
		BoardDTO bDto = bDao.boardDetailView(bno);
		request.setAttribute("boardview", bDto);
		
		
		
		
		
		
		
		
		ActionForward forward = new ActionForward();
		forward.setPath(url);
		forward.setRedirect(false);	
		
		return forward;
		
	}
	
}
