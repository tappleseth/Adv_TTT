$(document).ready(function(){
	placeElements();
});

$(window).resize(function(){
	placeElements();
});

function placeElements(){
	
	var size = window.innerWidth > window.innerHeight ? 
	window.innerHeight : window.innerWidth;
	
	$(".top").css("height",String((3/10)*size) + "px");
	
	$(".top > .h1").css("font-size",String((1/25)*size) + "px");
	$(".top > .h2").css("font-size",String((1/30)*size) + "px");
	
	$(".middle").css("height",String((1/2)*size) + "px");
	$(".board div").css("font-size",String((1/30)*size) + "px");
	
	
	$(".bottom").css("height",String((1/5)*size) + "px");
	$(".reset").css("font-size",String((1/30)*size) + "px");
}

var app = angular.module('cheap_app',[])
app.controller('cheap_ctrl',function($scope,$http){
	
	$scope.boardID = -1;
	$scope.playerAvatar = "X";
	$scope.foeAvatar = "0";
	$scope.playerFirst = true;
	$scope.playerTurn = true;
	$scope.bottomMsg = "Elastic Beanstalk + Springboot = It Just Kind Of Works"
	
	$scope.handlePost = function(response){
			$scope.tv = [];
			var tileInfoArray = response.data.tiles;
			$scope.boardID = response.data.id;
			for (var i = 0; i < tileInfoArray.length; i++){
				var tileOwner = tileInfoArray[i];
				$scope.tv[i] = tileOwner;
			}
			$(".h3").text("(This is game # " + String(response.data.id) + ")");
	}

	$http.post('http://cheapttt.us-east-1.elasticbeanstalk.com/board?boardLength=3&humanGoesFirst=true').
	then(function(response){
		$scope.handlePost(response);
	});
	
	$scope.resetClick = function(){
		$http.post('http://cheapttt.us-east-1.elasticbeanstalk.com/board?boardLength=3&humanGoesFirst=true').
		then(function(response){
			$scope.handlePost(response);
			$scope.playerTurn = true;
			$scope.bottomMsg = "All systems reset OK for exciting new game+."
		});
	}
	
	$scope.tileClick = function(tileNumber){
		if (!$scope.playerTurn) return;
		$scope.playerTurn = false;
		//is tile available to pick? if so, just let front-end draw change
		if ($scope.tv[tileNumber] == "_"){
			$scope.tv[tileNumber] = $scope.playerAvatar;
		}
		else if ($scope.tv[tileNumber] == "X"){
			$scope.playerTurn = true;
			$scope.bottomMsg = "That tile is already controlled by you.";
			return;
		}
		else {
			//not allowed to steal occupied tiles
			$scope.playerTurn = true;
			$scope.bottomMsg = "You have failed to seize that tile from its owner.";
			return;
		}
		//send this change to backend and get response
		$http.put('http://cheapttt.us-east-1.elasticbeanstalk.com/board?boardID=' + $scope.boardID + '&tileNumber=' + tileNumber + '&avatar=' + $scope.playerAvatar).
		then(function(response){
			//computer makes move
			var foeNumber = response.data.tileNumber;
			//var isValid = response.data.isValid;
			$scope.tv[foeNumber] = $scope.foeAvatar;
			
			if (response.data.gameOver){
				if (response.data.winner === "O"){
					$scope.bottomMsg = "The computer's victory ushers in a new age of darkness.";
				}
				else if (response.data.winner === "X"){
					$scope.bottomMsg = "You have triumphed gloriously over the machines!";
				}
				else {
					$scope.bottomMsg = "Nobody wins. It is over.";
				}
			}
			else {
			    $scope.bottomMsg = "Game in progress.";
				$scope.playerTurn = true;
			}
		});
	};
});

