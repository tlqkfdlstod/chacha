package com.chacha.action;

import java.io.File;

import java.io.IOException;

import java.util.Enumeration;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chacha.common.Constants;
import com.chacha.dao.BoardDAO;
import com.chacha.dto.BoardDTO;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;


public class BoardInsertPlayAction implements Action{

	@Override
	public ActionForward excute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "boardList.bizpoll";
		
		//Multipart를 사용하기 위해서는
		//cos.jar라는 라이브러리가필요하다.
		
		//파일업로드 처리
		File uploadDir = new File(Constants.UPLOAD_PATH);
		
		if(!uploadDir.exists()) { //저장할 경로가 없다면
			uploadDir.mkdir();    // 디렉토리를 생성하세요~
		}
		
		//request를 확장시킨 MultipartRequest 생성
		//request는 전부 String 타입으로 받으나, 
		//파일은 String타입이 아니므로 전송이 불가.
		//즉 request를 향상시킨 MultipartRequest를 사용함.
		//MultipartRequest를 사용하면 전부 multi타입으로 변환되므로, 
		//파일뿐만 아니라 기존의 String 타입도 전부 multi타입으로 받아야 함.
		MultipartRequest multi = new MultipartRequest(request, 								//request
													  Constants.UPLOAD_PATH,				//파일 업로드 다렉토리 경로
													  Constants.MAX_UPLOAD,					//파일 업로드 최대 용량
													  "UTF-8"								//인코딩 타입
													  ,new DefaultFileRenamePolicy());	    //파일 이름 중복 정책
																							//=> 중복 이름이 들어올 경우 
																							//DefaultFileRenamePolicy쪽을 포맷팅하여
																							//???.png에 ???1.png 이런식으로 숫자를 추가해줌.
		
		
		
		String title = multi.getParameter("title");
		String content = multi.getParameter("content");
		String writer = multi.getParameter("writer");
		String filename = " "; //(공백)
		int filesize = 0;
		
		
/*		System.out.println("제목 = "+ title);
		System.out.println("내용 = "+ content);
		System.out.println("작성자 = "+ writer);*/
		
		System.out.println(title + ", " + content + ", " + writer);
		
		try {	//입출력은 필수적으로 트라이캐치를 해줍니다.
			Enumeration files = multi.getFileNames(); //Enumeration = 배열 객체
			
			while(files.hasMoreElements()) {
				String file1 = (String)files.nextElement(); //file1 = 첫번째 첨부파일
				filename = multi.getFilesystemName(file1);	// 첫번째 첨부파일의 파일이름을 가져오고
				File f1 = multi.getFile(file1);				// 첫번째 첨부파일의 파일을 가져옴
				
				if(f1 != null) {
					filesize = (int)f1.length(); //첨부파일의 파일사이즈 저장
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(filename == null || filename.trim().equals("")) {//파일이름이 없을 경우
			filename = "-";									//파일네임에 "-"를 넣어주세요.
		}
		
		
		BoardDAO bDao = BoardDAO.getInstance();
		// dto 가방을 dao의 boardInsert로 보내줌.
		
		
		
		BoardDTO bDto = new BoardDTO(title, content, writer, filename, filesize);
		
		
		System.out.println(bDto.getFilesize() + bDto.getFilename());
		
		int result = bDao.boardInsert(bDto);
		
		
		if(result > 0) { //등록 성공
			url = "boardList.bizpoll";
			System.out.println("등록성공");
		}else { //등록실패
			url = "boardList.bizpoll";
			System.out.println("등록실패");
		}
		
		
	
		ActionForward forward = new ActionForward();
		forward.setPath(url);
		forward.setRedirect(true);	
		
		return forward;
		
	}

}
