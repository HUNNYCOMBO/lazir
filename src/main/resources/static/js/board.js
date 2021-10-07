let index= {
   init:function(){
     
       $("#btn-delete").on("click",()=>{
    		 this.deleteById();
    	   });
       $("#btn-update").on("click",()=>{
    	   this.update();
       });
       $("#btn-reply-save").on("click",()=>{
    	   this.replySave();
       });
       
	 },

   
   
   deleteById:function(){
	   let id = $("#id").text();
	   console.log(id);
	   $.ajax({  
		   type:"DELETE",
		   url:"/api/board/"+ id,
		   contentType:"application/json; charset=utf-8",
		   dataType:"json" 
	   }).done(function(resp){
		   alert("글 삭제가 완료되었습니다.");
		   location.href="/board/list";
	   }).fail(function(error){
		   alert(JSON.stringify(error));
		   console.log(JSON.stringify(error));
	   });
   },
   
   
   update:function(){
	   let id =$("#id").val();
	   
	   let data ={
			     title:$("#title").val(),
			     content:$("#content").val()
	            };
	   $.ajax({  
		   type:"PUT",
		   url:"/api/board/"+id,
		  // data:JSON.stringify(data), 
		   contentType:"application/json; charset=utf-8",
		   dataType:"json" 
	   }).done(function(resp){
		   alert("글수정이 완료 되었습니다.");
		   location.href="/board/list";
	   }).fail(function(error){
		   alert(JSON.stringify(error));
	   });
   },
   
   replySave:function(){
    let data ={
		       content:$("#reply_Content").val()
               };
         let boardId = $("#boardId").val();
                console.log(data);
     $.ajax({  
	      type:"POST",
	      url:`/api/board/${boardId}/reply`,
	     data:JSON.stringify(data), 
	      contentType:"application/json; charset=utf-8",
	      dataType:"json" 
         }).done(function(resp){
	       alert("댓글 작성이 완료 되었습니다.");
	       location.href=`/board/${boardId}`;
         }).fail(function(error){
	        alert(JSON.stringify(error));
         });
     },
     
     replyDelete:function(boardId,replyId){
    	 $.ajax({  
    		 type:"DELETE",
    		 url:`/api/board/${boardId}/reply/${replyId}`,
    		 dataType:"json" 
    	 }).done(function(resp){
    		 alert("댓글삭제 성공.");
    		 location.href=`/board/${boardId}`;
    	 }).fail(function(error){
    		 alert("댓글삭제 실패.");
    		 alert(JSON.stringify(error));
    	 });
     },
    
//     replyInsert:function(boardId,replyId){
//    	 
//    	 let data ={
//  		       content:$("#reply_Content").val()
//                 };
//           let replyId = $("#replyId").val();
//                  console.log(data);
//    	 $.ajax({  
//    		 type:"PUT",
//    		 url:`/api/board/reply/${replyId}`,
//    		   data:JSON.stringify(data), 
//    		   contentType:"application/json; charset=utf-8",
//    		   dataType:"json"  
//    	 }).done(function(resp){
//    		 alert("댓글수정 성공.");
//    		 location.href=`/board/${boardId}`;
//    	 }).fail(function(error){
//    		 alert("댓글수정 실패.");
//    		 alert(JSON.stringify(error));
//    	 });
//     },
     
   
}
index.init();