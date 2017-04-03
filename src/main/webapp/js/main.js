
var mainPage = angular.module('mainPageApp', []);
mainPage.controller('mainPageController', function($scope,$http) {

	$scope.init = function(){
		
	//获取PPD初始化重要数据
      $http({method:'POST',url:'PPD/getTodaysPPDImporData.do',params:null}).success(function(res){

				if(res.resultCode == '111111'){
					//获取今天的数据
					var data = res.data[0];
					$scope.totalMoney = data.totalMoney.total;
					$scope.badTotalMoney = data.badTotalMoney.total;
					$scope.badMoreThanThirty = data.badMoreThanThirty.total;
					$scope.totalprofile = data.totalprofile.total;
					$scope.totalInvestment = data.totalInvestment.total;
					$scope.myTotalprofile = data.myTotalprofile.total;
					//获取昨天的数据和今日比
					data1 = res.data[1];
					
                    $scope.myTotalprofileCompare = Math.round(($scope.myTotalprofile - data1.myTotalprofile.total)*100)/100;
				    
					if($scope.myTotalprofileCompare<0){
						$scope.myTotalprofileCompareResult = '减少：'+$scope.myTotalprofileCompare+'元';
					}
					if($scope.myTotalprofileCompare==0){
						$scope.myTotalprofileCompareResult = '不变';
					}
					if($scope.myTotalprofileCompare>0){
						$scope.myTotalprofileCompareResult = '增加：'+$scope.myTotalprofileCompare+'元';
					}
					
                    $scope.totalInvestmentCompare = Math.round(($scope.totalInvestment - data1.totalInvestment.total)*100)/100;
				    
					if($scope.totalInvestmentCompare<0){
						$scope.totalInvestmentCompareResult = '取现：'+$scope.totalInvestmentCompare+'元';
					}
					if($scope.totalInvestmentCompare==0){
						$scope.totalInvestmentCompareResult = '不变';
					}
					if($scope.totalInvestmentCompare>0){
						$scope.totalInvestmentCompareResult = '充值：'+$scope.totalInvestmentCompare+'元';
					}
					
					$scope.totalMoneyCompare = Math.round(($scope.totalMoney - data1.totalMoney.total)*100)/100;
				    
					if($scope.totalMoneyCompare<0){
						$scope.totalMoneyCompareResult = '减少：'+$scope.totalMoneyCompare+'元';
					}
					if($scope.totalMoneyCompare==0){
						$scope.totalMoneyCompareResult = '不变';
					}
					if($scope.totalMoneyCompare>0){
						$scope.totalMoneyCompareResult = '增加：'+$scope.totalMoneyCompare+'元';
					}
					
                    $scope.totalprofileCompare = Math.round(($scope.totalprofile - data1.totalprofile.total)*100)/100;
				    
					if($scope.totalprofileCompare<0){
						$scope.totalprofileCompareResult = '减少：'+$scope.totalprofileCompare+'元';
					}
					if($scope.totalprofileCompare==0){
						$scope.totalprofileCompareResult = '不变';
					}
					if($scope.totalprofileCompare>0){
						$scope.totalprofileCompareResult = '增加：'+$scope.totalprofileCompare+'元';
					}
					
					$scope.badTotalMoneyCompare = Math.round(($scope.badTotalMoney - data1.badTotalMoney.total)*100)/100;
					if($scope.badTotalMoneyCompare<0){
						$scope.badTotalMoneyCompareResult = '减少：'+$scope.badTotalMoneyCompare+'元';
					}
					if($scope.badTotalMoneyCompare==0){
						$scope.badTotalMoneyCompareResult = '不变';
					}
					if($scope.badTotalMoneyCompare>0){
						$scope.badTotalMoneyCompareResult = '增加：'+$scope.badTotalMoneyCompare+'元';
					}
					
					$scope.badMoreThanThirtyCompare = Math.round(($scope.badMoreThanThirty - data1.badMoreThanThirty.total)*100)/100;
					if($scope.badMoreThanThirtyCompare<0){
						$scope.badMoreThanThirtyCompareResult = '减少：'+$scope.badMoreThanThirtyCompare+'元';
					}
					if($scope.badMoreThanThirtyCompare==0){
						$scope.badMoreThanThirtyCompareResult = '不变';
					}
					if($scope.badMoreThanThirtyCompare>0){
						$scope.badMoreThanThirtyCompareResult = '增加：'+$scope.badMoreThanThirtyCompare+'元';
					}
					
				}
				if(res.resultCode == '000000'){
	                alert(res.errMsg);
				}

	   })
		
	}
	$scope.init();
	
	
	//测试按钮
	$scope.functionTest = function functionTest(){
		var method = 'POST';
		var url = 'test.do';
		var data = {'firstPageURL':$scope.firstPageURL,'secondPageURL':$scope.secondPageURL};
		$http({method:method,url:url,params:null}).success(function(res){
			
		});
		

	}
	
	
	$scope.getPictureData = function(){
		$('#myChart').remove();
		var params = {
				dataSoure:$scope.dataSoure,
				startDate:$( '#input_01' ).val(),
			    endDate:$( '#input_02' ).val()
		};
		 $http({method:'POST',url:'PPD/getPictureData.do',params:params}).success(function(res){
			 var data = res.data;
			 $scope.rowData = [];
			 $scope.zhongData = [];
			 for(var i=0;i<data.length;i++){
				 $scope.rowData[i] = data[i].recordTime;
			 }		 
			 for(var i=0;i<data.length;i++){
				 $scope.zhongData[i] = data[i].total;
			 }
			 console.log($scope.rowData);
			 console.log($scope.zhongData);
			 
			 var datadata = {
						labels : $scope.rowData,
						datasets : [
							{
								barItemName: "name2",
								fillColor : "rgba(151,187,205,0.5)",
								strokeColor : "rgba(151,187,205,1)",
								data : $scope.zhongData
							}
						]
			 };
			 $('#pictureDiv').html('<canvas style="margin-top: 10px"  id="myChart" width="300" height="200"></canvas>');
			 var ctx = document.getElementById("myChart").getContext("2d");
			 var chartBar = new Chart(ctx).Bar(datadata);
			 initEvent(chartBar, clickCall);
		 });
	}
    
	//给日期进行初始化，最近12天
	$scope.initDate = function(){
		$scope.showName = "显示柱状图";
		var a = new Date(); 
		var today = getNewDay(a,0);
		var reachDay = getNewDay(a,-11);
		$scope.dataSoure = 'myTotalprofile';
		$( '#input_01' ).val(reachDay);
		$( '#input_02' ).val(today);
	}
	$scope.initDate();
	
	function getNewDay(dateTemp, days) {   
	    var millSeconds = Math.abs(dateTemp) + (days * 24 * 60 * 60 * 1000);  
	    var rDate = new Date(millSeconds);  
	    var year = rDate.getFullYear();  
	    var month = rDate.getMonth() + 1;  
	    if (month < 10) month = "0" + month;  
	    var date = rDate.getDate();  
	    if (date < 10) date = "0" + date;  
	    return (year + "-" + month + "-" + date);  
	} 
	
		function clickCall(evt){
			var activeBar = chartBar.getBarSingleAtEvent(evt);
			if ( activeBar !== null )
				alert(activeBar.label + ": " + activeBar.barItemName + " ____ " + activeBar.value);
		}
		
		function initEvent(chart, handler) {
			var method = handler;
			var eventType = "click";
			var node = chart.chart.canvas;
							
			if (node.addEventListener) {
				node.addEventListener(eventType, method);
			} else if (node.attachEvent) {
				node.attachEvent("on" + eventType, method);
			} else {
				node["on" + eventType] = method;
			}
		}
		var $input = $( '.datepicker' ).pickadate({
		    formatSubmit: 'yyyy/mm/dd',
		    // min: [2015, 7, 14],
		    container: '#container',
		    // editable: true,
		    closeOnSelect: false,
		    closeOnClear: false,
		})

		var picker = $input.pickadate('picker')

});

