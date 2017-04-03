var commonSercher=angular.module("commonSercherAPP",[]);
commonSercher.controller("commonSercherC",function($scope,$http){

	$scope.jq = function find(){
		var method = 'POST';
		var url = 'commonSercher.do';
		var data = {'firstPageURL':$scope.firstPageURL,'secondPageURL':$scope.secondPageURL};
		$http({method:method,url:url,params:data}).success(function(res){
			
				if(res.resultCode == '111111'){
					console.log('成功获'+res.data);
				}
				if(res.resultCode =='000000'){
					alert(res.errMsg);
				}
				
			
		});
	}

	
	//获取总页面和URL规则
	$scope.getPagePosition = function getPagePosition(){
		var method = 'POST';
		var url = 'getPagePosition.do';
		var data = {'firstPageURL':$scope.firstPageURL,'secondPageURL':$scope.secondPageURL};
		$http({method:method,url:url,params:data}).success(function(res){
				if(res.resultCode == '111111'){
					var data = res.data;
					$scope.pagePosition = data.pagePosition;
					$scope.totalPage = data.totalPage;
				}
				if(res.resultCode =='000000'){
					alert(res.errMsg);
				}
				
			
		});
	}

	//显示元素内容
	$scope.showElementContent = function showElementContent(){
		var method = 'POST';
		var url = 'showElementContent.do';
		var data = {'firstPageURL':$scope.firstPageURL,'elementCharacter':$scope.elementCharacter};
		$http({method:method,url:url,params:data}).success(function(res){
			alert(res);
				if(res.resultCode == '111111'){
					var data = res.data;
					$scope.pagePosition = data.pagePosition;
					$scope.totalPage = data.totalPage;
				}
				if(res.resultCode =='000000'){
					alert(res.errMsg);
				}
				
			
		});
	}
	
	
	
});

