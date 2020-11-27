console.log("this is script file")

function toggleSideBar(){

    if($(".sidebar").is(":visible"))
    {
     $(".sidebar").css("display : none");
     $(".content").css("margin: 0")
    }
    else{
        $(".sidebar").css("display : visible");
        $(".content").css("margin: 20%");

    }  
};
    
//const srch=()=>
//{ 
//	console.log("searchingg..");
//	let query = $("#search-input").val();
//	
//	if(query=='')
//		{
//		$(".search-result").hide();
//		
//		}
//	else
//		{
//		console.log(query);
//		
//		let url= `http://localhost:8080/search/${query}`;
//		fetch(url).then((response) =>{
//			return response.json();
//			
//		}).then((data) =>
//		{
//			console.log(data)
//		});
//		
//		$(".search-result").show();
//		}
//
//
// };
//    
