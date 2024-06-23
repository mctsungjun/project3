//로그인 버튼 클릭됨----------------------------------------
let loginForm=()=>{
    $.ajax({
        url:"/sung/loginF",
        type:"GET",
        success:(resp)=>{
            
            let temp=$(resp).find(".login");
            $(".change").html(temp);
        }
    })
}
//로그아웃 버튼 클릭됨-------------------
let logOut=()=>{
	$.ajax({
		url:"/sung/logout",
		type:"GET",
		success:(resp)=>{
			sessionStorage.setItem("id",null);
			location.href="/";
		}
	})
}
//회원상세보기 보튼 클릭
let detail = ()=>{
	$.ajax({
		url:"/sung/detail",
		type:"GET",
		success:(resp)=>{
			let temp = $(resp).find(".change");
			$(".change").html(temp);
			
		}
	})
}
// 대표이미지 바뀌때 테두리변함
let repreImage="";
let change=(tag,photo)=>{
	let allImg = document.querySelectorAll(".photo_list img");
	allImg.forEach((img)=>{
		img.style.border="5px solid blue";
	})
	tag.style.border="5px solid #aaa";
	repreImage=photo;
}

//대표 이미지 수정폼


let	btnChangePhoto=()=>{
		$.ajax({
			url:"/sung/repreChangeForm",
			type:"GET",
			success:(resp)=>{
				let temp = $(resp).find(".photoSection");
				$(".photoSection").html(temp);
				// 대표이미지수정
				let btnRepreChange = document.querySelector(".btnRepreChange");
				btnRepreChange.onclick=()=>{
					var id = sessionStorage.getItem("id");
					
				
					$.ajax({
						url:"/sung/changePhoto",
						type:"GET",
						data:{"id": id,"photo":repreImage},
						success:(resp)=>{
							detail();
						
						}
					})
				}
			}
		})
	}
// 홈으로 이동---------------------------------------------
let btnGoHome = ()=>{
	location.href="/";
}

// 목록으로 이동 (관리자만)
let btnListForm = ()=>{
	let managerCode = prompt("관리코드를 입력하세요");
	if( managerCode !=null && managerCode !=""){
		$.ajax({
			url:"/sung/list",
			type:"GET",
			data:{"code":managerCode},
			success:(resp)=>{
				let temp =$(resp).find(".change");
				$(".change").html(temp);
			}
		})
	}
}

//회원가입 버튼 클릭됨-----------------------------------------
let registerForm=()=>{
    $.ajax({
        url:"/sung/registerF",
        type:"GET",
        success:(resp)=>{
            let temp=$(resp).find(".register");
            $(".change").html(temp);
        }
    })
}
//이미지등록버튼 클릭됨-------------------------------------------------
function regiPhoto() {
	// photoSection의 내용을 새로운 내용으로 변경
	var photoSection = document.querySelector('.photoSection');
	photoSection.innerHTML = `
	<div class="container mt-5">
	<div class="col-md-5 m-auto">	
	<label>
			<h6 class="text-success fw-bolder">파일첨부<h6>
			<input type="file" id="fileInput" class="form-control" name="files" multiple onChange="fileChange(this)"/>
		</label>
		<br/>
		<button type="button" class="btn btn-outline-primary" onClick="uploadFiles()">업로드</button>
		<fieldset class="repre">
                    <legend>대표이미지를 선택해 주세요</legend>
                </fieldset>
	</div>
	</div>
	`;
}
//대표이미지 선택---------------------------------------------
let fileChange=(tag)=>{
	let repre = document.querySelector(".repre");
	repre.innerHTML = ''; // 하위태그 모두 삭제
	let legend = document.createElement("legend");
	legend.textcontent="대표이미지를 선택해주세요";
	repre.appendChild(legend);
	for(f of tag.files){
		console.log(f.name);
		let chkbox = document.createElement('input');
		let label = document.createElement('label');
		let br = document.createElement("br");
		chkbox.type="radio";
		chkbox.name="photo";
		chkbox.value = f.name;

		label.textContent=f.name;
		label.prepend(chkbox);
		
		repre.appendChild(label);
		repre.appendChild(br)
	}
}


