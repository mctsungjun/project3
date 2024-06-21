function joinform_chk() {
    // Form 데이터 직렬화
    let frm = document.joinForm;
    let frmData = new FormData(frm);
    // alert("FormData: " + JSON.stringify(Object.fromEntries(frmData.entries())));

    $.ajax({
        url: "/sung/registerR",
        type: "POST",
        data: frmData,
        processData: false,  // 필수: FormData를 문자열로 변환하지 않음
        contentType: false,  // 필수: 컨텐츠 타입을 false로 설정하여 jQuery가 설정할 수 있도록 함
        success: function(resp) {
            console.log(resp);
            // 성공 시 처리할 코드
        },
        error: function(xhr, status, error) {
            console.error('AJAX 오류 발생:', status, error);
            // 오류 발생 시 처리할 코드를 추가할 수 있습니다.
        }
    });
}