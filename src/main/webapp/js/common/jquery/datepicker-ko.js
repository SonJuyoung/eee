/* Korean initialisation for the jQuery calendar extension. */
/* Written by DaeKwon Kang (ncrash.dk@gmail.com), Edited by Genie and Myeongjin Lee. */
( function( factory ) {
	"use strict";

	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define( [ "../widgets/datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
} )( function( datepicker ) {
"use strict";

datepicker.regional.ko = {
	closeText: "닫기",
	prevText: "이전달",
	nextText: "다음달",
	currentText: "오늘",
	monthNames: [ "1", "2", "3", "4", "5", "6",
	"7", "8", "9", "10", "11", "12" ],
	monthNamesShort: [ "1", "2", "3", "4", "5", "6",
	"7", "8", "9", "10", "11", "12" ],
	dayNames: [ "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" ],
	dayNamesShort: [ "일", "월", "화", "수", "목", "금", "토" ],
	dayNamesMin: [ "일", "월", "화", "수", "목", "금", "토" ],
	weekHeader: "주",
	dateFormat: "yy-mm-dd",
	firstDay: 0,
	isRTL: false,
	showMonthAfterYear: true,
	yearSuffix: "." };
datepicker.setDefaults( datepicker.regional.ko );

return datepicker.regional.ko;

} );


/* datepicker */
$(document).ready(function() {
	$(".everyDate").datepicker({
        
    });
	
    $(".startDate").datepicker({
        onClose: function( selectedDate ) {    
            // 시작일(startDate) datepicker가 닫힐때
            // 종료일(endDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
            $(".endDate").datepicker( "option", "minDate", selectedDate );
        } 
    });

    $(".endDate").datepicker({
        onClose: function( selectedDate ) {
            // 종료일(endDate) datepicker가 닫힐때
            // 시작일(startDate)의 선택할수있는 최대 날짜(maxDate)를 선택한 종료일로 지정 
            $(".startDate").datepicker( "option", "maxDate", selectedDate );
        }  
    });
});
$(document).ready(function() {
    $('.date').attr("readonly",true);
	if($('.date').hasClass("readonly")){
		$('.date.readonly').datepicker('option', 'disabled', true);
	}
});