//사진/파일 업로드-----------------------------------------------
function uploadFiles(){
	let input = document.getElementById('fileInput');
	let frmData = new FormData();
	let selectedPhoto = document.querySelector('input[name="photo"]:checked');
	for (let i = 0; i < input.files.length; i++) {
        frmData.append('files', input.files[i]); // 파일 추가
    }
	if(selectedPhoto){
		frmData.append('reprePhoto',selectedPhoto.value); //선택된 대표이미지
	}
	$.ajax({
		url:"/sung/upload",
		type:"POST",
		data:frmData,
		processData:false,
		contentType:false,
		success:(resp)=>{
			console.log(resp);
			detail();
		}
	})
}

// 수정버튼 클릭됨-------------------------------------------

let modifyFrom=()=>{
	let id = sessionStorage.getItem("id");
	$.ajax({
		url:"/sung/modify",
		type:"GET",
		data:{"id":id},
		success:(resp)=>{
			let temp =$(resp).find(".change");
			$(".change").html(temp);
		}

	})
}

//아이디/비번 찾기폼------------------------------------
  
function findIdPwd() {
       

        $.ajax({
            url:"/sung/findIdPwd",
            type: "GET",
            success: (resp)=> {
                
                // 응답 처리
                let temp = $(resp).find(".change");
                $(".change").html(temp);
            }
        });
    };

// 아이디/비번찾기 함수들-------------------------------------------
function inputSendit(num) {
	if(keyCode==13) {
		idpwsearch(num);
	}
}


function idpwsearch(num)
{
if(num==2){

		var form = document.idsearch_form2;
		if(form.name.value==""){
			alert("이름을 입력해주세요.");
			form.name.focus();
			return;
		}else if(form.email.value==""){
			alert("이메일을 입력해주세요.");
			form.email.focus();
			return;
		}
	}

		form.submit();
}

//비밀번호----------------------------------------------------------
function text_chkpw(){
	var form = document.querySelector("#pwd").value;
    var pattern = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,20}$/;
	if(form.length < 6 || pattern.test(form) == false){
		$('#no_pw').show();
		$('#ok_pw').hide();
	}else{
		$('#no_pw').hide();
		$('#ok_pw').show();
	}
	form.passwd_check.value = "";
	$('#no_multipw').hide();
	$('#ok_multipw').hide();
}
function multi_chkpw(){
	var formchk = document.querySelector("#pwdchk").value;
    var form = document.querySelector("#pwd").value;
	if(form != formchk) {
		$('#no_multipw').show();
		$('#ok_multipw').hide();
	}else{
		$('#no_multipw').hide();
		$('#ok_multipw').show();
	}
}
// 체크박스 선택시 필요한 양식 표시-------------------------------------------

function joinform_chk(event)     
{
	var form = document.join_form;
	var checked = $("input[name='chked']:checkbox:checked").val();
	

			if(form.id.value==""){
				alert('아이디를 입력해주세요.');
				form.id.focus();
                event.preventDefault();
				return;	
			}
			// else if(form.id.value.length < 3 || form.id.value.length > 16) {
			// 	alert("아이디는 3~15자로 입력 주세요.");
			// 	form.id.focus();
            //     event.preventDefault();
			// 	return;
			// }
			else if(form.idchk.value==''){
				alert("아이디 중복체크하여 주세요.");
                event.preventDefault();
				return;
			}
			else if(form.pwd.value=="") {
				alert("비밀번호를 입력해 주세요.");
				form.pwd.focus();
                event.preventDefault();
				return;
			}
			// else if(form.pwd.value.length < 6 || text_chkpw()(form.passwd.value) == false){

			// 	alert("비밀번호는 영문+숫자 조합 6자리 이상으로 입력해 주세요.");
			// 	form.pwd.focus();
            //     event.preventDefault();
			// 	return;

			// }
            else if(form.pwdchk.value=="") {
				alert("비밀번호확인를 입력해 주세요.");
				form.pwd_check.focus();
                event.preventDefault();
				return;
			}
			else if(form.pwd.value != form.pwdchk.value) {
				alert("비밀번호가 정확하지 않습니다. 정확히 입력해 주세요.");
				form.pwdchk.focus();
                event.preventDefault();
				return;
			}
			else if(form.name.value=="") {
				alert("이름을 입력해 주세요.");
				form.name.focus();
                event.preventDefault();
				return;
			}
			else if(form.birth.value=="") {
				alert("생년월일을 입력해 주세요.");
				form.birth.focus();
                event.preventDefault();
				return;
			}


			else if(form.postcode.value=="") {
				alert("우편번호를 입력해 주세요.");
				form.postcode.focus();
                event.preventDefault();
				return;
			}
			
			else if(form.roadAddress.value=="") {
				alert("주소를 입력해 주세요.");
				form.roadAddress.focus();
                event.preventDefault();
				return;
			}
			else if(form.detailAddress.value=="") {
				alert("상세주소를 입력해 주세요.");
				form.detailAddress.focus();
                event.preventDefault();
				return;
			}
			else if(form.emailName.value=="") {
				alert("이메일을 입력해 주세요.");
				form.emailName.focus();
                event.preventDefault();
				return;
			}
			else if(form.phone1.value=="") {
				alert("회원님의 휴대폰 번호를 입력해 주세요.");
				form.phone1.focus();
                event.preventDefault();
				return;
			}
			else if(form.phone2.value=="") {
				alert("회원님의 휴대폰 번호를 입력해 주세요.");
				form.phone2.focus();
                event.preventDefault();
				return;
			}
			else if(form.phone3.value=="") {
				alert("회원님의 휴대폰 번호를 입력해 주세요.");
				form.phone3.focus();
                event.preventDefault();
				return;
			
			}else{
				form.submit();
				
			}
	}
//--------------------------------------------------------주소----------------------------
         //본 예제에서는 도로명 주소 표기 방식에 대한 법령에 따라, 내려오는 데이터를 조합하여 올바른 주소를 구성하는 방법을 설명합니다.
         let sample4_execDaumPostcode=()=> {
            var postcodePopup = new daum.Postcode({
                oncomplete: function(data) {
                  
                    var roadAddr = data.roadAddress; // 도로명 주소 변수
                    var extraRoadAddr = ''; // 참고 항목 변수
        
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraRoadAddr += data.bname;
                    }
                   
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                       extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                 
                    if(extraRoadAddr !== ''){
                        extraRoadAddr = ' (' + extraRoadAddr + ')';
                    }
        
                  
                    document.getElementById('postcode').value = data.zonecode;
                    document.getElementById("roadAddress").value = roadAddr;
                    document.getElementById("jibunAddress").value = data.jibunAddress;
       
                
                }
                
            }).open();

        }


 // 아이디체크----------------------------------------------------
function userChk()
{

    var userId = document.querySelector("#id");
    var pattern = /^(?=.*[a-zA-Z])(?=.*[0-9]).{3,16}$/;


    if(userId.value==""){
        alert('아이디를 입력해주세요.');
        userId.focus();
        return;
    }else if(userId.value.length < 3 || userId.value.length > 16) {
        alert("아이디는 3~15자의 영문과 숫자를 포함해 주세요.");
        userId.focus();
        return;
    }else if(!pattern.test(userId.value)){
    alert('아이디는 영문과 숫자를 포함해 주세요.');
        return;
   }else{

            $.ajax({
                url:"/memberId/chk",
                type:"GET",
                data:{"userId":userId.value},
                success:(resp)=>{

                    if(resp==0){
                        console.log(resp);
                        alert("사용가능한 아이디 입니다.");
                      
                    }else{
                        console.log(resp);
                        alert("현재 사용중인 아이디 입니다.");
                        userId.value="";
                        userId.focus();
                    }
                }
            })

        };
    

    
}
//이메일 option값 input에 넣기-------------------------------------------
function updateEmailInput(){
    var select = document.getElementById("email_sel");
    var selectedValue = select.value;
    var email2 = document.getElementById("email2");
    email2.value = selectedValue;
}   



       



    
  